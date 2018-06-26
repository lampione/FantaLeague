Array.prototype.diff = function(a) {
	return this.filter(function(i) {
		return a.indexOf(i) < 0;
	});
};

function comparer(otherArray) {
	return function(current) {
		return otherArray.filter(function(other) {
			return other.id == current.id
		}).length == 0;
	}
};

Array.prototype.indexOfId = function(f) {
    for (var i = 0; i < this.length; ++i) {
        if ( f.id == this[i].id )
            return i;
    	}
    return -1;
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
	
	// Map role to tempArrays
	map = new Map();
	map.set("g", "tempGoalkeepers");
	map.set("d", "tempDefenders");
	map.set("m", "tempMidfielders");
	map.set("f", "tempForwards");
	
	// Map role to full role name
	roleMap = new Map();
	roleMap.set("g", "Goalkeepers");
	roleMap.set("d", "Defenders");
	roleMap.set("m", "Midfielders");
	roleMap.set("f", "Forwards");

	// Credits count
	creditsCurrent = $('#hidden-league-credits').val();
	creditsTotal = $('#hidden-league-credits').val();
	
	// arrays to store team players, either selected or retrieved by existing team
	tempGoalkeepers = [];
	tempDefenders = [];
	tempMidfielders = [];
	tempForwards = [];

	// arrays to store ALL players from database
	goalkeepers = [];
	defenders = [];
	midfielders = [];
	forwards = [];
	
	// While removing player, store it here
	tempReplacePlayer = [];
	
	// to get the removing player row
	oldContext = null;
	
	// currently selected team
	selectedTeamId = null;
	selectedTeamName = null;

	var league_g_count = parseInt($('#league-g-count').val());
	var league_d_count = parseInt($('#league-d-count').val());
	var league_m_count = parseInt($('#league-m-count').val());
	var league_f_count = parseInt($('#league-f-count').val());

	leaguePlayerCount = league_g_count + league_d_count + league_m_count + league_f_count;

	console.log("Required Players: " + leaguePlayerCount);

	pTable = $('#table-search').DataTable({
		paging: false,
		bInfo: false
	});

	/* FILTER! */
	$('#table-player-filter').keyup(function(){
		pTable.search($(this).val()).draw();
	});

	gTable = $('#table-team-g').DataTable({
		searching: false,
		paging: false,
		ordering: false,
		bInfo: false,
		drawCallback: function (settings) {

			var api = this.api();
            var rows = api.rows( {page:'current'} ).nodes();
            var last=null;

            api.column(1, {page:'current'}).data().each( function (group, i) {

            	console.log(group);

                if ( last !== group ) {
                    $(rows).eq( i ).before(
                        '<tr class="group table-group-header theme-bg-' + group + '"><td></td><td></td><td colspan="3">'+roleMap.get(group)+'</td></tr>'
                    );
                    last = group;
                }
            });

        }
	});

});

function setTeamTableClick(which, table, player) {

	addCredits(player[4]);

	var jsonData = {};
	jsonData['id'] = player[0];
	jsonData['role'] = player[1];
	jsonData['name'] = player[2];
	jsonData['equipe'] = player[3];
	jsonData['initQuote'] = player[4];
	
	$('#save-team').attr('disabled', 'disabled');
	pTable.clear();
	
	if (player[1] == 'g') {
		tempGoalkeepers.splice(tempGoalkeepers.indexOfId(jsonData), 1);		
	} else if (player[1] == 'd') {
		tempDefenders.splice(tempDefenders.indexOfId(jsonData), 1);		
	} else if (player[1] == 'm') {
		tempMidfielders.splice(tempMidfielders.indexOfId(jsonData), 1);		
	} else if (player[1] == 'f') {
		tempForwards.splice(tempForwards.indexOfId(jsonData), 1);		
	}
	
	//data.splice(data.indexOfId(jsonData), 1);
	table.row(which).remove().draw(false);
	selectPlayerByRole(jsonData.role);
	$('body').scrollTop(0);

}

function setRowClick(data, player, context) {
	
	if (data.length < $('#league-' + player.role + '-count').val()) {

		bootbox.prompt({
			title: 'Amount of credits for ' + player.name,
			value: player.initQuote,
			inputType: 'number',
			callback: function(credits) {
				
				if (credits === null) {
			        // Prompt dismissed
			    } else {

			    	if (creditsCurrent - credits >= 0){
			    		
			    		if (credits.length >= 1) {
			    			
			    			removeCredits(credits);
			    			
			    			const temp = Object.assign({}, player);
			    			
			    			temp.initQuote = credits;
			    			data.push(temp);
			    			redrawTableTeam();
			    			pTable.row(context).remove().draw(false);
			    			
			    			var tgoalkeepers = parseInt(tempGoalkeepers.length);
			    			var tdefenders = parseInt(tempDefenders.length);
			    			var tmidfielders = parseInt(tempMidfielders.length);
			    			var tforwards = parseInt(tempForwards.length);
			    			
			    			var tsum = tgoalkeepers + tdefenders + tmidfielders + tforwards;
			    			
			    			if (tsum == leaguePlayerCount) {
			    				$('#save-team').removeAttr('disabled');
			    			}

			    		} else {
			    			
			    			bootbox.alert("Invalid input!");
			    			
			    		}

					} else {
						bootbox.alert("You don't have enough credits");
					}
			    	$('body').scrollTop(0);
			    }

				/**/
			}
		});
		
	} else {
		bootbox.alert("Max players for this role");
		$('body').scrollTop(0);
	}
	
}

