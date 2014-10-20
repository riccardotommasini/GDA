<%@ page language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<t:template title="GDA - Admin Panel">
	<jsp:attribute name="header">
		<h1>Administration Panel</h1>
	</jsp:attribute>
	<jsp:attribute name="scripts">
	
	<script type="text/javascript"
			src="http://code.highcharts.com/highcharts.js"></script>
			<script src="http://code.highcharts.com/modules/exporting.js"></script>
	
	<script type="text/javascript" src="js/bootstrap-multiselect.js"></script>
	<link rel="stylesheet" href="css/bootstrap-multiselect.css"
			type="text/css" />
	
	
	<script type="text/javascript" src="../js/golem-scripts.js"></script>
	</jsp:attribute>
	<jsp:body>
			<div id="content_graph" class="col-md-12">
				<div id="tabs" class="row">
					<div class="tabbable tabs">
						<ul id="sampleTabs" class="nav nav-tabs">
							<li class="active"><a href="#summary" data-toggle="tab">Summary</a></li>
							<li><a href="#multiplot" data-toggle="tab">Multiplot</a></li>
							<li><a href="#graph" data-toggle="tab">ACDA</a></li>
							<li><a href="#graph" data-toggle="tab">ACDV</a></li>
							<li><a href="#graph" data-toggle="tab">ARSV</a></li>
							<li><a href="#graph" data-toggle="tab">Minutes</a></li>
							<li><a href="#graph" data-toggle="tab">Sell</a></li>
							<li><a href="#graph" data-toggle="tab">Buy</a></li>
							<li><a href="#graph" data-toggle="tab">Revenue</a></li>
							<li><a href="#graph" data-toggle="tab">Gp</a></li>
							<li><a href="#graph" data-toggle="tab">GpPercent</a></li>
							<c:if test="${user.admin}">	
								<li><a href="#tools" data-toggle="tab">Tools</a></li>
								<li><a href="#benchmark" data-toggle="tab">Benchmark</a></li>
							</c:if>
							<li><a href="#report" data-toggle="tab">Report</a></li>
						</ul>
						<div class="tab-content">
							<div class="tab-pane active" id="summary">
								<div class="row"> 
									<div class="col-md-12 alert"> 
											<c:forEach var="a" items="${alerts}">
												<t:alert alert="${a}"></t:alert>
											</c:forEach>
									</div>
								</div>
							</div>
							<div class="tab-pane" id="multiplot">
								<div class="row">
									<div class="col-md-6" id="minutes"></div>
									<div class="col-md-6" id="revenue"></div>
									<div class="col-md-6" id="acdv"></div>
									<div class="col-md-6" id="arsv"></div>
								</div>
							</div>
							<div class="tab-pane" id="tools">
								<c:if test="${user.admin}">
									<div class="row"> 
										<div class="col-md-12">
											<form name="uploadForm" method="post" action="/upload"
											enctype="multipart/form-data">
				 									<input type="file"
												
													name="measure_file" multiple=true />
												
												
												<button class="btn btn-primary" type="submit">Carica online</button>
											</form>
							            </div>
									<!-- Coulmn div -->
									</div>
								<!-- Panel Tools End -->
								</c:if>
							</div>
							<div class="tab-pane" id="benchmark">
								<div class="row"> 
									<div class="col-md-6">
								        <label>Avaliable Benchmark </label><br>
										<select class="multiselect" name="" id="benchmakr_scope" multiple=false
										style="width: 290px; height: 100px;">
											<option value='0'>Opzione Prova</option>
										</select>
						            </div>
						            <div class="col-md-6">
						              <label>Avaliable Benchmark </label><br>
									  <input type="radio" name="always_plot" value="">Enabled<br>
									  <input type="radio" name="always_plot" value="">Disabled<br>
									  <br>
									  <label>Avaliable Benchmark </label><br>
									  <input type="radio" name="always_plot" value="">Enabled<br>
									  <input type="radio" name="always_plot" value="">Disabled<br>
						            </div>
								</div>
							</div>
						<!-- Panel Benchmark End -->
							
							<div class="tab-pane" id="report">
								<div class="row"> 
									<div class="col-md-12">
								            <div class="row" id="reportdiv">
								      	  	</div>
						            </div>
								</div>
							</div>
						<!-- Panel Report End -->
							<div class="tab-pane" id="graph" />
						</div>
					</div>
				</div>
			<!-- tabs -->
			</div>
		<!-- content_graph -->
			<t:graphbar />
		
				
		<!-- jQuery -->
		<!-- Slidebars -->
		<script>
			(function($) {
				$(document).ready(function() {
					$().button('loading')
					$("[data-toggle='tooltip']").tooltip();
					$('.personal').multiselect({
						maxHeight : 300,
						buttonWidth : '120px'
					})

					$('.customers').multiselect({
						maxHeight : 300,
						enableCaseInsensitiveFiltering : true
					});
					$('.btn-group').addClass('dropup')
				});
			})(jQuery);
		</script>	
	</jsp:body>
</t:template>