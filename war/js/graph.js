//measures;

function benchmarkPlot(benchmark, graph, html) {
	if (graph != "Multiplot") {
		var chart = $('#' + html).highcharts();
		var serie = [];
		for ( var m in benchmark) {
			var me = benchmark[m];
			serie.push({
				x : me["Hour"],
				y : me[graph]
			});
		}
		serie.sort(function(a, b) {
			return a.x - b.x
		});

		chart.addSeries({
			name : "benchmark",
			data : serie
		});
	} else {
		benchmarkPlot(benchmark, 'Minutes', 'minutes')
		benchmarkPlot(benchmark, 'Revenue', 'revenue')
		benchmarkPlot(benchmark, 'ACDV', 'acdv')
		benchmarkPlot(benchmark, 'ARSV', 'arsv')
	}
}

function bcall(country, customer) {
	$("cbenchmark").multiselect('dataprovider', []);
	$("#disabledbenchmark").multiselect('dataprovider', []);
	var servlet = "/benchmark";
	var call = "?list=true&country=" + country + "&customer=" + customer;
	console.log(call);
	$('#loading').show();
	$("#country_select").find("select").attr("disabled", true);
	$("#customer_select").find("select").attr("disabled", true);
	$("#day_select").find("select").attr("disabled", true)
	$.getJSON(servlet + call, function(json) {
		var options_active = [];
		var options_disabled = [];
		$.each(json["active_opt"],
						function(index, name) {
							options_active.push({label:name, value:name});
						});
		$.each(json["disabled_opt"],
						function(index, name) {
							options_disabled.push({label:name, value:name});
						});
		setTimeout(function() {
			$("#country_select").find("select").attr("disabled", false);
			$("#customer_select").find("select").attr("disabled", false);
			$("#day_select").find("select").attr("disabled", false);
			$("cbenchmark").multiselect('dataprovider', options_active);
			$("#disabledbenchmark").multiselect('dataprovider', options_disabled);
			$('#loading').hide();
		}, 2000);
		var jarrBenchPlot = json["toplot"];
		$.each(jarrBenchPlot, function(index, benchmark) {
			benchmarkPlot(benchmark['measures'], graph(), 'graph');
		});

	});
}
function gcall(country, customer, graph, day) {
	var servlet = 'graph';
	var call = '?day=' + day + '&country=' + country + '&customer=' + customer;
	console.log(call);
	$("#country_select").find("select").attr("disabled", true);
	$("#customer_select").find("select").attr("disabled", true);
	$("#day_select").find("select").attr("disabled", true)
	if (measures != null) {
		multiplot(measures, country, customer, graph, day);
	} else {
		$.getJSON(servlet + call, function(jdata) {
			if (jQuery.isEmptyObject(jdata) || jdata["message"] != null) {
				console.log(jdata["message"]);
				noPlot();
			} else {
				measures = jdata;
				multiplot(jdata, country, customer, graph, day);

			}
		});
	}
	$("#country_select").find("select").attr("disabled", false);
	$("#customer_select").find("select").attr("disabled", false);
	$("#day_select").find("select").attr("disabled", false);
};

function multiplot(jdata, country, customer, graph, day) {
	plot(jdata, country, customer, graph, day, 'graph');
	plot(jdata, country, customer, 'Minutes', day, 'minutes');
	plot(jdata, country, customer, 'Revenue', day, 'revenue');
	plot(jdata, country, customer, 'ACDV', day, 'acdv');
	plot(jdata, country, customer, 'ARSV', day, 'arsv');
}

function plot(jdata, country, customer, graph, day, id, suffix) {
	var suffix = "";
	if (graph == 'ACDA' || graph == 'ACDV' || graph == 'ARSV') {
		suffix = " Seconds";
	} else if (graph == 'Buy' || graph == 'Sell') {
		suffix = " EUR Cent";

	} else {
		suffix = " Minutes";
	}
	/*
	 * { name : 'Alert Line', data : Array.apply(null, new
	 * Array(24)).map(function() { return 150; }) }
	 */

	var yserie = [];

	$.each(jdata, function(day, graphs) {
		var numbers = [];
		for ( var g in graphs) {
			$.each(graphs[g], function(route, measures) {
				numbers = [];
				for ( var m in measures) {
					var me = measures[m];
					numbers.push({
						x : me["Hour"],
						y : me[graph]
					});
				}
				yserie.push({
					name : route + " day " + day,
					data : numbers
				});
			});
		}

		yserie.sort(function(a, b) {
			return a.x - b.x
		});

	});
	$('#' + id).highcharts({
		chart : {
			borderColor : '#2f7ed8',
			borderWidth : 3,
			type : 'line'
		},
		title : {
			text : 'Daily ' + id,
			x : -20
		},
		subtitle : {
			text : 'Source: Golem srl',
			x : -20
		},
		xAxis : {
			categories : [ '0', '1', '2' ]
		},
		yAxis : {
			title : {
				text : id
			},
			plotLines : [ {
				value : 0,
				width : 1,
				color : '#808080'
			} ]
		},
		plotOptions : {
			line : {
				dataLabels : {
					enabled : false,
					style : {
						textShadow : '0 0 3px white, 0 0 3px white'
					}
				},
				enableMouseTracking : true
			}
		},
		tooltip : {

			valueSuffix : suffix
		},
		series : yserie,
		noData : {
			// Custom positioning/aligning options
			position : {
			},
			attr : {
			},
			style : {
			}
		}

	});

};

function noPlot() {
	$('#graph').highcharts({
		title : {
			text : 'No data in line chart'
		},
		series : [ {
			type : 'line',
			name : 'Random data',
			data : []
		} ],
		lang : {
		// Custom language option
		// noData: "Nichts zu anzeigen"
		},
		/* Custom options */
		noData : {
			// Custom positioning/aligning options
			position : {
			// align: 'right',
			// verticalAlign: 'bottom'
			},
			// Custom svg attributes
			attr : {
			// 'stroke-width': 1,
			// stroke: '#cccccc'
			},
			// Custom css
			style : {
			// fontWeight: 'bold',
			// fontSize: '15px',
			// color: '#202030'
			}
		}
	});

}
