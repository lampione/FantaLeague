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

	var ctx = document.getElementById('vote-chart').getContext('2d');
	
	var day = [];
	var votes = [];
	var finalVotes = [];
	
	var reds = 0;
	var yellows = 0;
	var dones = 0;
	var assists = 0;
	var taken = 0;
	
	var finalVote = 0;
	var vote = 0;
	
	var count = 0;
	
	var role = 'null';
	
	var name = '';
	
	$.getJSON("player-stats?s=" + $('#pid').val(), function(response) {
		
		$.each(response, function(index, item){
			
			name = item.name;
			
			role = item.role;
			
			day.push(item.dayId);
			votes.push(item.vote);
			finalVotes.push(item.finalVote);

			reds += parseInt(item.redCard);
			yellows += parseInt(item.yellowCard);
			dones += parseInt(item.done);
			assists += parseInt(item.assist);
			taken += parseInt(item.taken);
			finalVote += parseFloat(item.finalVote);
			vote += parseFloat(item.vote);

			count++;

		});
		
		$('#player-name').text('Statistics for ' + name);
		
		$('#yellow-text').text(yellows);
		$('#yellow').show();
		
		$('#red-text').text(reds);
		$('#red').show();
		
		var avgVote = (vote/count);
		var avgFantaVote = (finalVote/count);
		
		$('#vote-avg-text').text(avgVote.toFixed(2));
		$('#fantavote-avg-text').text(avgFantaVote.toFixed(2));
		
		if (role == 'g') {
			$('#taken-text').text(taken);
			$('#taken').show();
		} else {
			
			$('#goal-text').text(dones); 
			$('#goal').show();
			
			$('#assist-text').text(assists);
			$('#assist').show();
		}
		
		var chart = new Chart(ctx, {
			
		    // The type of chart we want to create
		    type: 'line',

		    // The data for our dataset
		    data: {
		        labels: day,
		        datasets: [{
		            label: "Vote",
		            backgroundColor: 'rgba(0, 0, 0, 0)',
		            borderColor: 'rgb(255, 0, 0)',
		            data: votes,
		            fill: false,
		            pointBackgroundColor: 'rgb(255, 0, 0)',
		            pointHoverRadius: 6
		        }, 
		        {
		        	label: "FantaVote",
		            backgroundColor: 'rgba(0, 0, 0, 0)',
		            borderColor: 'rgb(0, 0, 255)',
		            data: finalVotes,
		            fill: false,
		            pointBackgroundColor: 'rgb(0, 0, 255)',
		            pointHoverRadius: 6
		            
		        }]
		    },

		    // Configuration options go here
		    options: {
		    	responsive: true,
                tooltips: {
                    mode: 'index'
                },
                scales: {
                    xAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Match day'
                        }
                    }],
                    yAxes: [{
                        display: true,
                        scaleLabel: {
                            display: true,
                            labelString: 'Vote'
                        },
                        ticks: {
                            suggestedMin: 0,
                            suggestedMax: 20,
                        }
                    }]
                }
		    }
		});
		
	});
	
});