function selectPlayerByRole(role) {

	$("#table-player-filter").val('');
	var lduplicate = $('#league-duplicate').val();
	var lid = $('#league-id').val();

	if (role == 'g') {

		if (goalkeepers.length == 0) {
			
			$.getJSON("find-players?role=" + role +"&lduplicate=" + lduplicate + "&lid=" + lid + "&tid=" + selectedTeamId, function(response) {
				goalkeepers = response.slice();
				fillTable(goalkeepers, tempGoalkeepers, role);
			});
			
		} else {
			fillTable(goalkeepers, tempGoalkeepers, role);
		}

	} else if (role == 'd') {
		
		if (defenders.length == 0) {
			$.getJSON("find-players?role=" + role +"&lduplicate=" + lduplicate + "&lid=" + lid + "&tid=" + selectedTeamId, function(response) {
				defenders = response.slice();
				fillTable(defenders, tempDefenders, role);
			});
		} else {
			fillTable(defenders, tempDefenders, role);
		}

	} else if (role == 'm') {

		if (midfielders.length == 0) {
			$.getJSON("find-players?role=" + role +"&lduplicate=" + lduplicate + "&lid=" + lid + "&tid=" + selectedTeamId, function(response) {
				midfielders = response.slice();
				fillTable(midfielders, tempMidfielders, role);
			});
		} else {
			fillTable(midfielders, tempMidfielders, role);
		}

	} else if (role == 'f') {

		if (forwards.length == 0) {
			$.getJSON("find-players?role=" + role +"&lduplicate=" + lduplicate + "&lid=" + lid + "&tid=" + selectedTeamId, function(response) {
				forwards = response.slice();
				fillTable(forwards, tempForwards, role);
			});
		} else {
			fillTable(forwards, tempForwards, role);
		}

	}

};

function fillTable(data, dataDiff, role) {
	
	$('#table-player-filter').show();

	pTable.clear();
	$.each(data.filter(comparer(dataDiff)), function(index, item) {
		pTable.row.add([item.id, item.role, item.name, item.equipe, item.initQuote]);
	});
	pTable.draw();

	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}

}

function addCredits(diff) {
	creditsCurrent += parseInt(diff);
	$('#league-credits').text(creditsCurrent + "/" + creditsTotal);
}

function removeCredits(diff) {
	creditsCurrent -= parseInt(diff);
	$('#league-credits').text(creditsCurrent + "/" + creditsTotal);
}

function redrawTableTeam() {

	gTable.clear();
	$.each(tempGoalkeepers, function(index, item) {
		gTable.row.add([item.id, item.role, item.name, item.equipe, item.initQuote]);
	});
	$.each(tempDefenders, function(index, item) {
		gTable.row.add([item.id, item.role, item.name, item.equipe, item.initQuote]);
	});
	$.each(tempMidfielders, function(index, item) {
		gTable.row.add([item.id, item.role, item.name, item.equipe, item.initQuote]);
	});
	$.each(tempForwards, function(index, item) {
		gTable.row.add([item.id, item.role, item.name, item.equipe, item.initQuote]);
	});
	gTable.draw();
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}

}

