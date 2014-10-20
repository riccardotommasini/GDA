<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@attribute name="user" required="true"
	type="it.golem.model.users.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="col-md-6 alert">
	<c:choose>
		<c:when test="${user.role eq 'ADMIN'}">
			<div class="alert alert-danger">
				<h4>${user.name}</h4>
				<h4>${user.surname}</h4>
				<h4>${user.email}</h4>
				<h4>${user.numTel}</h4>
				<button onClick='downgrade("${user.id}", this);'class="btn btn-xs btn-danger">Downgrade</button>
			</div>
		</c:when>
		<c:when test="${user.role eq 'EMPLOYEE'}">
			<div class="alert alert-warning">
				<h4>${user.name}</h4>
				<h4>${user.surname}</h4>
				<h4>${user.email}</h4>
				<h4>${user.numTel}</h4>
				<button onClick='upgrade("${user.id}", this);'class="btn btn-xs btn-warning">Upgrade</button>
				<button onClick='downgrade("${user.id}", this);'class="btn btn-xs btn-warning">Downgrade</button>
			</div>
		</c:when>
		<c:otherwise>
			<div class="alert alert-info">
				<h4>${user.name}</h4>
				<h4>${user.surname}</h4>
				<h4>${user.email}</h4>
				<h4>${user.numTel}</h4>
				<button onClick='upgrade("${user.id}", this);'class="btn btn-xs btn-info">Upgrade</button>
			</div>
		</c:otherwise>
	</c:choose>
</div>