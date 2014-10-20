<%@tag description="Table Line"%>
<%@attribute name="measure" required="true"
	type="it.golem.model.MCData"%>
<tr>
	<td>${measure.country}</td>
	<td>${measure.customer}</td>
	<td>${measure.minutes}</td>
	<td>${measure.ARSV}</td>
	<td>${measure.ACDV}</td>
	<td>${measure.ACDA}</td>
	<td>${measure.buy}</td>
	<td>${measure.sell}</td>
	<td>${measure.revenue}</td>
	<td>${measure.gp_percent}</td>
	<td>${measure.gp}</td>
	<td>${measure.delta}</td>
	<td>${measure.routing}</td>
	<td>${measure.on}</td>
	<td>${measure.block}</td>
</tr>
