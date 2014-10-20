<%@tag description="Admin Panel navigation bar"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>

<nav class="navbar navbar-default navbar-fixed-bottom" role="navigation">
	<div class="navbar-inner">
		<div class="container">
			<ul class=" nav navbar-nav navbar-left">
				<li><button class="btn btn-info"
						data-loading-text="Status:loading stuff..." id="loading_button">Status:Ready</button>
					<br></li>
			</ul>
			<ul class="nav navbar-nav">
				<li data-toggle="tooltip" data-placement="left"
					title="Day Selection"><select class="multiselect personal "
					name="day" id="day">
						<option value='8'>None Selected</option>
						<option value='0,1'>Today&#38Yesterday</option>
						<c:forEach var="i" begin="0" end="7">
							<option value="${i}"><t:day day="${i}"></t:day></option>
						</c:forEach>
				</select></li>
				<li data-toggle="tooltip" data-placement="left"
					title="Customers Selection"><select
					class="multiselect customers " name="customers" id="customers">
				</select></li>
				<li data-toggle="tooltip" data-placement="left"
					title="Countries Selection"><select
					class="multiselect personal " name="country" id="country">
				</select></li>
			</ul>

			<ul id="benchmark_menu"
				class=" nav navbar-nav navbar-right btn-group dropup">
				<!-- BENCHMARK -->
				<li data-toggle="tooltip" data-placement="left"
					title="Benchmarks" class="btn-group"><select
					class="multiselect personal  benchmark" name="benchmarks"
					id="benchmarks" multiple=true>
						<optgroup id="custormer_group" label="Customer"></optgroup>
						
						<optgroup id="avg_group" label="AVG"></optgroup>
				</select>
				<br></li>
				<li><button class="btn btn-default"
						 id="calcbutton">Calculate</button></li>
				<li><button class="btn btn-default"
						id="savebutton">Save</button></li>
			</ul>
			<!-- container -->
		</div>
	</div>
	<!-- navbar inner -->
</nav>


<!-- 

	<li data-toggle="tooltip" data-placement="left"
					title="Temp Benchmarks"class="btn-group">
					<select
					class="multiselect personal  benchmark"
					name="tempbenchmark" id="tempbenchmark"  multiple=true
					style="height: 69px;">
						<option>All</option>
				</select><t:benchmark_menu menu="tempbenchmark" /><br></li>


 -->
