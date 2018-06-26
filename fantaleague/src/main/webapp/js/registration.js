$(document).ready(function() {
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}

	$.validate({
		  form : '#registrationForm',
	});

	$("#inputBorn").datepicker({
		changeYear: true,
		yearRange: "-118:+0"
	});

});

function validate() {
	// default jquery format: MM/DD/YYYY	
	var date = $('#inputBorn').val();

	var split = date.split("/");
	var today = new Date();
	var input = new Date(split[2], split[0]-1, split[1]);
	
	if (today >= input) {
		return true;
	}

	$('#pop-up').show("slow");
	return false;
}
