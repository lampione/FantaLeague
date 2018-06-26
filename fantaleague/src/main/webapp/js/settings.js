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

	firstName = $('#first-name-input').val();
	lastName = $('#last-name-input').val();
	born = $('#born-input').val();

	$('#password').click(function() {
		initSettings();

		$('#password-input').removeAttr('readonly');
		$(this).hide();
		$('#ar-password').show();
	});

	$('#first-name').click(function() {
		initSettings();

		$('#first-name-input').removeAttr('readonly');
		$(this).hide();
		$('#ar-first-name').show();
	});

	$('#last-name').click(function() {
		initSettings();

		$('#last-name-input').removeAttr('readonly');
		$(this).hide();
		$('#ar-last-name').show();		
	});

	$('#born').click(function() {
		initSettings();

		$('#born-input').removeAttr('readonly');

		$("#born-input").datepicker({
			changeYear: true,
			yearRange: "-118:+0"
		});

		$(this).hide();
		$('#ar-born').show();
	});
	
});

function initSettings(){
	$('#first-name-input').attr('readonly', 'readonly');
	$('#last-name-input').attr('readonly', 'readonly');
	$('#born-input').attr('readonly', 'readonly');
	$('#password-input').attr('readonly', 'readonly');
	
	$('#ar-first-name').hide();
	$('#ar-last-name').hide();
	$('#ar-born').hide();
	$('#ar-password').hide();
	
	$('#first-name').show();
	$('#last-name').show();
	$('#born').show();
	$('#password').show();

	$('#first-name-input').val(firstName);
	$('#last-name-input').val(lastName);
	$('#born-input').val(born);
	$('#password-input').val('');
}

function update(what){
	if( what == 'p' ){
		var pass = $('#password-input').val();
		
		if (pass != null && pass.length == 0) {
			initSettings();
			
		} else {
			
			$.post("settings",
			        {
			          p: pass
			        },
			        function(data,status){
			        	$('#password-input').val('');
			        	initSettings();
			});
			
		}

	}else if(what == 'f'){
		var first = $('#first-name-input').val();

		$.post("settings",
		        {
		          f: first
		        },
		        function(data,status){
		        	 $('#first-name-input').val(data);
		        	 firstName = data;
		        	 initSettings();
		});
		
	}else if(what == 'l'){
		var last = $('#last-name-input').val();

		$.post("settings",
		        {
		          l: last
		        },
		        function(data,status){
		        	$('#last-name-input').val(data);
		        	 lastName = data;
		        	 initSettings();
		});
	}else if(what == 'b') {
				
		var bornN = $('#born-input').val();
		
		if (bornN == born) {
			initSettings();
		} else {
			
			$.post("settings", {
				b : bornN
			}, function(data, status) {
				$('#born-input').val(data);
				born = data;
				initSettings();
			});

		}


	}
	
}


