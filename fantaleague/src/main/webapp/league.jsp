<%@page import="org.apache.taglibs.standard.lang.jstl.ValueSuffix"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>League ${league.name}</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>
<!-- Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js" type="text/javascript"></script>

<!-- tags input -->
<link rel="stylesheet" href="css/tagsinput.css" />
<script src="js/tagsinput.js"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<script src="js/league.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/theme.css">

</head>
<body>
	<div id="nav-placeholder"></div>

	<div class="container-fluid container-default">

		<div class="row">

			<div class="col-sm-1 col-md-1 col-lg-1"></div>

			<div class="col-lg-7">

				<div style="overflow: hidden;" class="box-shadow padding-16 mb-4">

					<ul id="list-days" style="list-style: none;">

						<c:forEach begin="1" end="${day}" step="1" var="d">
							<li class="league-list-day"><c:if test="${d eq daySelected}">
									<a class="a-day btn btn-sm btn-primary active" href="league?id=${league.id}&s=${status}&d=${d}" onclick="selectDay(${d}, this)"> ${d} </a>
								</c:if> <c:if test="${d ne daySelected}">
									<a class="a-day btn btn-sm btn-primary" href="league?id=${league.id}&s=${status}&d=${d}" onclick="selectDay(${d}, this)"> ${d} </a>
								</c:if></li>
						</c:forEach>

					</ul>

				</div>

				<div class="row">
					<c:forEach items="${playerteamvote}" var="team">
						<div class="col-md-6">

							<h5 id="team-id-${team.key}">${teams[team.key]}</h5>

							<div class="box-shadow mb-4">

								<table id="table-league" class="table table-hover table-custom" style="margin-bottom: 0px !important;">
									<thead>
										<tr>
											<th class="col-sm-9">Player</th>
											<th class="col-sm-1">Equipe</th>
											<th class="col-sm-1">V</th>
											<th class="col-sm-1">FV</th>
										</tr>
									</thead>
									<tbody>

										<c:set var="teamtotal" value="0" scope="page" />

										<c:forEach items="${team.value}" var="playervote">

											<c:set var="teamtotal" value="${teamtotal + playervote.finalVote}" scope="page" />

											<tr class="pid-${playervote.playerId}">
												<td><a href="player-stats?pid=${playervote.playerId}">${playervote.name}</a></td>
												<td>${playervote.equipe}</td>
												<td>${playervote.vote}</td>
												<td>${playervote.finalVote}</td>
											</tr>
										</c:forEach>

										<tr class="box-title">
											<td>Total</td>
											<td></td>
											<td></td>
											<td>${teamtotal}</td>
										</tr>

									</tbody>
								</table>

							</div>
						</div>
					</c:forEach>

				</div>

			</div>

			<div class="col-lg-3">

				<!-- Admin Dashboard -->
				<div id="admin-dashboard" style="display: none" class="box-shadow mb-4">

					<p class="box-title">Admin Dashboard</p>

					<div style="display: block;" class="box-padding">

						<!-- Create Team button -->
						<div>Teams creation and edit.</div>
						<a class="btn btn-primary btn-block mb-4" id="create-team" href="create-team?id=${league.id}">Manage Teams</a>

						<div class="mb-4">
							<label style="display: block;">League Name</label>

							<div class="form-control">

								<input id="league-name-input" readonly type="text" value="${league.name}" style="outline: none; width: 60%; border: 0px;">

								<div style="width: 40%; float: right; text-align: right;">
									<button id="league-name" style="width: 50%; text-align: center;" class="btn btn-success">
										<i class="material-icons" style="font-size: 16px;">mode_edit</i>
									</button>
									<div id="ar-league-name" class="btn-group" style="width: 100%; display: none;">
										<div style="width: 50%">
											<button style="width: 100%;" class="btn btn-success btn-invite-accept" onclick="updateLName(${league.id})">
												<i class="material-icons" style="font-size: 16px;">check</i>
											</button>
										</div>
										<div style="width: 50%" class="ml-1">
											<button id="btn-refuse" style="width: 100%;" class="btn btn-danger btn-invite-refuse" onclick="initLeague()">
												<i class="material-icons" style="font-size: 16px;">close</i>
											</button>
										</div>
									</div>
								</div>
							</div>
						</div>

						<!-- Invite Form -->

						<c:if test="${not empty error}">
							<p class="error-tag">${error.message}</p>
						</c:if>
						<c:if test="${not empty success}">
							<p class="success-tag">Invites sent successfully</p>
						</c:if>

						<div>Invite your friends to join your league!</div>
						<form method="post" id="form-invite" class="form-group" onsubmit="callServlet()" role="form">

							<input name="email" id="inviteEmail" style="display: block;" placeholder="Emails separated by commas" required> <input type="text"
								name="invite-message" class="form-control mb-2" id="inviteMessage" placeholder="Write your message here" maxlength="180"> <input
								type="hidden" name="league-name" class="form-control" id="league-name" value="${league.name}"> <input type="hidden" name="league-id"
								class="form-control mb-2" id="league-id" value="${league.id}">
							<button type="submit" class="btn btn btn-primary btn-block" id="inviteButton">Invite</button>

						</form>

						<a class="btn btn-danger btn-block mb-4" style="color: white;" onclick="deleteLeague('${league.name}', '${league.id}')">Delete League</a>

					</div>

				</div>

				<!-- User Dashboard -->
				<div id="user-dashboard" class="box-shadow mb-4">

					<p class="box-title">User Dashboard</p>

					<div style="display: block;" class="box-padding">

						<a class="btn btn-primary btn-block mb-4" href="line-up?lid=${league.id}">Line-Up your team</a>

						<div>
							<label style="display: block;">Team Name</label>

							<div class="form-control">

								<input id="team-name-input" readonly type="text" value="${userteam.name}" style="width: 60%; border: 0px;">

								<div style="width: 40%; float: right; text-align: right;">

									<button id="team-name" style="width: 50%;" class="btn btn-success">
										<i class="material-icons" style="font-size: 16px;">mode_edit</i>
									</button>

									<div id="ar-team-name" class="btn-group" style="width: 100%; display: none;">
										<div style="width: 50%;">
											<button style="width: 100%;" class="btn btn-success btn-invite-accept" onclick="updateTeam(${userteam.id})">
												<i class="material-icons" style="font-size: 16px;">check</i>
											</button>
										</div>
										<div style="width: 50%;" class="ml-1">
											<button style="width: 100%;" id="btn-refuse" class="btn btn-danger btn-invite-refuse" onclick="initUserDash()">
												<i class="material-icons" style="font-size: 16px;">close</i>
											</button>
										</div>
									</div>
								</div>
							</div>

						</div>

					</div>

				</div>

				<h5>Leaderboard</h5>

				<div class="box-shadow mb-4">

					<div class="table-responsive">

						<table id="board-league" class="table table-hover table-custom">
							<thead>
								<tr>
									<th class="col-sm-*">Position</th>
									<th class="col-sm-*">Team Name</th>
									<th class="col-sm-*">Total Score</th>
								</tr>
							</thead>
							<tbody>
								<!-- Incremental value for table positions -->
								<c:set var="count" value="1" scope="page" />
								<c:forEach items="${teamscore}" var="team">
									<tr>
										<td>${count}</td>
										<td id="leaderboard-${team.teamId}">${team.teamName}</td>
										<td>${team.score}</td>
									</tr>
									<c:set var="count" value="${count + 1}" scope="page" />
								</c:forEach>
							</tbody>
						</table>

					</div>

				</div>

				<div class="box-shadow mb-4">

					<p id="rules" class="box-title">'${league.name}' league rules</p>

					<div class="box-padding">

						<p>Starting credits: ${league.credits}.</p>

						<p>
							Duplicate players:
							<c:if test="${league.duplicatePlayers eq 'true'}">YES.</c:if>
							<c:if test="${league.duplicatePlayers eq 'false'}">NO.</c:if>
						</p>

						<p>You may build your team as follow:</p>
						<p>${league.goalkeepers}
							goalkeepers<br> ${league.defenders} defenders<br> ${league.midfielders} midfielders<br> ${league.forwards} forwards
						</p>

						<p>You have to line-up your team at least ${league.maxTimeToLineup} minutes before the first match of the day.</p>

						<p>Available modules: ${league.modules}.</p>

						<p>Good Luck, Have Fun!</p>

					</div>

				</div>

			</div>

			<div class="col-sm-1 col-md-1 col-lg-1"></div>

		</div>

	</div>

	<input id="status-user" type="hidden" value="${status}">

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