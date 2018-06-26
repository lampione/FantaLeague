<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create League</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>
<!-- Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<!-- Validator jquery -->
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.3.26/jquery.form-validator.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js" type="text/javascript"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<!--  <link rel="stylesheet" href="//cdn.jsdelivr.net/bootstrap.tagsinput/0.8.0/bootstrap-tagsinput.css" /> -->
<!-- <script src="//cdn.jsdelivr.net/bootstrap.tagsinput/0.8.0/bootstrap-tagsinput.min.js"></script> -->

<link rel="stylesheet" href="css/tagsinput.css"/>
<script src="js/tagsinput.js"></script>

<script src="js/create_league.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="css/theme.css">

</head>
<body>

	<div id="nav-placeholder"></div>

	<div class="container-fluid container-default">

		<div class="row justify-content-center align-items-center">

			<div class="col-lg-5 form-box">

				<h3>Create your league</h3>

				<form id="registrationForm" method="post" action="create-league" onSubmit="return validate()" role="form" novalidate>

					<div class="form-group">
						<label>League name</label> 
						
						<input type="text" 
						name="league-name" 
						class="form-control" 
						data-validation="custom" 
						data-validation-regexp="^[A-Za-z0-9]+$" 
						data-validation-error-msg="Only use letters and numbers, without spaces" 
						title="League Name" 
						id="leagueName" 
						maxlength="255">
						
					</div>

					<div class="form-group">
						<label>Starting credits</label> 
					
						<input type="number" 
						name="starting-credits" 
						class="form-control" 
						data-validation="number"
						data-validation-allowing="range[1;100000000]" 
						data-validation-error-msg="Respect range number from 1 to 100000000"
						title="Starting credits" 
						id="initialCredits" 
						value="250">
					
					</div>
					
					<div class="col-xs-6 form-check" style="margin-bottom: 16px">
						<input class="form-check-input" type="checkbox" name="duplicate-players" id="duplicatePlayers">
						<label class="form-check-label" for="duplicate_players">
							Duplicate players?
							<i onclick="duplicateHelp()" class="col-xs-6 material-icons md-18 md-dark">help_outline</i>
						</label>
					</div>

					<div class="form-group" style="display: none">
						<label style="font-size: 12px">Max number of players: 32</label>
					</div>

					<div class="form-group">
						<label>Number of Goalkeepers</label>
						
						<input type="number" 
						name="num_goalkeepers" 
						title="Goalkeepers"
						class="form-control"
						data-validation="number"
						data-validation-allowing="range[1;8]"
						data-validation-error-msg="Respect range number: min 1, max 8"
						id="num_goalkeepers"
						step="1"
						value="3"
						min="1"
						max="8">
					
					</div>
					<div class="form-group">
						<label>Number of Defenders</label> 
						
						<input type="number" 
						title="Defenders" 
						name="num_defenders" 
						data-validation="number"
						data-validation-allowing="range[1;8]"
						data-validation-error-msg="Respect range number: min 1, max 8"
						class="form-control" 
						id="num_defenders"
						min="1"
						max="8"
						value="8">
					
					</div>
					<div class="form-group">
						<label>Number of Midfielders</label> 
						
						<input type="number" 
						title="Midfielders" 
						name="num_midfielders" 
						data-validation="number"
						data-validation-allowing="range[1;8]"
						data-validation-error-msg="Respect range number: min 1, max 8"
						class="form-control" 
						id="num_midfielders"
						min="1"
						max="8"
						value="8">
					
					</div>
					<div class="form-group">
						<label>Number of Forwards</label> 
						
						<input type="number" 
						title="Forwards" 
						name="num_forwards" 
						data-validation="number"
						data-validation-allowing="range[1;8]"
						data-validation-error-msg="Respect range number: min 1, max 8"
						class="form-control" 
						id="num_forwards"
						min="1"
						max="8"
						value="6">
					
					</div>
					
					<!-- MAX TIME LINE-UP -->
					<div class="form-group">
						<label>
							Max time to line up (in minutes)
							<i onclick="lineUpHelp()" class="material-icons md-18 md-dark">help_outline</i>
						</label> 
						<input type="number"
						data-validation="number"
						data-validation-allowing="range[1;60]"
						data-validation-error-msg="Respect range number: min 1, max 60 (1hr)"
						title="Max time to line-up"
						class="form-control"
						name="max-time"
						min="1"
						max="60"
						id="max-time">
					</div>
					
					<!-- MODULES WITH TAGS -->
					<div class="form-group">

						<label>Available modules (separate with commas, example: 343,541,...)</label>

						<!-- data-role="tagsinput" -->
						<input 
						style="max-width: 100%;"
						type="text"
						name="modules"
						data-validation="custom" 
						data-validation-regexp="^([0-9]{3},|[0-9]{3})+$"
						data-validation-error-msg="Three-numbers modules, separated by commas" 
						title="Select modules"
						value="343,352,433,442,451,532,541"
						placeholder="Insert a module"
						id="modules">
						
					</div>

					<div class="form-group">
						<label>Enter your Team name</label> 
						<input type="text" 
						name="team-name" 
						data-validation="custom" 
						data-validation-regexp="^[A-Za-z0-9]+$" 
						data-validation-error-msg="Only letters and numbers, without spaces" 
						title="Fill this field" 
						class="form-control" 
						maxlength="255" 
						id="max_time">
					</div>

					<button type="submit" class="btn btn btn-primary btn-block">Create League</button>
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
