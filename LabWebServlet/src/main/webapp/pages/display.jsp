<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../css/table.css" />
<style type="text/css">
#photo {
	position:absolute;
	z-index:1;
	left: 600px;
	top: 50px;
	width:300px;
	height:300px;
}
</style>
<script type="text/javascript">
var popupWindow = null;
function doMouseOver(url) {
	popupWindow = window.open(url, "_blank", "left=1100,top=200,width=500,height=800");
}
function doMouseOut() {
	if(popupWindow!=null) {
		popupWindow.close();
	}
}
function showPhoto(url) {
	hidePhoto();
	var photoNode = document.getElementById("photo");
	var imgElement = document.createElement("img");
	imgElement.setAttribute("src", url);
	photoNode.appendChild(imgElement);
	
// 	photoNode.innerHTML = "<img src='"+url+"'>";
}
function hidePhoto() {
	var photoNode = document.getElementById("photo");
	while(photoNode.hasChildNodes()) {
		photoNode.removeChild(photoNode.firstChild);
	}
}
</script>
<title>Display</title>
</head>
<body>

<div id="photo"></div>

<h3>Select Product Table Result : XXX row(s) selected</h3>

<c:if test="${not empty select}">
<table>
	<thead>
	<tr>
		<th>ID</th>
		<th>Name</th>
		<th>Price</th>
		<th>Make</th>
		<th>Expire</th>
	</tr>
	</thead>
	<tbody>
	<c:forEach var="row" items="${select}">
		<c:url value="/pages/product.jsp" var="link" scope="page">
			<c:param name="id" value="${row.id}" />
			<c:param name="name" value="${row.name}" />
			<c:param name="price" value="${row.price}" />
			<c:param name="make" value="${row.make}" />
			<c:param name="expire" value="${row.expire}" />
		</c:url>
		<c:url value="/pages/photo.view" var="url">
			<c:param name="photoid" value="${row.id}" />
		</c:url>
	<tr>
		<td><a href="${link}">${row.id}</a></td>
		<td><label onmouseover="doMouseOver('${url}')" onmouseout="doMouseOut()">${row.name}</label></td>
		<td>${row.price}</td>
		<td><label onmouseover="showPhoto('${url}')" onmouseout="hidePhoto()">${row.make}</label></td>
		<td>${row.expire}</td>
	</tr>
	</c:forEach>
	</tbody>
</table>
</c:if>
<h3><a href="<c:url value="/pages/product.jsp" />">Product Table</a></h3>

</body>
</html>