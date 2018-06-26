<%@page import="org.apache.taglibs.standard.lang.jstl.ValueSuffix"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Create Team</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<!-- Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- DataTable jquery and javascript -->
<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/1.10.16/js/dataTables.bootstrap4.min.js"></script>

<script type="text/havascript" src="https://cdn.datatables.net/responsive/2.2.1/js/dataTables.responsive.min.js"></script>
<script type="text/javascript" src="https://cdn.datatables.net/responsive/2.2.1/js/responsive.bootstrap4.min.js"></script>

<!-- bootbox -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/4.4.0/bootbox.min.js" type="text/javascript"></script>


<!-- SELECTOR -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.0-beta/css/bootstrap-select.min.css" />
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.0-beta/js/bootstrap-select.min.js"></script>

<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<!-- javascript -->
<script src="js/create_team.js" type="text/javascript"></script>

<!-- Custom CSS -->
<link rel="stylesheet" href="css/theme.css">

</head>
<body>

	<div id="nav-placeholder"></div>

	<div class="container-fluid container-default">

		<div class="row">

			<div class="col-sm-3 offset-l" style="display: none;"></div>

			<div class="col-6">

				<div class="row">

					<div class="dropdown col-sm-10 mb-2">

						<button id="team-selector" style="text-align: left;" class="btn btn-secondary btn-block dropdown-toggle" type="button" data-toggle="dropdown"
							aria-haspopup="true" aria-expanded="false">Select team</button>

						<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
							<c:forEach items="${users}" var="user">
								<div class="dropdown-item" onclick="fetchTeam('${user.teamId}','${user.teamName}')">${user.userEmail},${user.teamName}</div>
							</c:forEach>
						</div>

					</div>
					
					<div class="col-sm-2" style="text-align: left;">
					
						<i class="material-icons" onclick="showHelp()" style="cursor: pointer; color: #30577c; font-size: 38px;">help</i>
					
					</div>

				</div>

				<h3 id="league-credits"></h3>

			</div>

			<div class="col-6">

				<form id="save-team-form" class="mb-2" method="post" action="create-team" onsubmit="setValues()" style="display: block;">
					<input id="g" name="g" type="hidden"> <input id="d" name="d" type="hidden"> <input id="m" name="m" type="hidden"> <input
						id="f" name="f" type="hidden"> <input id="save-team-league-id" name="save-team-league-id" type="hidden" value="${league.id}"> <input
						id="save-team-teamid" name="save-team-teamid" type="hidden">
					<button id="save-team" class="btn btn-primary btn-block" disabled>SAVE TEAM</button>
				</form>

			</div>

			<div class="col-sm-3 offset-r" style="display: none;"></div>

		</div>

		<div class="row">

			<div class="col-sm-3 offset-l" style="display: none;"></div>

			<div id="table-player-container" class="col-sm-6">

				<div class="box-shadow mb-4">

					<nav>
						<div class="nav nav-tabs" id="nav-tab" role="tablist">
							<a class="nav-item nav-link disabled" id="g-tab" data-toggle="tab" role="tab" aria-selected="false">GK</a> <a
								class="nav-item nav-link disabled" id="d-tab" data-toggle="tab" role="tab" aria-selected="false">D</a> <a class="nav-item nav-link disabled"
								id="m-tab" data-toggle="tab" role="tab" aria-selected="false">MF</a> <a class="nav-item nav-link disabled" id="f-tab" data-toggle="tab"
								role="tab" aria-selected="false">F</a>
						</div>
					</nav>

					<input id="table-player-filter" type="text" placeholder="Search players, teams or credits" title="Type in a player name">

					<table class="table table-hover table-custom table-100 dt-responsive" id="table-search">
						<thead>
							<tr>
								<th>Id</th>
								<th>Role</th>
								<th class="col-sm-5">Player</th>
								<th class="col-sm-5">Team</th>
								<th class="col-sm-2">Quotation</th>
							</tr>
						</thead>
						<tbody id="table-search-body"></tbody>
					</table>

				</div>

			</div>

			<div id="table-team-container" class="col-sm-6">

				<div class="box-shadow mb-4">

					<table class="table table-hover table-custom table-100" id="table-team-g">
						<thead>
							<tr>
								<th>Id</th>
								<th>Role</th>
								<th class="col-sm-5">Player</th>
								<th class="col-sm-5">Team</th>
								<th class="col-sm-2">Quotation</th>
							</tr>
						</thead>
						<tbody></tbody>
					</table>

				</div>

			</div>

			<div class="col-sm-3 offset-r" style="display: none;"></div>

		</div>

	</div>

	<input id="league-id" name="league-id" type="hidden" value="${league.id}">
	<input id="league-duplicate" name="league-duplicate" type="hidden" value="${league.duplicatePlayers}">
	<input id="hidden-league-credits" type="hidden" value="${league.credits}">
	<input id="league-g-count" type="hidden" value="${league.goalkeepers}">
	<input id="league-d-count" type="hidden" value="${league.defenders}">
	<input id="league-m-count" type="hidden" value="${league.midfielders}">
	<input id="league-f-count" type="hidden" value="${league.forwards}">

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