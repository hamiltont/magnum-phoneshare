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
			<td>Model</td>
			<td>${phone.model}</td>
		</tr>
		<tr>
			<td>Status</td>
			<td>${phone.statusString}</td>
		</tr>
		<tr>
			<td>Current User</td>
			<td>${phone.currentUser.name}</td>
		</tr>
		<tr>
			<td>Serial</td>
			<td>${phone.serial}</td>
		</tr>
	</table>
</div>

<br />

<div>
<c:if test="${phone.statusString=='Checked In'}">
	Check This Phone Out To: 
	<form action="/transfer" method="POST">
		<input type="hidden" value="${phone.serial}" name="serial" />
		<input type="hidden" value="${user.googleId}" name="from" />
		<input type="hidden" value="admin2user" name="transferType" />
		<select name="to">
			<c:forEach items="${users}" var="curuser">
				<option value="${curuser.googleId}">${curuser.name}</option>
			</c:forEach>
		</select>
		<input type="submit" value="Submit" />
	</form>
</c:if>
</div>

<br />

<div>
	<table border="1">
	<tr>
		<td>From</td>
		<td>To</td>
		<td>When</td>
		<td>Accessories</td>
	</tr>
	<c:forEach items="${phone.transferRecords}" var="record">
		<tr>
			<td><c:if test="${record.from.isAdmin}">*</c:if>${record.from.Name}</td>
			<td><c:if test="${record.to.isAdmin}">*</c:if>${record.to.Name}</td>
			<td>${record.finalizedDateString}</td>
			<td>${record.hasAccessories}</td>
		</tr>
	</c:forEach>
	</table>

</div>


</body></html>