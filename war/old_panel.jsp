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
	
	
	<script type="text/javascript" src="js/bootstrap.min.js"></script>
	<script type="text/javascript" src="../js/jasny-bootstrap.js"></script>
	
	<script type="text/javascript" src="../js/routecountry.js"></script>
	<script type="text/javascript" src="../js/graph.js"></script>
	<script type="text/javascript" src="../js/report.js"></script>
	<script type="text/javascript" src="../js/alert.js"></script>
	<script type="text/javascript" src="../js/variables.js"></script>
	<script type="text/javascript" src="../js/buttons.js"></script>
	<script type="text/javascript" src="../js/portamento.js"></script>
	<link rel="stylesheet" href="js/tablesorter/themes/blue/style.css"
			type="text/css" id="" media="print, projection, screen" />
	
	<script type="text/javascript" src="../js/tablesorter/tablesorter.js"></script>
	<script>
		$(document).ready(function() {
			$("#myTable").tablesorter();
		});
	</script>
	</jsp:attribute>
	<jsp:body>	
	<div id="content" class="row">
		<div id="content_graph" class="col-md-8">
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
							<li><a href="#tables" data-toggle="tab">Tables</a></li>
							<li><a href="#tools" data-toggle="tab">Tools</a></li>
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
									<div class="col-md-12" id="minutes"></div>
									<div class="col-md-12" id="revenue"></div>
									<div class="col-md-12" id="acdv"></div>
									<div class="col-md-12" id="arsv"></div>
								</div>
							</div>
							<div class="tab-pane" id="tables">
								<div class="row"> 
									<div class="col-md-12"> 
										<label>Selected Tables</label>
										<table id="myTable" class="tablesorter"> 
											<t:linehead />
											<tbody> 
												<c:forEach var="m" items="${today}">
													<t:line measure="${m}" />
												</c:forEach>
											</tbody> 
										</table> 
									</div>
								</div>
							</div>
							<div class="tab-pane" id="tools">
								<div class="row"> 
									<div class="col-md-12">
										<form name="uploadForm" method="post" action="/upload"
											enctype="multipart/form-data">
											<div class="fileinput fileinput-new input-group"
												data-provides="fileinput">
		 										<span class="input-group-addon btn btn-default btn-file">
			 									<span class="fileinput-exists">Change</span>
			 										<input type="file"
													class="input-group-addon btn btn-default btn-file fileinput-new"
													name="measure_file" multiple="true" />
			 										Select Measure File 
		 										</span>
		  										<a href="#"
													class="input-group-addon btn btn-default fileinput-exists"
													data-dismiss="fileinput">Remove</a>
		  										<div class="form-control" data-trigger="fileinput">
													<i class="glyphicon glyphicon-file fileinput-exists"></i>
													<span class="fileinput-filename"></span>
												</div>
											</div>
											
											<div class="fileinput fileinput-new input-group"
												data-provides="fileinput">
		 										<span class="input-group-addon btn btn-default btn-file">
			 									<span class="fileinput-exists">Change</span>
			 										<input type="file"
													class="input-group-addon btn btn-default btn-file fileinput-new"
													name="measure_file" multiple="true" />
			 										Select Benchmark File 
		 										</span>
		  										<a href="#"
													class="input-group-addon btn btn-default fileinput-exists"
													data-dismiss="fileinput">Remove</a>
		  										<div class="form-control" data-trigger="fileinput">
													<i class="glyphicon glyphicon-file fileinput-exists"></i>
													<span class="fileinput-filename"></span>
												</div>
											</div>
											
											<button class="btn btn-primary" type="submit">Carica online</button>
										</form>
						            </div>
								</div>
							</div>
							<div class="tab-pane" id="report">
								<div class="row"> 
									<div class="col-md-12">
								            <div class="row" id="reportdiv">
								      	  	</div>
						            </div>
								</div>
							</div>
							<div class="tab-pane" id="graph" />
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="input_panel" class="col-md-4">
			<div id="menu" class="row">
				<div id="day_select" class="col-md-12">
				<label>Day Selection </label><br>
					<select class="multiselect" name="day" id="day" multiple=true
						style="width: 290px; height: 69px;">
						<option value='0,1'>Today&#38Yesterday</option>
							<c:forEach var="i" begin="0" end="7">
				     			<option value="${i}"><t:day day="${i}"></t:day></option>
							</c:forEach>
					</select>
				</div>
				<div id="customer_select" class="col-md-12"> 
						<label>Customers Selection </label>
						<div style="display: none" id="loading_customer">
							<img src="/img/loading_32.gif"> loading...</div>
							<div id="tohide_customer">
					 			<select class="multiselect" name="customers" id="customers"
							multiple=true style="width: 290px; height: 69px;">
									<option value="All">All</option>
								     <c:forEach var="c" items="${customers}">
										<option ${c}>${c}</option>
									</c:forEach>
								</select>
							</div>
				</div>
				<div id="country_select" class="col-md-12">
						<label>Countries Selection</label>
						<div style="display: none" id="loading_country">
						<img src="/img/loading_32.gif"> loading...</div>
						<div id="tohide_country">
							<select class="multiselect" name="country" id="country"
							multiple=true
							style="border: 3px solid #2f7ed8; width: 290px; height: 69px;">
								<option value="All">All</option>
								<c:forEach var="c" items="${routes}">
									<option ${c.country.key.name}>${c.country.key.name}</option>
								</c:forEach>
						    </select>
						</div>
				</div>
				<div id="active_benchmark_select" class="col-md-12">
						<label>Active Benchmark</label>
						<div style="display: none" id="loading_benchmark_select"
						class="loading_benchmark_select">
						<img src="/img/loading_32.gif"> loading...</div>
						<div id="tohide_benchmark_select" class="tohide_benchmark_select">
							<select
							style="border: 3px solid #2f7ed8; width: 290px; height: 69px;"
							class="multiselect" name="cbenchmark" id="cbenchmark"
							multiple=true style="height: 69px;">
									<option>All</option>
							</select>
						</div>
				</div>
				<div id="unactive_benchmark_select" class="col-md-12">
						<label>Disabled Benchmark</label>
						<div style="display: none" id="loading_benchmark_select"
						class="loading_benchmark_select">
						<img src="/img/loading_32.gif"> loading...</div>
						<div id="tohide_benchmark_select" class="tohide_benchmark_select">
							<select
							style="border: 3px solid #2f7ed8; width: 290px; height: 69px;"
							class="multiselect" name="disabledbenchmark"
							id="disabledbenchmark" multiple=true style="height: 69px;">
								 <option>All</option>
							</select>
						</div>
				</div>
				<div class="col-md-12">
					<br>
					<button class="btn btn-default" id="calcbutton">Calculate Benchmark
					<div style="display: none" id="loading_benchmark_calc">
						<img src="/img/loading_32.gif" />
					</div>
					</button>
					<p>Push this button to calculate a 7 days benchmark</p>
				</div>
				<div class="col-md-12">
					<button class="btn btn-default" id="savebutton">Save Benchmark
						<div style="display: none" id="loading_benchmark_save">
							<img src="/img/loading_32.gif" />
						</div>
					</button>
				<p>Push this button to save the benchmark just plot,
					it will be visible on the box just on the right</p>
				</div>
				<div class="col-md-12">
					<button class="btn btn-default" id="defaultbutton">Set Default
						<div style="display: none" id="loading_benchmark_def">
							<img src="/img/loading_32.gif" />
						</div>
					</button>
					<p>Push this button to set this benchmark as default for plotting</p>
				</div>
				<div class="col-md-12">
					<button class="btn btn-default" id="activationbutton">On/OFF
					<div style="display: none" id="loading_benchmark_act">
						<img src="/img/loading_32.gif" />
					</div>
					</button>
					<p>Push this button to activate/disable the selected benchmark</p>
				</div>
				<div class="col-md-12">
					<button class="btn btn-default" id="plotbutton">Plot Benchmark</button>
					<p>Push this button to save the benchmark just plot, it will be visible on the box just on the right</p>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</t:template>