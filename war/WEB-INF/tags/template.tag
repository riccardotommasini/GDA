<%@ tag description="Main template" pageEncoding="UTF-8"
	isELIgnored="false"%>

<%@ attribute name="title" required="true"%>
<%@ attribute name="header" fragment="true"%>
<%@ attribute name="scripts" fragment="true"%>
<%@ attribute name="meta" fragment="true"%>
<%@ attribute name="stylesheet" fragment="true"%>
<%@ attribute name="user" required="false" type="it.golem.model.users.User"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>


<!doctype html>
<html>
	<head>
		<title>${title}</title>
		<link rel='stylesheet' href="/css/gda.css">
		<link rel='stylesheet'
			href='http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css'>
		
		<script type="text/javascript"
			src="http://code.jquery.com/jquery-latest.js"></script>
		<script type="text/javascript"
			src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
		
		<script type="text/javascript" src="../js/bootstrap.min.js"></script>
		<jsp:invoke fragment="scripts" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
		
	</head>
	<body >
		<c:choose>
			<c:when test="${empty user}">
				<%-- Specific for public pages --%>
				<t:bar />
				<div id="sb-site" class="container">
					<jsp:invoke fragment="header" />
					<jsp:doBody />
				</div>
			</c:when>
			<c:otherwise>
				<%-- When the user is logged in --%>
				<t:userbar user="${user}" />
				<div id="sb-site">
					<div class="container" id="privatepage-container">
						<jsp:invoke fragment="header" />
						<jsp:doBody />
					</div>
				</div>
		</c:otherwise>
		</c:choose>
		<!-- FOOTER -->
	</body>
</html>