function fetchTeam(teamId, teamName) {

	selectedTeamId = teamId;
	selectedTeamName = teamName;
	$('#save-team-teamid').val(teamId);
	$('#team-selector').text(teamName);

	gTable.clear();
	
	tempGoalkeepers = [];
	tempDefenders = [];
	tempMidfielders = [];
	tempForwards = [];

	$.getJSON("find-players?tid=" + teamId, function(response) {

		// this removes listeners from table
		$('#table-search tbody').off();

		// same as before
		$('#table-team-g').off();

		disableAll();

		if (response == null || response.length == 0) {
			
			$('.offset-l').hide();
			$('.offset-r').hide();

			$('#table-player-container').show();
			$('#table-team-container').show();		
			$('#save-team').show();

			/* SEARCH TABLE CLICK */
			$('#table-search tbody').on('click', 'tr', function() {

				player = pTable.row(this).data();
				
				var jsonData = {};
				jsonData['id'] 			= player[0];
				jsonData['role'] 		= player[1];
				jsonData['name'] 		= player[2];
				jsonData['equipe'] 		= player[3];
				jsonData['initQuote'] 	= player[4];

				switch(jsonData.role) {
				case 'g':
					setRowClick(tempGoalkeepers, jsonData, this);
					break;
				case 'd':
					setRowClick(tempDefenders, jsonData, this);
					break;
				case 'm':
					setRowClick(tempMidfielders, jsonData, this);
					break;
				case 'f':
					setRowClick(tempForwards, jsonData, this);
					break;
				}

			});

			/* ALL ROLE TABLES CLICK */
			$('#table-team-g').on('click', 'tr', function() {
				var player = gTable.row(this).data();
				setTeamTableClick(this, gTable, player);
			});

			$('#g-tab').removeClass('disabled');
			$('#g-tab').click(function() {selectPlayerByRole('g')});
			
			$('#d-tab').removeClass('disabled');
			$('#d-tab').click(function() {selectPlayerByRole('d')});
				
			$('#m-tab').removeClass('disabled');
			$('#m-tab').click(function() {selectPlayerByRole('m')});

			$('#f-tab').removeClass('disabled');
			$('#f-tab').click(function() {selectPlayerByRole('f')});
			
			$('#g-tab').addClass('active');
			selectPlayerByRole('g');
			
			if ($(document).height() > $(window).height()) {
			    $('#footer-container').removeClass('fixed-bottom');
			}
			
		} else {
			
			$('.offset-l').show();
			$('.offset-r').show();
			
			$('#table-player-container').hide();
			$('#table-team-container').show();			
			$('#save-team').hide();

			$('#table-search tbody').on('click', 'tr', function() {

				var player = pTable.row(this).data();
				replacePlayer(player, this, gTable);

			});

			/* ALL ROLE TABLES CLICK */
			$('#table-team-g').on('click', 'tr', function() {
				var player = gTable.row(this).data();
				oldContext = this;
				choosePlayer('g', player);
			});

			if ($(document).height() > $(window).height()) {
			    $('#footer-container').removeClass('fixed-bottom');
			}
			
		}

		var sumCredit = 0;
		$.each(response, function(index, item) {

			if (item.role == 'g') {
				tempGoalkeepers.push(item);
			} else if (item.role == 'd') {
				tempDefenders.push(item);
			} else if (item.role == 'm') {
				tempMidfielders.push(item);
			} else if (item.role == 'f') {
				tempForwards.push(item);
			}
			sumCredit += item.initQuote;
		});

		creditsCurrent = creditsTotal - sumCredit;
		$('#league-credits').text(creditsCurrent + "/" + creditsTotal);

		redrawTableTeam();

	});

	
}

function disableAll() {
	$('#g-tab').addClass('disabled');
	$('#d-tab').addClass('disabled');
	$('#m-tab').addClass('disabled');
	$('#f-tab').addClass('disabled');
	
	$('#g-tab').removeClass('active');
	$('#d-tab').removeClass('active');
	$('#m-tab').removeClass('active');
	$('#f-tab').removeClass('active');
}

function choosePlayer(role, player) {
	
	bootbox.confirm("Are you sure you want to replace " + player[2] + "?", function(response) {
		
		if (response === true) {
			
			addCredits(parseInt(player[4]));
			tempReplacePlayer = player;
			$('#table-player-container').show();
			$('#table-team-container').hide();
			
			// disable all tabs
			disableAll();
			
			// remove disabled from selected tab and set it active
			$('#' + player[1] + '-tab').removeClass('disabled');			
			$('#' + player[1] + '-tab').addClass('active');
			
			selectPlayerByRole(player[1]);
			
			$('body').scrollTop(0);
			
		}
	});

}

function replacePlayer(player, context, table) {
	
	bootbox.prompt({
		title: "Amount of credits for " + player[2] + "?",
		value: player[4],
		inputType: 'number',
		callback: function(credits) {
			
			if (credits === null) {
				
			} else {
				
				if (creditsCurrent - credits >= 0) {

					var leagueId = $('#save-team-league-id').val();
					var url = "update-player?oid=" + tempReplacePlayer[0] + "&nid=" + player[0] + "&tid=" + selectedTeamId + "&c=" + credits;

					$.get(url, function(response) {

						if (response == "200") {

							removeCredits(credits);
							fetchTeam(selectedTeamId,selectedTeamName);

						} else {}
						
					});
				}
				
			}
			
		}
	});
	
}

function setValues() {
	
	$('#g').val(JSON.stringify(tempGoalkeepers));
	$('#d').val(JSON.stringify(tempDefenders));
	$('#m').val(JSON.stringify(tempMidfielders));
	$('#f').val(JSON.stringify(tempForwards));

	return true;
	
}

function showHelp() {
	
	bootbox.alert({ 
		message: 
			'Here you can create teams for you and other trainers of your league.<br>' + 
			'<br>First, select the team you want to edit:<br>if that team is not already created, ' + 
			'a list of players will appear and the "role" buttons will unlock. ' + 
			'You can select a role and pick players from that role.<br>Once you\'ve done, '+
			'remember to click on the SAVE button.<br><br>Instead, if the team has already been created, '+
			'then you will see the table with the complete team, there you can change one player at a time '+
			'by selecting it and picking his substitute from the new table.<br>The change will be saved automatically.'
	});
	
}