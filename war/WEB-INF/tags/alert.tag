<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@attribute name="alert" required="true"
	type="it.golem.alerting.model.Alert"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="col-md-3 alert">
	<c:choose>
		<c:when test="${alert.level eq 'INFO'}">
			<div class="alert alert-info">
				<h4 class="media-heading">${alert.name}
					<p>${alert.message}</p>
					<button onClick='markAsRead("${alert.id}", this);'
						class="btn btn-xs btn-info">Mark As Read</button>
			</div>
		</c:when>
		<c:when test="${alert.level eq 'DANGER'}">
			<div class="alert alert-danger">
				<h4 class="media-heading">${alert.name}
					<p>${alert.message}</p>
					<button onClick='markAsRead("${alert.id}", this);'
						class="btn btn-xs btn-danger">Mark As Read</button>
			</div>
		</c:when>
		<c:when test="${alert.level eq 'WARNING'}">
			<div class="alert alert-warning">
				<h4 class="media-heading">${alert.name}
					<p>${alert.message}</p>
					<button onClick='markAsRead("${alert.id}", this);'
						class="btn btn-xs btn-warning">Mark As Read</button>
			</div>
		</c:when>
		<c:otherwise>
			<div class="alert alert-default">
				<h4 class="media-heading">${alert.name}
					<p>${alert.message}</p>
					<button onClick='markAsRead("${alert.id}", this);'
						class="btn btn-xs btn-default">Mark As Read</button>
			</div>
		</c:otherwise>
	</c:choose>
</div>