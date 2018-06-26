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

<script src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js" type="text/javascript"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<script type="text/javascript" src="js/home.js"></script>
<link rel="stylesheet" href="css/theme.css">

</head>
<body>

	<div id="nav-placeholder"></div>

	<div class="container-fluid container-default">

		<div class="row">

			<div class="col-sm-1 col-md-1 col-lg-1"></div>

			<div id="home-tables" class="col-lg-7 col-centered">

				<!-- ADMIN TABLE -->
				<c:if test="${not empty adminleagues}">

					<h5>Your leagues</h5>

					<div class="box-shadow mb-4">

						<div class="table-responsive">

							<table class="table table-hover table-custom">
								<thead>
									<tr>
										<th class="col-sm-*">League</th>
										<th class="col-sm-*">Team</th>
										<th class="col-sm-*">Last</th>
										<th class="col-sm-*">Score</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${adminleagues}" var="item">
										<tr style="cursor: pointer;" class="table-row" data-href="${item.leagueId}" data-status="A">
											<td>${item.leagueName}</td>
											<td>${item.teamName}</td>
											<td>${item.lastScore}</td>
											<td>${item.totalScore}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>

						</div>
						<!-- table responsive -->

					</div>
					<!-- box shadow -->

				</c:if>

				<!-- USER TABLE -->
				<c:if test="${not empty userleagues}">

					<h5 class="table-h5">Leagues you're in</h5>

					<div class="box-shadow">

						<div class="table table-responsive">

							<table class="table table-hover table-custom">
								<thead>
									<tr>
										<th class="col-sm-*">League</th>
										<th class="col-sm-*">Team</th>
										<th class="col-sm-*">Last</th>
										<th class="col-sm-*">Score</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${userleagues}" var="item">
										<tr style="cursor: pointer;" class="table-row" data-href="${item.leagueId}" data-status="R">
											<td>${item.leagueName}</td>
											<td>${item.teamName}</td>
											<td>${item.lastScore}</td>
											<td>${item.totalScore}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>

						</div>
						<!-- table responsive -->

					</div>
					<!-- box shadow -->

				</c:if>

				<div class="box-shadow mb-4">

					<p class="box-title">Serie A Fixtures</p>

					<div class="box-padding">

						<c:forEach items="${seriea}" var="match">

							<div onclick="fetchLocation(${match.id}, '${match.location}')" style="overflow: hidden;" class="fixtures-row mb-3" data-toggle="collapse"
								aria-expanded="false" aria-controls="collapse-${match.id}">

								<div class="home-match-time">${match.time}</div>
								<div class="home-match-row">
									<div class="home-match-home">${match.team1}</div>
									<div class="home-match-dash">-</div>
									<div class="home-match-away">${match.team2}</div>
								</div>
								<div class="collapse" id="collapse-${match.id}">
									<div style="background-color: white;" class="card card-body">
										<iframe id="map-${match.id}" width="100%" height="300px" style="border: 0; background-color: white;"></iframe>
									</div>
								</div>

							</div>

						</c:forEach>

					</div>

				</div>

			</div>

			<div class="col-lg-3 col-centered">

				<c:if test="${not empty error}">
					<p class="error-tag">${error.message}</p>
				</c:if>

				<!-- INVITES TABLE -->
				<c:if test="${not empty invites}">
					<div id="home-invite" class="box-shadow mb-4">

						<table class="table table-hover table-custom">
							<thead>
								<tr>
									<th>League Invites</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${invites}" var="invite">
									<tr>
										<td>${invite.name}
											<div class="float-right">
												<button class="btn btn-success btn-invite-accept" data-href="${invite.id}">
													<i class="material-icons" style="font-size: 16px;">check</i>
												</button>
												<button class="btn btn-danger btn-invite-refuse" data-href="${invite.teamId}">
													<i class="material-icons" style="font-size: 16px;">close</i>
												</button>
											</div>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:if>

				<!-- COUNTDOWN -->
				<div id="home-countdown" class="box-shadow">

					<p class="box-title">Until next match:</p>

					<div id="count-down-message" class="pb-3" style="font-weight: bold; display: none;"></div>

					<div id="count-down" style="display: none;" class="row justify-content-center align-items-center">
						<div class="count-down-col">
							<h5 id="days-group" class="digits">00</h5>
							<p class="label">D</p>
						</div>
						<div class="count-down-col">
							<h5 id="hours-group" class="digits">00</h5>
							<p class="label">H</p>
						</div>
						<div class="count-down-col">
							<h5 id="minutes-group" class="digits">00</h5>
							<p class="label">M</p>
						</div>
						<div class="count-down-col">
							<h5 id="seconds-group" class="digits">00</h5>
							<p class="label">S</p>
						</div>
					</div>

				</div>

				<!-- FEED RSS -->
				<div id="home-feed-rss" class="box-shadow">
					<script type="text/javascript" src="https://feed.mikle.com/js/fw-loader.js" data-fw-param="62931/"></script>
				</div>

			</div>

			<div class="col-sm-1 col-md-1 col-lg-1"></div>

		</div>

	</div>

	<input type="hidden" id="next-match" name="next-match" value="${nextMatch}">

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