var countries = [];
var customers = [];
$(document).ready(function() {

	var servlets = [ "customers", "countries" ];
	$.each(servlets, function(i, servlet) {
		$.getJSON("/" + servlet, function(json) {
			$.each(json, function(index, c) {
				if ("customers" == servlet) {
					customers.push(c);
				} else {
					countries.push(c);
				}
			});
		});
	});

});

$(function() {

	$("#newcountry").autocomplete({
		source : countries
	});
	$("#newcustomer").autocomplete({
		source : customers
	});

});
