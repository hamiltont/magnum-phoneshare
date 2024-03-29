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

<a href="/register_phone">New Phone</a>

<div>

	<table border="1">
	<tr>
		<td>Model</td>
		<td>Location</td>
		<td>How Long</td>
		<td>Serial</td>
	</tr>
	<c:forEach items="${phone}" var="ph">
		<tr>
			<td><a href="/device/${ph.serial}">${ph.model}</a></td>
			<td>${ph.location}</td>
			<td>${ph.time}</td>
			<td>${ph.serial}</td>
		</tr>
	</c:forEach>
	</table>

</div>


</body></html>