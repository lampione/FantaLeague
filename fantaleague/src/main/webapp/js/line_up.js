Array.prototype.indexOfId = function(f) {
    for (var i = 0; i < this.length; ++i) {
        if ( f.id == this[i].id )
            return i;
    	}
    return -1;
};

function comparer(otherArray) {
	return function(current) {
		return otherArray.filter(function(other) {
			return other.id == current.id
		}).length == 0;
	}
};

Array.prototype.diff = function(a) {
	return this.filter(function(i) {
		return a.indexOf(i) < 0;
	});
};

$(document).ready(function() {
	
	$("#nav-placeholder").load("navbar.jsp", function() {

		$('#search-player').on('input', function() {
			
			$(this).attr('disabled', 'disabled');
			if( $(this).val().length != 0 ){
				$('#dropdown-players').show();
			}
			else{
				$('#dropdown-players').hide();				
			}

			// query db
			var query = $(this).val();
			query = query.replace(/ /g, "_");

			$.getJSON('find-players?q=' + query, function(response) {
				
				$('#search-player').removeAttr('disabled').focus();
				$('#dropdown-players').empty();
				$.each(response, function(index, item) {
				    $("#dropdown-players").append("<a class='dropdown-item px-2' style='cursor: pointer;' data-href='" + item.id +"'>" + item.name + "</a>");
				});
			});

		});
		
		$('#dropdown-players').on('click', 'a', function() {
			var url = "player-stats?pid=" + $(this).data('href');
			window.location = url;
		});
		
	});
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}
	
	if ($('#expired').val() == 'true') {
		
		bootbox.alert("The max time to line up has expired!", function() {
			window.location = "home";
		});

	} else {

		roleMap = new Map();
		roleMap.set("g", "Goalkeepers");
		roleMap.set("d", "Defenders");
		roleMap.set("m", "Midfielders");
		roleMap.set("f", "Forwards");

		setupTables();
		clearTable();
		getPlayers();

		moduleG = null;
		moduleD = null;
		moduleM = null;
		moduleF = null;

		totalPlayers = 0;

		goalkeepers = [];
		defenders = [];
		midfielders = [];
		forwards = [];

		tg = [];
		td = [];
		tm = [];
		tf = [];

		bench = [];
		
		setClickContainer();
		setClickStarters();
		setClickBench();
	}

});

function setValue(maxLineUp){
	var canLineUp = $.ajax({ 
			url: "line-up?mlineup=" + maxLineUp, 
			dataType : "text/plain", 
			async : false
	}); 
	
	if( canLineUp.responseText == 'true'){
		
		bootbox.alert("The max time to line up has expired!", function() {
			window.location = "home";
		});
		return false;		
	}
	else{
		$('#starters-g').val(JSON.stringify(goalkeepers));
		$('#starters-d').val(JSON.stringify(defenders));
		$('#starters-m').val(JSON.stringify(midfielders));
		$('#starters-f').val(JSON.stringify(forwards));
		$('#bench').val(JSON.stringify(bench));
		return true;
	}
	
}

function clearTable(){

	gTable.clear();
	starterTable.clear();
	benchTable.clear();

	starterTable.draw();
	benchTable.draw();

}

function getPlayers() {

	$.getJSON("find-players?tid=" + $('#t-id').val(), function(response) {
		
		$.each(response, function(index, item) {
			window['t' + item.role].push(item);
		});

		$.getJSON("find-players?tid=" + $('#t-id').val() + "&lineup=0", function(responseL) {
			
			$.each(responseL, function(index, item) {
				
				console.log(item);

				if( item.player.role == 'g' ){
					
					if( item.status == 'S' ){
						goalkeepers.push(item.player);
					} else{
						bench.push(item.player);
					}
				
				} else if( item.player.role == 'm'  ){

					if( item.status == 'S' ){
						midfielders.push(item.player);
					} else{
						bench.push(item.player);
					}
				
				} else if( item.player.role == 'd' ){

					if( item.status == 'S' ){
						defenders.push(item.player);
					} else{
						bench.push(item.player);
					}

				} else if( item.player.role == 'f' ){

					if( item.status == 'S' ){
						forwards.push(item.player);
					} else{
						bench.push(item.player);
					}
				}

			});
			
			redrawOrder();
			redrawBench();
			redrawTeam();

			if (goalkeepers.length > 0) {
				moduleG = 1;	
				moduleD = defenders.length;
				moduleM = midfielders.length;
				moduleF = forwards.length;
				$('#module-selected').text(moduleD + ''+ moduleM + '' + '' +moduleF);
				totalPlayers = moduleG + moduleD + moduleM + moduleF;
			}

		});
	});
		
}

