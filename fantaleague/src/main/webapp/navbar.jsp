<%@ page session="true" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>

	<nav id="navbar-main" class="navbar navbar-dark fixed-top navbar-expand-lg">

		<a class="navbar-brand" href="home"> <img src="img/ic_fantaleague_navbar.png" class="d-inline-block align-top" alt="">
		</a>

		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown"
			aria-expanded="false" aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarNavDropdown">
			<ul class="navbar-nav">
				<li class="nav-item"><a class="nav-link" href="home">Home</a></li>
				<li class="nav-item"><a class="nav-link" href="create-league">Create League</a></li>
			</ul>

			<form class="nav-item dropdown form-inline my-0">
				<input id="search-player" class="form-control" type="search" placeholder="Search players" aria-label="Search" data-toggle="dropdown">
				<div id="dropdown-players" class="dropdown-menu" style="overflow: auto; max-height: 400px; display: none;">
				</div>
			</form>

			<ul class="navbar-nav ml-auto">
				<li class="nav-item nav-link dropdown">
					<div class="dropdown-toggle mr-auto" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true"
						aria-expanded="false" style="color: white; cursor: pointer;">${user.email}</div>
					<div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
						<a class="dropdown-item" href="settings">Settings</a> 
						<a class="dropdown-item" href="mailto:fantaleague.siw18@gmail.com">Help</a> 
						<a class="dropdown-item" href="logout">Logout</a>
					</div>
				</li>
			</ul>
		</div>

	</nav>

</body>

</html>
