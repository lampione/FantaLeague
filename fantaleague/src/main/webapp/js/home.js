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
	
	$('#search-player').hide();

	$(".table-row").click(function() {
		var url = "league?id=" + $(this).data("href") + "&s=" + $(this).data("status");
        window.location = url;
    });

	$(".btn-invite-accept").click(function() {

		var lid = $(this).data("href");

		bootbox.prompt({
			title: 'Please enter your team name',
			callback: function(person){
				if (person === null){
			        // dismissed
			    } else {
			        // OK
			    	var url= "handle-invite?selection=accept&team-name=" + person + "&id=" + lid;
			    	window.location = url;	    	
			    }
			}
		});

	});

	$(".btn-invite-refuse").click(function() {
		var url= "handle-invite?selection=refuse&tid=" + $(this).data("href");
		window.location = url;
	});

	// date format: MM DD YYYY hh:mm:ss
	var nextMatch = $('#next-match').val();	
	
	nextMatch = nextMatch.replace(/@/g, " ");

	var countDownDate = new Date(nextMatch).getTime();
	var x = setInterval(function() {

				var now = new Date().getTime();
				var difference = countDownDate - now;

				if (difference < 0) {
					clearInterval(x);
					$("#count-down").hide();
					$("#count-down-message").text("Day in progress");
					$("#count-down-message").show();
					// TODO handle this case ^^
				} else {
					
					$("#count-down").show();
					$("#count-down-message").hide();
					
					var days = Math.floor(difference / (1000 * 60 * 60 * 24));
					var hours = Math.floor((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
					var minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
					var seconds = Math.floor((difference % (1000 * 60)) / 1000);

					var stringDay = days.toString();
					if (stringDay.length == 1) {
						stringDay = "0" + stringDay;
					}
					$('#days-group').text(stringDay);

					var stringHour = hours.toString();
					if (stringHour.length == 1) {
						stringHour = "0" + stringHour;
					}
					$('#hours-group').text(stringHour);

					var stringMinutes = minutes.toString();
					if (stringMinutes.length == 1) {
						stringMinutes = "0" + stringMinutes;
					}
					$('#minutes-group').text(stringMinutes);

					var stringSecond = seconds.toString();
					if (stringSecond.length == 1) {
						stringSecond = "0" + stringSecond;
					}
					$('#seconds-group').text(stringSecond);

				}
				
			}, 1000);

});

function setRss(selected) {
    
    switch(selected) {
            
        case 0:
            $('#tab-btn0').addClass('active');
            $('#rss-content0').show();
            
            $('#tab-btn1').removeClass('active');
            $('#rss-content1').hide();
            break;

        case 1:
            $('#tab-btn0').removeClass('active');
            $('#rss-content0').hide();
            
            $('#tab-btn1').addClass('active');
            $('#rss-content1').show();
            break;
    }

}

var mMapId;
var mLocation;

function fetchLocation(mapId, location) {
	
	mMapId = mapId;
	mLocation = location;
	
	if (navigator.geolocation) {
        var pos = navigator.geolocation.getCurrentPosition(showMap);
    }
	
}

function showMap(position) {

	$('#collapse-' + mMapId).collapse('toggle');
	
	var origin = position.coords.latitude + "," + position.coords.longitude;
	var destination = mLocation.replace(/ /g, "+");

	var url = "https://www.google.com/maps/embed/v1/directions?key=[API_KEY]&origin=" + origin + "&destination=" + destination;
	
	$('#map-' + mMapId).prop('src', url);
	
}
