<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="day" required="true"%>

<c:choose>
	<c:when test="${day == 0}">Today</br>
	</c:when>
	<c:when test="${index == '1'}">
      Yesterday</br>
	</c:when>
	<c:otherwise>
      ${day} Days ago</br>
	</c:otherwise>
</c:choose>
