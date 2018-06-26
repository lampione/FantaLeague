$(document).ready(function() {
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}

	$.validate({
	  form : '#login',
	});

});