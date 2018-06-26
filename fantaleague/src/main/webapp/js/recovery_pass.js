$(document).ready(function() {
	
	if ($(document).height() > $(window).height()) {
	    $('#footer-container').removeClass('fixed-bottom');
	}

	var url = new URL(window.location.href);

	$.validate({
		form : '#form-recovery',
		modules : 'security'
	});

	$('#form-recovery').attr('action', url);

});