function setupTables() {
	
	gTable = $('#table-team-lineup').DataTable({
		searching: false,
		paging: false,
		ordering: false,
		bInfo: false,
		drawCallback: function (settings) {

			var api = this.api();
            var rows = api.rows( {page:'current'} ).nodes();
            var last=null;
 
            api.column(1, {page:'current'}).data().each( function (group, i) {
                if ( last !== group ) {
                    $(rows).eq( i ).before(
                        '<tr class="group table-group-header theme-bg-' + group + '"><td></td><td></td><td colspan="3">'+roleMap.get(group)+'</td></tr>'
                    );
                    last = group;
                }
            });
            
        }
	});

	starterTable = $('#table-starters').DataTable({
		searching: false,
		paging: false,
		ordering: false,
		bInfo: false,
		drawCallback: function (settings) {

			var api = this.api();
            var rows = api.rows( {page:'current'} ).nodes();
            var last=null;
 
            api.column(1, {page:'current'}).data().each( function (group, i) {
                if ( last !== group ) {
                    $(rows).eq( i ).before(
                        '<tr class="group table-group-header theme-bg-' + group + '"><td></td><td></td><td colspan="3">'+roleMap.get(group)+'</td></tr>'
                    );
                    last = group;
                }
            });
            
        }
	});

	benchTable = $('#table-bench').DataTable({
		searching: false,
		paging: false,
		ordering: false,
		bInfo: false,
		drawCallback: function (settings) {

			var api = this.api();
            var rows = api.rows( {page:'current'} ).nodes();
            var last=null;
 
            api.column(1, {page:'current'} ).data().each( function ( group, i ) {
                if ( last !== group ) {
                    $(rows).eq( i ).before(
                        '<tr class="group table-group-header theme-bg-' + group + '"><td></td><td></td><td colspan="3">'+roleMap.get(group)+'</td></tr>'
                    );
                    last = group;
                }
            });
            
        }
	});
	
}

function selectModule(m) {
	
	bootbox.confirm("Changin your module will empty your players table. To undo refresh this page or press cancel.", function(result) {
		
		if (result) {
		
			$('#lineup-dropdown').text(m);
			$('#save-team').attr('disabled', 'disabled');

			// reset selections
			goalkeepers = [];
			defenders = [];
			midfielders = [];
			forwards = [];

			bench = [];

			clearTable();
			redrawTeam();

			// assign numbers for role
			moduleG = 1;
			moduleD = parseInt(m.toString().charAt(0));
			moduleM = parseInt(m.toString().charAt(1));
			moduleF = parseInt(m.toString().charAt(2));

			totalPlayers = moduleG + moduleD + moduleM + moduleF;
			
		}
		
	});
	
}

