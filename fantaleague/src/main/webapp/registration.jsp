<%@ page session="true" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Registration</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>
<!-- Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Questi 3 sono relativi a JQUERY datepicker -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">

<!-- Validator jquery -->
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.3.26/jquery.form-validator.min.js"></script>

<link rel="stylesheet" type="text/css" href="css/theme.css">
<script src="js/registration.js" type="text/javascript"></script>

</head>

<body class="img-background">

	<div class="container-fluid container-default">

		<div class="row justify-content-center align-items-center">
		
			<div class="form-box">

				<c:if test="${not empty error}">
					<p class="error-tag">${error.message}</p>
				</c:if>

				<h3>
					Create a new account, <a href="login">Have one?</a>
				</h3>

				<form id="registrationForm" method="post" action="registration" onsubmit="return validate()" role="form">
					<div class="form-group">
						<label for="inputFirstName">First Name</label> <input type="text" name="first_name" class="form-control" id="inputFirstName"
							data-validation="length" data-validation-length="min1" data-validation-error-msg="This field is required" title="Fill this field"
							maxlength="255">
					</div>
					<div class="form-group">
						<label for="inputLastName">Last Name</label> <input type="text" name="last_name" class="form-control" id="inputLastName"
							data-validation="length" data-validation-length="min1" data-validation-error-msg="This field is required" title="Fill this field"
							maxlength="255">
					</div>
					<div class="form-group">
						<label for="inputBorn">Date of birth</label> <input readonly type="text" name="born" class="form-control" id="inputBorn"
							data-validation="length" data-validation-length="min1" data-validation-error-msg="This field is required" title="Fill this field">
					</div>
					<div class="form-group">
						<label for="inputEmail">Email</label> <input type="text" name="email" class="form-control" id="inputEmail" data-validation="email"
							data-validation-error-msg="Email not valid" title="Fill this field" maxlength="255">
					</div>
					<div class="form-group">
						<label for="inputPassword">Password</label> <input type="password" name="password" class="form-control" id="inputPassword"
							data-validation="length" data-validation-length="min1" data-validation-error-msg="This field is required" title="Fill this field"
							maxlength="255">
					</div>
					<button type="submit" class="btn btn  btn-primary btn-block">Register</button>
				</form>

			</div>

		</div>
	</div>

	<!-- FOOTER -->
	<div id="footer-container" class="fixed-bottom" style="text-align: left; background-color: #555; padding: 8px 32px; color: white;">
		<p style="margin: 0px;">
			This website was developed by <a href="https://www.linkedin.com/in/antonio-visciglia-930b00146/"> Antonio Visciglia <img width="20px"
				height="20px" src="https://i.imgur.com/1DoZJ3W.png">
			</a> &amp;&amp; <a href="https://www.linkedin.com/in/matteomiceli/"> Matteo Miceli <img width="20px" height="20px"
				src="https://i.imgur.com/1DoZJ3W.png">
			</a>
		</p>
	</div>

</body>
</html>
