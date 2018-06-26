<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta charset="utf-8">
<title>Error ${error.code}</title>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<!-- Popper -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/css/bootstrap.min.css">
<!-- Bootstrap JS -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta.3/js/bootstrap.min.js"></script>

<script src="js/jquery.fittext.js"></script>

<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/css?family=Lato" rel="stylesheet">
<link href="css/theme.css" rel="stylesheet">

</head>

<body>

	<div class="container-error">

		<p class="error-code">${error.code}</p>
		<p class="error-message">${error.message}</p>
		<a class="btn btn-primary error-btn" href="home">Return to home</a>

	</div>

	<script>
    	jQuery('.error-code').fitText(0.8);
    	//jQuery('.error-message').fitText(1.2);
    </script>

</body>

</html>