function setClickContainer() {

	$('#table-team-lineup tbody').on('click', 'tr', function() {

		var player = gTable.row(this).data();

		var jsonData = {};
		jsonData['id'] 			= player[0];
		jsonData['role'] 		= player[1];
		jsonData['name'] 		= player[2];
		jsonData['equipe'] 		= player[3];
		jsonData['initQuote'] 	= player[4];

		if (jsonData.role == 'g') {
			
			if (moduleG != null && goalkeepers.length < moduleG) {
				
				gTable.row(this).remove().draw(false);
				goalkeepers.push(jsonData);
				redrawOrder();
				
			} else if (moduleG != null && goalkeepers.length >= moduleG) {

				gTable.row(this).remove().draw(false);
				bench.push(jsonData);
				redrawBench();
				
			} else {
				bootbox.alert("Please select a module first");
			}
			
			if( moduleG != null && totalPlayers == starterTable.rows().count() ){
				$('#save-team').removeAttr('disabled');
			}
			
		} else if (jsonData.role == 'd') {
			
			if (moduleD != null && defenders.length < moduleD) {

				gTable.row(this).remove().draw(false);
				defenders.push(jsonData);
				redrawOrder();
				
			} else if (moduleD != null && defenders.length >= moduleD) {

				gTable.row(this).remove().draw(false);
				bench.push(jsonData);
				redrawBench();
				
			} else {
				bootbox.alert("Please select a module first");
			}

			if( moduleD != null && totalPlayers == starterTable.rows().count() ){
				$('#save-team').removeAttr('disabled');
			}
			
		} else if (jsonData.role == 'm') {
			
			if (moduleM != null && midfielders.length < moduleM) {

				gTable.row(this).remove().draw(false);
				midfielders.push(jsonData);
				redrawOrder();
				
			} else if (moduleM != null && midfielders.length >= moduleM) {

				gTable.row(this).remove().draw(false);
				bench.push(jsonData);
				redrawBench();
				
			} else {
				bootbox.alert("Please select a module first");
			}

			if( moduleM != null && totalPlayers == starterTable.rows().count() ){
				$('#save-team').removeAttr('disabled');
			}
			
		} else if (jsonData.role == 'f') {
			
			if (moduleF != null && forwards.length < moduleF) {

				gTable.row(this).remove().draw(false);
				forwards.push(jsonData);
				redrawOrder();

			} else if (moduleF != null && forwards.length >= moduleF) {

				gTable.row(this).remove().draw(false);
				bench.push(jsonData);
				redrawBench();
				
			} else {
				bootbox.alert("Please select a module first");
			}
			
			if( moduleF != null && totalPlayers == starterTable.rows().count() ){
				$('#save-team').removeAttr('disabled');
			}
			
		}
		
	});
	
}

function redrawTeam() {
	
	gTable.clear();

	$.each(tg.filter(comparer(goalkeepers)).filter(comparer(bench)), function(index, player) {
		gTable.row.add([player.id, player.role, player.name, player.equipe, player.initQuote]);
	});
	$.each(td.filter(comparer(defenders)).filter(comparer(bench)), function(index, player) {
		gTable.row.add([player.id, player.role, player.name, player.equipe, player.initQuote]);
	});
	$.each(tm.filter(comparer(midfielders)).filter(comparer(bench)), function(index, player) {
		gTable.row.add([player.id, player.role, player.name, player.equipe, player.initQuote]);
	});
	$.each(tf.filter(comparer(forwards)).filter(comparer(bench)), function(index, player) {
		gTable.row.add([player.id, player.role, player.name, player.equipe, player.initQuote]);
	});

	gTable.draw();
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}

}

function redrawOrder() {
	
	starterTable.clear();
	
	$.each(goalkeepers, function(index, player) {
		starterTable.row.add([player.id, player.role, player.name, player.equipe, player.initQuote]);
	});
	$.each(defenders, function(index, player) {
		starterTable.row.add([player.id, player.role, player.name, player.equipe, player.initQuote]);
	});
	$.each(midfielders, function(index, player) {
		starterTable.row.add([player.id, player.role, player.name, player.equipe, player.initQuote]);
	});
	$.each(forwards, function(index, player) {
		starterTable.row.add([player.id, player.role, player.name, player.equipe, player.initQuote]);
	});

	starterTable.draw();
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}
	
}

function redrawBench() {
	
	benchTable.clear();
	
	bench.sort(sortByRole);

	$.each(bench, function(index, player) {
		benchTable.row.add([player.id, player.role, player.name, player.equipe, player.initQuote]);
	});
	
	benchTable.draw();
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}
	
}

