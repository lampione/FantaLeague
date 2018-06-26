<%@ page session="true" language="java"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Settings</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<!-- Popper -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Questi 3 sono relativi a JQUERY datepicker -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">


<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<script type="text/javascript" src="js/settings.js"></script>
<link rel="stylesheet" href="css/theme.css">

</head>
<body>

	<div id="nav-placeholder"></div>

	<div class="container-fluid container-default">

		<div class="row">

			<div class="col-lg-3"></div>
			<div class="col-lg-6">
				<div class="box-shadow padding-16">

					<div>
						<label style="display: block;">Email</label>
						<div class="row">
							<div class="col-lg-10">
								<input readonly type="text" value="${user.email}" class="form-control">
							</div>
						</div>
					</div>

					<div>
						<label  style="display: block;">Password</label>
						
						<div class="form-control">
							<div class="row">
								<div class="col-lg-9" >
									<input id="password-input" readonly type="text" placeholder="*******" style="border: 0px;">
								</div>
								<div class="col-lg-3" align="right">
									<button id="password" class="btn btn-success">  
										<i class="material-icons" style="font-size: 16px;">mode_edit</i>
									</button>
									<div id="ar-password" class="btn-group" style="display: none;">
										<div class="">
											<button class="btn btn-success btn-invite-accept" onclick="update('p')">
												<i class="material-icons" style="font-size: 16px;">check</i>
											</button>
										</div>
										<div class="ml-1">
											<button class="btn btn-danger btn-invite-refuse">
												<i class="material-icons" style="font-size: 16px;" onclick="initSettings()">close</i>
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
						
					</div>

					<div>
						<label  style="display: block;">First Name</label>
						
						<div class="form-control">
							<div class="row">
								<div class="col-lg-9" >
									<input id="first-name-input" readonly type="text" value="${user.firstName}" style="border: 0px;">
								</div>
								<div class="col-lg-3" align="right">
									<button id="first-name" class="btn btn-success">  
										<i class="material-icons" style="font-size: 16px;">mode_edit</i>
									</button>
									<div id="ar-first-name" class="btn-group" style="display: none;">
										<div class="">
											<button class="btn btn-success btn-invite-accept" onclick="update('f')">
												<i class="material-icons" style="font-size: 16px;">check</i>
											</button>
										</div>
										<div class="ml-1">
											<button id="btn-refuse" class="btn btn-danger btn-invite-refuse" onclick="initSettings()">
												<i class="material-icons" style="font-size: 16px;">close</i>
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
						
					</div>
					
					<div>
						<label  style="display: block;">Last Name</label>
						
						<div class="form-control">
							<div class="row">
								<div class="col-lg-9" >
									<input id="last-name-input" readonly type="text" value="${user.lastName}" style="border: 0px;">
								</div>
								<div class="col-lg-3" align="right">
									<button id="last-name" class="btn btn-success">  
										<i class="material-icons" style="font-size: 16px;">mode_edit</i>
									</button>
									<div id="ar-last-name" class="btn-group" style="display: none;">
										<div class="">
											<button class="btn btn-success btn-invite-accept" onclick="update('l')">
												<i class="material-icons" style="font-size: 16px;">check</i>
											</button>
										</div>
										<div class="ml-1">
											<button class="btn btn-danger btn-invite-refuse" onclick="initSettings()">
												<i class="material-icons" style="font-size: 16px;">close</i>
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
						
					</div>

					<div>
						<label  style="display: block;">Born</label>
						
						<div class="form-control">
							<div class="row">
								<div class="col-lg-9" >
									<input id="born-input" readonly type="text" value="${user.born}" style="border: 0px;">
								</div>
								<div class="col-lg-3" align="right">
									<button id="born" class="btn btn-success">  
										<i class="material-icons" style="font-size: 16px;">mode_edit</i>
									</button>
									<div id="ar-born" class="btn-group" style="display: none;">
										<div class="">
											<button class="btn btn-success btn-invite-accept" onclick="update('b')">
												<i class="material-icons" style="font-size: 16px;">check</i>
											</button>
										</div>
										<div class="ml-1">
											<button class="btn btn-danger btn-invite-refuse" onclick="initSettings()">
												<i class="material-icons" style="font-size: 16px;">close</i>
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>
						
					</div>
										
				</div>
			</div>

			<div class="col-lg-3"></div>
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