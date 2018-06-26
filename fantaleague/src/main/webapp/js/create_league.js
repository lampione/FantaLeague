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
		
		$('#modules').tagsinput({maxChars: 3});

	});
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}
	
});

$(function() {

	$.validate({
	  form : '#registrationForm',
	});

});

function validate() {
	
	var goalkeepers = parseInt(document.getElementById("num_goalkeepers").value);
	var defenders = parseInt(document.getElementById("num_defenders").value);
	var midfielders = parseInt(document.getElementById("num_midfielders").value);
	var forwards = parseInt(document.getElementById("num_forwards").value);
	
	var sum = goalkeepers + defenders + midfielders + forwards;
	
	if (sum > 32) {
		bootbox.alert("Too much players!");
		return false;
	}
	
	var canCreate = true;

	var modules = $('#modules').val().split(",");
	
	$.each(modules, function(index, item) {
		var moduleSum = 0;
		
		for (var j=0; j < 3; j++) {
			moduleSum += parseInt(item.charAt(j));
		}
		
		if (moduleSum != 10) {
			bootbox.alert("The following module '" + item + "' is not valid");
			canCreate=false;
			return false;
		}
	});
	
	return canCreate;
}

function lineUpHelp() {
	bootbox.alert("If the first match of the day is scheduled to start at 15:00 and you set a value of 15, then every Manager in your league should line-up their formation before 14:45.<br><br>Otherwise, the last valid line-up will be considered.");
}

function duplicateHelp() {
	bootbox.alert("Allows players to be 'owned' by more than one Manager");
	return false;
}