function setClickStarters() {

	$('#table-starters tbody').on('click', 'tr', function() {
		$('#save-team').attr('disabled', 'disabled');
		var player = starterTable.row(this).data();
		
		var jsonData = {};
		jsonData['id'] 			= player[0];
		jsonData['role'] 		= player[1];
		jsonData['name'] 		= player[2];
		jsonData['equipe'] 		= player[3];
		jsonData['initQuote'] 	= player[4];

		if ( player[1] == 'g' ) {

			gTable.row.add([player[0], player[1], player[2], player[3], player[4]]).draw();
			starterTable.row(this).remove().draw(false);
			goalkeepers.splice(goalkeepers.indexOfId(jsonData), 1);

		} else if ( player[1] == 'd' ){

			gTable.row.add([player[0], player[1], player[2], player[3], player[4]]).draw();
			starterTable.row(this).remove().draw(false);
			defenders.splice(defenders.indexOfId(jsonData), 1);

		} else if ( player[1] == 'm' ){

			gTable.row.add([player[0], player[1], player[2], player[3], player[4]]).draw();
			starterTable.row(this).remove().draw(false);
			midfielders.splice(midfielders.indexOfId(jsonData), 1);

		} else if ( player[1] == 'f' ){

			gTable.row.add([player[0], player[1], player[2], player[3], player[4]]).draw();
			starterTable.row(this).remove().draw(false);
			forwards.splice(forwards.indexOfId(jsonData), 1);

		}
		
		redrawTeam();

	});

}

function setClickBench(){

	$('#table-bench tbody').on('click', 'tr', function() {

		if( goalkeepers.length == moduleG && defenders.length == moduleD && midfielders.length == moduleM && forwards.length == moduleF ){
			$('#save-team').removeAttr('disabled');
		}

		var player = benchTable.row(this).data();
		
		var jsonData = {};
		jsonData['id'] 			= player[0];
		jsonData['role'] 		= player[1];
		jsonData['name'] 		= player[2];
		jsonData['equipe'] 		= player[3];
		jsonData['initQuote'] 	= player[4];
		
		if( player[1] == 'g' ){
			
			gTable.row.add([player[0], player[1], player[2], player[3], player[4]]).draw();
			benchTable.row(this).remove().draw(false);
			bench.splice(bench.indexOfId(jsonData), 1);
		}
		else if( player[1] == 'd' ){
			
			gTable.row.add([player[0], player[1], player[2], player[3], player[4]]).draw();
			benchTable.row(this).remove().draw(false);
			bench.splice(bench.indexOfId(jsonData), 1);

		}
		else if( player[1] == 'm' ){
			
			gTable.row.add([player[0], player[1], player[2], player[3], player[4]]).draw();
			benchTable.row(this).remove().draw(false);
			bench.splice(bench.indexOfId(jsonData), 1);

		}
		else if( player[1] == 'f' ){
			
			gTable.row.add([player[0], player[1], player[2], player[3], player[4]]).draw();
			benchTable.row(this).remove().draw(false);
			bench.splice(bench.indexOfId(jsonData), 1);

		}

		redrawTeam();

	});
}

function sortByRole(a,b) {
	
	var map = new Map();
	map.set("g", 0);
	map.set("d", 1);
	map.set("m", 2);
	map.set("f", 3);
	
	if (a.role == b.role)
		return 0;
	
	if (map.get(a.role) < map.get(b.role)) {
		return -1;
	}
	
	return 1;
	
}

function showHelp() {
	
	bootbox.alert({ 
		message: 
			'Here you can line up your team.<br><br>'+
			'On the the left, you got two section, starters and bench:<br>' + 
			'If you\'ve already lined-up your team for the next match-day, here you will see it.<br>' + 
			'You can remove players from here by clicking them, then you can add other players from the table on the right,'+
			'which contains your whole team minus starters and bench.<br>'+
			'Remember to click on the SAVE button after you\'ve done.'
	});
	
}
