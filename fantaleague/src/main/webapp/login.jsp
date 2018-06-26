<%@page import="org.apache.taglibs.standard.lang.jstl.ValueSuffix"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>
<!-- Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Validator jquery -->
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.3.26/jquery.form-validator.min.js"></script>

<link rel="stylesheet" type="text/css" href="css/theme.css">
<script type="text/javascript" src="js/login.js"></script>

</head>

<body class="img-background">

	<div class="container-fluid h-100">

		<div class="row h-100 justify-content-center align-items-center">

			<div class="form-box">
			
				<c:if test="${not empty error}">
					<p class="error-tag">${error.message}</p>
				</c:if>
				
				<c:if test="${not empty success}">
					<p class="success-tag">Registration successful</p>
				</c:if>				

				<h3>
					Please Log In, or <a href="registration">Sign Up</a>
				</h3>

				<form id="login" method="post" action="login" role="form">
					<div class="form-group">
						<label for="inputUsernameEmail">Email</label> <input type="text" data-validation="email" data-validation-error-msg="Email not valid"
							title="Fill this field" name="email" class="form-control" id="inputUsernameEmail">
					</div>
					<div class="form-group">
						<a class="pull-right" href="forgot-password">Forgot password?</a> <label for="inputPassword">Password</label> <input type="password"
							data-validation="length" data-validation-length="min1" data-validation-error-msg="This field is required" title="Fill this field"
							name="password" class="form-control" id="inputPassword">
					</div>
					<button type="submit" class="btn btn btn-primary btn-block">Log In</button>
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
