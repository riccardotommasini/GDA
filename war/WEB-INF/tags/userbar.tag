<%@tag description="Admin Panel navigation bar"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@attribute name="user" required="true"
	type="it.golem.model.users.User"%>

<nav class="navbar navbar-default navbar-static-top" role="navigation">
	<div class="navbar-inner">
		<div class="container">
			<a class="navbar-brand navbar-left" href="/home">GDT</a>
			<p class="navbar-text navbar-left">Welcome ${user.name}
				${user.surname}</p>
			<ul class="nav navbar-nav navbar-right">
				<c:if test="${user.admin}">
					<li><a href="/admin">Admin</a></li>
					<li><a href="/benchmark/new">Add Benchmark</a></li>
					<li><a href="/user">Manage Users</a></li>
					<!-- 
					<li><a href="/tools">Tools</a></li>
					 -->
				</c:if>
				<li><a href="/logout">Logout</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
			</ul>
		</div><!-- container -->
	</div><!-- navbar inner -->
</nav>