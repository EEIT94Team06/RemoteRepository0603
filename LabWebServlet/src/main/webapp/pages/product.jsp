<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="../css/main.css" />

<title>Product</title>
<script type="text/javascript" src="../js/json2.js"></script>
<script type="text/javascript" src="../js/product.js"></script>
<script type="text/javascript">
var contextPath = "${pageContext.request.contextPath}";
function doBlur() {
	document.getElementById("img").style.display = "inline";
	document.getElementsByTagName("form")[0].id.disabled = true;
	
	var id = document.getElementsByTagName("form")[0].id.value;
// 	sendGetRequest(id, contextPath+"/pages/product.view");
// 	sendPostRequest(id, contextPath+"/pages/product.view");
	sendPostJsonRequest(id, contextPath+"/pages/product.view");
}
function clearForm() {
	var spanElement = document.getElementsByTagName("span")[0];
	if(spanElement.hasChildNodes()) {
		spanElement.removeChild(spanElement.firstChild);
	}
	var inputs = document.getElementsByTagName("input");
	for(var i=0; i<inputs.length; i++) {
		if(inputs[i].type=="text") {
  			inputs[i].value="";
		}
	}
}
</script>
</head>
<body>

<h3>Welcome ${user.email}</h3>

<h3>xxxProduct Table</h3>

<form action="<c:url value="/pages/product.controller" />" method="get">
<table>
	<tr>
		<td>ID : </td>
		<td><input type="text" name="id" value="${param.id}" onblur="doBlur()" onfocus="clearForm()"></td>
		<td><span class="error">${errors.id}</span></td>
	</tr>
	<tr>
		<td>Name : </td>
		<td><input type="text" name="name" value="${param.name}"></td>
		<td><img id="img" src="../img/ajax-loader.gif" style="display:none"></td>
	</tr>

	<tr>
		<td>Price : </td>
		<td><input type="text" name="price" value="${param.price}"></td>
		<td><span class="error">${errors.price}</span></td>
	</tr>
	<tr>
		<td>Make : </td>
		<td><input type="text" name="make" value="${param.make}"></td>
		<td><span class="error">${errors.make}</span></td>
	</tr>
	<tr>
		<td>Expire : </td>
		<td><input type="text" name="expire" value="${param.expire}"></td>
		<td><span class="error">${errors.expire}</span></td>
	</tr>
	<tr>
		<td>
			<input type="submit" name="prodaction" value="Insert">
			<input type="submit" name="prodaction" value="Update">
		</td>
		<td>
			<input type="submit" name="prodaction" value="Delete">
			<input type="submit" name="prodaction" value="Select">
			<input type="button" value="Clear" onclick="clearForm()">
		</td>
	</tr>
</table>

</form>

<h3><span class="error">${errors.action}</span></h3>

<c:if test="${not empty delete}">
<h3>Delete Product Table Success : ${delete} row deleted</h3>
<script type="text/javascript">clearForm();</script>
</c:if>

<c:if test="${not empty insert}">
<h3>Insert Product Table Success</h3>
<table border="1">
	<tr><td>Id</td><td>${insert.id}</td></tr>
	<tr><td>Name</td><td>${insert.name}</td></tr>
	<tr><td>Price</td><td>${insert.price}</td></tr>
	<tr><td>Make</td><td>${insert.make}</td></tr>
	<tr><td>Expire</td><td>${insert.expire}</td></tr>
</table>
<script type="text/javascript">clearForm();</script>
</c:if>

<c:if test="${not empty update}">
<h3>Update Product Table Success</h3>
<table border="1">
	<tr><td>Id</td><td>${update.id}</td></tr>
	<tr><td>Name</td><td>${update.name}</td></tr>
	<tr><td>Price</td><td>${update.price}</td></tr>
	<tr><td>Make</td><td>${update.make}</td></tr>
	<tr><td>Expire</td><td>${update.expire}</td></tr>
</table>
<script type="text/javascript">clearForm();</script>
</c:if>

</body>
</html>