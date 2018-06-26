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
		
		$('#inviteEmail').tagsinput({maxChars: 34});
		
	});
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}

	teamName = $('#team-name-input').val();
	leagueName = $('#league-name-input').val();

	$('#team-name').click(function() {
		initUserDash();

		$('#team-name-input').removeAttr('readonly');
		$(this).hide();
		$('#ar-team-name').show();
	});

	$('#league-name').click(function() {
		initLeague();

		$('#league-name-input').removeAttr('readonly');
		$(this).hide();
		$('#ar-league-name').show();
	});


	teamMap = new Map();

	var status = $('#status-user').val();

	if (status == 'A') {
		$('#admin-dashboard').show();
	}

});

function initLeague(){
	$('#league-name-input').attr('readonly', 'readonly');
	$('#ar-league-name').hide();
	$('#league-name').show();
	$('#league-name-input').val(leagueName);
}

function updateLName(leagueId){
	var lName = $('#league-name-input').val();

	$.post("league",
	        {
	          lN: lName,
	          lId: leagueId
	        },
	        function(data,status){
	        	$('#league-name-input').val(data);
	        	leagueName = data;
	        	$('#rules').text('\'' + data + '\' league rules')
	        	$('title').text("League " + data);
	        	initLeague();
	});
}

function initUserDash(){
	$('#team-name-input').attr('readonly', 'readonly');
	$('#ar-team-name').hide();
	$('#team-name').show();
	$('#team-name-input').val(teamName);
}

function updateTeam(teamId){
	var tName = $('#team-name-input').val();

	$.post("league",
	        {
	          tN: tName,
	          tId: teamId
	        },
	        function(data,status){
	        	$('#team-name-input').val(data);
	        	$('#leaderboard-'+teamId).text(data);
	        	$('#team-id-' + teamId).text(data);
	        	teamName = data;
	        	initUserDash();
	});
}

function selectDay(day, element) {
	
	$('#list-days li a').each(function(index, item) {
		$(item).removeClass("active");
	});
	
	$(element).addClass('active');
	
}

function callServlet() {

	var urlParams = new URLSearchParams(window.location.search);
	var idUrl = urlParams.get('id');

	$('#form-invite').attr('action', 'invite-user?id=' + idUrl);
}

function deleteLeague(lName, leagueId) {

	bootbox.confirm("Are you sure you want to delete '" + lName + "' league?", function(result) {
		if (result == true) {
			window.location = "delete-league?id=" + leagueId;
		}
	});

}