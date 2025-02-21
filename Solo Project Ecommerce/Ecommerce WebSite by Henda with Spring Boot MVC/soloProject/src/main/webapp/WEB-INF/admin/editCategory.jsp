<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!-- New line below to use the JSP Standard Tag Library -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!-- Only in Form page to use data binding and to deal with Model error validation -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page isErrorPage="true"%>



<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Edit Category</title>
<!--CSS & js Folder-->
<link rel="stylesheet" type="text/css" href="/css/style.css">
<script type="text/javascript" src="/js/app.js"></script>

<!-- for Bootstrap CSS -->
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<!-- YOUR own local CSS -->
<link rel="stylesheet" href="/css/main.css" />
<!-- For any Bootstrap that uses JS -->
<script src="/webjars/bootstrap/js/bootstrap.min.js"></script>

</head>
<body style="background-color: #faf9f6;">
	<div class="container1">

		<!-- Edit Form : add hidden // method = post or put -->
		<h1 class="title">Edit Category</h1>
		<!-- Logout -->
		<div class="logout-style">
			<form id="logoutForm" method="POST" action="/logout">
				<input type="hidden" name="${_csrf.parameterName}"
					value="${_csrf.token}" /> <input type="submit"
					class="btn btn-danger" value="Logout!" />
			</form>
		</div>
		<br />
		<div>
			<a href="/home1">back to admin dashboard</a>
		</div>
		<br />

		<form:form action="/editCategory/${category.id}" method="POST"
			modelAttribute="category">

			<input type="hidden" name="_method" value="put">
			<div class="form-group">
				<form:label path="name">Name:</form:label>
				<form:errors path="name" class="text-danger" />
				<form:input path="name" class="form-control" />
			</div>
			<!-- This is a hidden row to submit user id when creating a new Book -->
			<div class="form-group row">
				<form:errors path="user" class="error" />
				<form:input type="hidden" path="user" value="${user.id}"
					class="form-control" />
			</div>
			<input type="submit" value="Edit" class="btn btn-success" />
		</form:form>


		<br />
		<!-- Delete -->
		<form action="/deleteCategory/${category.id}" method="post"
			style="display: inline">
			<input type="hidden" name="_method" value="delete"> <input
				type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
			<button type="submit" class="btn btn-danger"
				onclick="return confirm('Are you sure you want to delete this category?')">Delete</button>
		</form>
	</div>

</body>
</html>