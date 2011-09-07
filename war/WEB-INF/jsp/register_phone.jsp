<html><body>


<div>
	Magnum Phoneshare! - 

	<c:if test="${isAdminUser}">
		<a href="/phonedb">Phone DB</a> | 
		<a href="/users">Users</a> | 
		<a href="/settings">Admin Settings</a> | 
	</c:if>

	<a href="/help">Help</a> | 
	<a href="/logout">Logout</a>
</div>


<form action="/register_phone" method="post"> 
Model: <input type="text" name="model" /> <br />
Serial: <input type="text" name="serial" /> <br />
<br />
<input type="submit" value="Submit" />
</form>


</body></html>