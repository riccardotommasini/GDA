<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template title="GDA - Benchmark Form">
	<jsp:attribute name="header">
		<h1>Benchmark Form</h1>
	</jsp:attribute>
	<jsp:attribute name="scripts">
	
	<script type="text/javascript" src="../js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/jasny-bootstrap.js"></script>
	<script type="text/javascript" src="../js/autocomplete.js"></script>
	<script type="text/javascript" src="../js/addinput.js"></script>
	
	</jsp:attribute>
	<jsp:body>	
		<div class="row">
			<div class="col-md-12">
				<form name="benchmarkForm" action="/benchmark/new" method="post">
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label for="name">Select a Country or insert a new one:</label>
								<input type="text" name="country" class="form-control"
									id="newcountry" placeholder="Country" required>
								<input type="text" name="customer" class="form-control"
									id="newcustomer" placeholder="Customer" required>
							</div>
						</div>
					</div>
					<label>Insert Field for Benchmarking:</label>
					<p>Leave zero for fields you don't want to specify</p>
									<c:forEach var="s" items="${attributes}">
										<div id="attrline1" class="row">
											<div class="col-md-3">
												<label>${s}</label>
											</div>
											<div class="col-md-3">
												<input type="number" id="value1" name="${s}" class="form-control" min="0" placeholder="0">
											</div>		
									   </div>
									</c:forEach>
						<br>
					<br><br>
					<button type="submit" class="btn btn-primary">Crea</button>
				</form>	
			</div>
		</div>
		
	
	</jsp:body>
</t:template>