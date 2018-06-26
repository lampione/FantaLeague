$(document).ready(function() {
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}

	var url = new URL(window.location.href);
	$('#form-recovery-invite').attr('action', url);

});
