<%@page import="org.apache.taglibs.standard.lang.jstl.ValueSuffix"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Line Up ${t.name}</title>

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

<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">

<!-- javascript -->
<script src="js/line_up.js" type="text/javascript"></script>

<!-- Custom CSS -->
<link rel="stylesheet" href="css/theme.css">

</head>
<body>

	<div id="nav-placeholder"></div>

	<div class="container-fluid container-default">

		<div class="row">

			<div class="col-sm-3 offset-l" style="display: none;"></div>

			<div class="col-sm-6">
			
				<div class="row">
				
					<div class="dropdown col-sm-10 mb-4">
	
						<button id="lineup-dropdown" class="btn btn-secondary btn-block dropdown-toggle" style="text-align: left;" type="button" data-toggle="dropdown" aria-haspopup="true"
							aria-expanded="false">Select Module</button>
	
						<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
							<c:forEach items="${modules}" var="module">
								<div class="dropdown-item" onclick="selectModule(${module})">${module}</div>
							</c:forEach>
						</div>
	
					</div>
					
					<div class="col-sm-2" style="text-align: left;">
						<i class="material-icons" onclick="showHelp()" style="cursor: pointer; color: #30577c; font-size: 38px;">help</i>
					</div>			
				
				</div>

			</div>

			<div class="col-sm-6">

				<form class="mb-4" method="post" action="line-up" onsubmit="return setValue(${maxlineup})">
					<input id="t-id" name="t-id" type="hidden" value="${t.id}"> <input id="starters-g" name="starters-g" type="hidden"> <input
						id="starters-d" name="starters-d" type="hidden"> <input id="starters-m" name="starters-m" type="hidden"> <input id="starters-f"
						name="starters-f" type="hidden"> <input id="bench" name="bench" type="hidden"> <input id="maxlineup" name="maxlineup"
						type="hidden" value="${maxlineup}">
					<button id="save-team" class="btn btn-primary btn-block" type="submit" disabled>Save</button>
				</form>

			</div>

			<div class="col-sm-3 offset-l" style="display: none;"></div>

		</div>

		<div class="row">

			<div class="col-sm-3 offset-l" style="display: none;"></div>
			
			<div class="col-sm-6">

				<div class="card mb-4">

					<div class="card-header" id="heading-starters">
						<button class="btn btn-link" data-toggle="collapse" data-target="#starters-content" aria-expanded="true" aria-controls="starters-content">
							Team starters</button>
					</div>

					<div id="starters-content" class="collapse show" aria-labelledby="heading-starters">

						<table id="table-starters" class="table table-hover table-custom table-100 dt-responsive">
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

				<div class="card mb-4">

					<div class="card-header" id="heading-bench">
						<button class="btn btn-link" data-toggle="collapse" data-target="#bench-content" aria-expanded="true" aria-controls="bench-content">Team
							bench</button>
					</div>

					<div id="bench-content" class="collapse show" aria-labelledby="heading-bench">

						<table id="table-bench" class="table table-hover table-custom table-100 dt-responsive">
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

			</div>

			<div id="table-team-container" class="col-sm-6">

				<div class="box-shadow mb-4">

					<table class="table table-hover table-custom table-100 dt-responsive" id="table-team-lineup">
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

			<div class="col-sm-3 offset-l" style="display: none;"></div>

		</div>

	</div>

	<input id="t-id" name="t-id" type="hidden" value="${t.id}">
	<input id="t-name" name="t-name" type="hidden" value="${t.name}">
	<input id="expired" name="expired" type="hidden" value="${expired}">

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
