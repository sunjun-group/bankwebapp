<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="true" %>

<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="author" content="phuc@sutd.edu.sg">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <link rel="icon" href="<c:url value="/resources/img/sutd-logo.ico" />">
    <title>bankwebapp</title>

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bootstrap.min.css" />">
    <script type="text/javascript" src="<c:url value="/resources/js/jquery-1.11.3.js" />"></script>

    <!-- Custom styles for this template -->
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bootstrap-theme.min.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/sons-of-obsidian.css" />">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/bankwebapp.css" />">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    
  </head>

  <body>
	<header class="sutd-template">
	    <nav class="navbar navbar-inverse navbar-fixed-top">
	      <div class="container">
	        <div class="navbar-header">
	          <button id="navbar-button" type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
	            <span class="sr-only">Toggle navigation</span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	            <span class="icon-bar"></span>
	          </button>
	          <a class="navbar-brand" href="#"><img alt="SUTD Logo" src="<c:url value="/resources/img/sutd-logo.png" />"></a>
	        </div>
	        <div id="navbar" class="collapse navbar-collapse">
	          <ul class="nav navbar-nav">
	            <c:if test="${empty sessionScope.authenticatedUser}">
	            	<li><a href="login">Login</a></li>
	            	<li class="header-text">or</li>
            	 	<li><a href="register">Register</a></li>
	            </c:if>
	            <c:if test="${not empty sessionScope.authenticatedUser}">
	            	<li><a href="logout">Logout</a></li>
	            </c:if>
	          </ul>
	        </div><!--/.nav-collapse -->
	      </div>
	    </nav>
	</header>

	<c:if test="${not empty sessionScope.authenticatedUser}">
		<!-- Logout form -->
		<form id="logoutForm" action="logout" method="post">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		</form>
	</c:if>
	
	<input id="csrf_name" type="hidden" value="${_csrf.parameterName}">
	<input id="csrf_token" type="hidden" value="${_csrf.token}">

	<div class="welcome">
		<h1 style="text-align: center;padding-top: 150px;">Welcome to client dashboard!</h1>
	</div>
  </body>
</html>
