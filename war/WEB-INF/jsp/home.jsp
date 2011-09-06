<html><body>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>
  <c:when test="${isUserAdmin}">
    You're an admin!
  </c:when>
  <c:otherwise>
    You're not an admin :(
  </c:otherwise>
</c:choose>


</body></html>