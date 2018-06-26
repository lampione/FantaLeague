<%@ page session="true" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Homepage</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<!-- Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.4.0/Chart.min.js"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="text/javascript" src="js/player-stats.js"></script>
<link rel="stylesheet" href="css/theme.css">

</head>
<body>

	<div id="nav-placeholder"></div>

	<div class="container-fluid container-default">
	
		<h3 id="player-name" class="mb-4" style="text-align: center;"></h3>

		<div class="row">

			<div class="col-lg-1"></div>

			<div class="col-lg-10">

				<div class="row justify-content-lg-center">

					<div id="goal" class="col-lg-2 mb-4" style="display: none;">
						<div class="box-shadow">
							<h5 style="text-align: center;">Goals</h5>
							<h2 id="goal-text" style="text-align: center;"></h2>
						</div>
					</div>
					<div id="assist" class="col-lg-2 mb-4" style="display: none;">
						<div class="box-shadow">
							<h5 style="text-align: center;">Assists</h5>
							<h2 id="assist-text" style="text-align: center;"></h2>
						</div>
					</div>
					<div id="taken" class="col-lg-2 mb-4" style="display: none;">
						<div class="box-shadow">
							<h5 style="text-align: center;">Taken</h5>
							<h2 id="taken-text" style="text-align: center;"></h2>
						</div>
					</div>
					<div id="yellow" class="col-lg-2 mb-4" style="display: none;">
						<div class="box-shadow">
							<h5 style="text-align: center;">Yellows</h5>
							<h2 id="yellow-text" style="text-align: center;"></h2>
						</div>
					</div>
					<div id="red" class="col-lg-2 mb-4" style="display: none;">
						<div class="box-shadow">
							<h5 style="text-align: center;">Reds</h5>
							<h2 id="red-text" style="text-align: center;"></h2>
						</div>
					</div>

				</div>
				
				<div class="row justify-content-lg-center">

						<div id="vote-avg" class="col-lg-4 mb-4">
						<div class="box-shadow">
							<h5 style="text-align: center;">Average Vote</h5>
							<h2 id="vote-avg-text" style="text-align: center;"></h2>
						</div>
					</div>
					<div id="fantavote-avg" class="col-lg-4 mb-4">
						<div class="box-shadow">
							<h5 style="text-align: center;">Average FantaVote</h5>
							<h2 id="fantavote-avg-text" style="text-align: center;"></h2>
						</div>
					</div>

				</div>

				<div class="row">

					<div class="col-lg-2"></div>
					<div class="col-lg-8">
						<div class="box-shadow padding-16">
							<canvas id="vote-chart"></canvas>						
						</div>
					</div>
					<div class="col-lg-2"></div>

				</div>

			</div>

			<div class="col-lg-1"></div>

		</div>

	</div>
	
	<input id="pid" type="hidden" value="${player_id}">
	
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