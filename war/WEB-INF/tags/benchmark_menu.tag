<%@tag description="Benchmark Menu"%>
<%@ tag language="java" pageEncoding="US-ASCII"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@attribute name="menu" required="true"%>

<div name="benchmark_menu_${menu}" "id="benchmark_menu_${menu}"
	class=" nav navbar-nav navbar-right btn-group dropup">
	<a href="#" class="btn btn-default dropdown-toggle"
		data-toggle="dropdown"><b class="caret"></b></a>
	<ul class="multiselect-container dropdown-menu">
		<c:choose>
			<c:when test="${menu eq 'tempbenchmark' }">
				<li><a class="btn btn-default savebutton todisable"
					data-loading-text="loading stuff..." id="savebutton_${menu}"
					ref="${menu}">Save Benchmark</a></li>
			</c:when>
			<c:when test="${menu eq 'cbenchmark' }">
				<li><a class="btn btn-default alwaysbutton todisable"
					data-toggle="tooltip" data-placement="left"
					title="Click to set plotting frequency: Always or on-demand"
					id="alwaysbutton_${menu}" ref="${menu}">PlotSatus</a></li>
				<li><a class="btn btn-default activationbutton todisable"
					data-toggle="tooltip" data-placement="left"
					title="click to disable alerting" id="activationbutton_${menu}"
					ref="${menu}">Disable</a></li>
			</c:when>
			<c:otherwise>
				<li><a class="btn btn-default alwaysbutton todisable"
					data-toggle="tooltip" data-placement="left"
					title="Click to set plotting frequency: Always or on-demand"
					id="alwaysbutton_${menu}" ref="${menu}">PlotStatus</a></li>
				<li><a class="btn btn-default activationbutton todisable"
					data-toggle="tooltip" data-placement="left"
					title="Click to enable alerting" id="activationbutton_${menu}"
					ref="${menu}">Enable</a></li>
			</c:otherwise>
		</c:choose>

	</ul>
</div>

