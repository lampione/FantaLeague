<%@ page session="true" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Recovery Invite</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>
<!-- Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript" src="js/recovery_invite.js"></script>
<link rel="stylesheet" type="text/css" href="css/theme.css">

</head>
<body class="img-background">

	<div class="container-fluid h-100 container-default">

		<div class="row h-100 justify-content-center align-items-center">

			<div class="form-box">
			
				<c:if test="${not empty error}">
					<p class="error-tag">${error.message}</p>
				</c:if>

				<h3>
					Paste your code from e-mail<br><a href="forgot-password">Re-Send Email</a>
				</h3>

				<form id="form-recovery-invite" method="post" role="form">
					<div class="form-group">
						<label for="inputInvite">Code</label> <input type="text" name="inputInvite" class="form-control" id="inputInvite">
					</div>
					<button type="submit" class="btn btn btn-primary btn-block">Submit</button>
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