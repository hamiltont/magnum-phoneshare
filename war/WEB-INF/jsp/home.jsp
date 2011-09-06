<html><body>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div>
	Magnum Phoneshare! - 

	<c:if test="${isUserAdmin}">
		<a href="/phonedb">Phone DB</a> | 
		<a href="/users">Users</a> | 
		<a href="/settings">Admin Settings</a> | 
	</c:if>

	<a href="/help">Help</a> | 
	<a href="/logout">Logout</a>
</div>

<div>
	
	<c:if test="!${isUserAdmin}">
	<h3>Your Phones</h3>
	<table>
	<tr>
		<td>Model</td>
		<td>Checked Out</td>
		<td>Serial</td>
		<td>&nbsp;</td>
	</tr>
	<c:forEach items="${userphones}" var="up">
		<tr>
			<td>${up.model}</td>
			<td>${up.checked_out}</td>
			<td>${up.serial}</td>
			<td>X</td>
		</tr>
	</c:forEach>
	</table>
	</c:if>

	<h3>Available Phones</h3>
	<table>
	<tr>
		<td>Model</td>
		<td>Quantity</td>
		<c:if test="${isUserAdmin}">
			<td>Location</td>
		</c:if>
		<c:if test="!${isUserAdmin}">
			<td>&nbsp;</td>
		</c:if>
	</tr>
	<c:forEach items="${availphones}" var="ap">
		<tr>
			<td>${ap.model}</td>
			<td>${ap.quantity}</td>
			<c:if test="${isUserAdmin}">
				<td>${ap.location}</td>
			</c:if>	
			<c:if test="!${isUserAdmin}">
				<td>X</td>
			</c:if>
		</tr>
	</c:forEach>
	</table>
	
	
	<h3>Checked Out Phones</h3>
	<table>
	<tr>
		<td>Model</td>
		<td>To Whom</td>	
		<td>When</td>
		<td>&nbsp;</td>
	</tr>
	<c:forEach items="${outphones}" var="op">
		<tr>
			<td>${op.model}</td>
			<td>${op.to}</td>
			<td>${op.when}</td>
			<td>X</td>
		</tr>
	</c:forEach>
	</table>
	
</div>

	




</body></html>