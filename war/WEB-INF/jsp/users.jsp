<html><body>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	Magnum Phoneshare! - 

	<a href="/home">Home</a> | 
	
	<c:if test="${isAdminUser}">
		<a href="/phonedb">Phone DB</a> | 
		<a href="/users">Users</a> | 
		<a href="/settings">Admin Settings</a> | 
	</c:if>

	<a href="/help">Help</a> | 
	<a href="/logout">Logout</a>
</div>

<div>

	<table border="1">
	<tr>
		<td>Name</td>
		<td>Phone #</td>
		<td>Email</td>
		<td>Phones Out</td>
	</tr>
	<c:forEach items="${users}" var="user">
		<tr>
			<td>
			<c:if test="${isAdminUser}">*</c:if>
			${user.name}
			</td>
			<td>${user.phoneNumber}</td>
			<td>${user.email}</td>
			<td>5</td>
		</tr>
	</c:forEach>
	</table>

</div>

</body></html>