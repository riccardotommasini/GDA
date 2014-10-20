<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template title="GDA - Users">
	<jsp:attribute name="header">
		<h1>Users</h1>
	</jsp:attribute>
	<jsp:attribute name="scripts">
	
	<script type="text/javascript" src="../js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/jasny-bootstrap.js"></script>
	<script type="text/javascript" src="../js/autocomplete.js"></script>
	<script type="text/javascript" src="../js/user.js"></script>
	
	</jsp:attribute>
	<jsp:body>	
		<div class="row">
			<div class="col-md-4" id="admin-column">
				<c:forEach var="a" items="${admins}">
					<t:user user="${a}"/>
				</c:forEach>
			</div>
			<div class="col-md-4" id="employee-column">
			<c:forEach var="u" items="${employees}">
					<t:user user="${u}"/>
				</c:forEach>
			</div>
			<div class="col-md-4" id="users-column">
			<c:forEach var="u" items="${users}">
					<t:user user="${u}"/>
				</c:forEach>
			</div>
		</div>
		
	
	</jsp:body>
</t:template>