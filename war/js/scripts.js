//VARIABLES
var currentCountries = [];
var lastbenchrmak = null;
var test = "test";
var measures = null;
var benchmarks = null;
var calculatedBenchmark = null;

var routeJson = null;

// UTILITY FUNCTIONS
// SELECTION
function selectedBenchmarkMenu(menu) {
	if (menu == "cbenchmark")
		return $('#cbenchmark').val();
	if (menu == 'disabledbenchmark')
		return $('#disabledbenchmark').val();
	if (menu == 'tempbenchmark')
		return $('#tempbenchmark').val();
}

function selectedBenchmark() {
	if ($.isEmptyObject(selectedBenchmarkMenu("cbenchmark")))
		return selectedBenchmarkMenu("disabledbenchmark");
	else
		return selectedBenchmarkMenu("cbenchmark");
}

function loading() {
	$('#loading_button').button('loading')
	disableInput();
}
function loaded() {
	$('#loading_button').button('reset')
	enableInput();

}

function customer() {
	return $('#customers').val();
}
function country() {
	return $('#country').val();
}
function day() {
	return $('#day').val();
}

// INPUT
function disableInput() {
	$("#day").multiselect('disable')
	$("#country").multiselect('disable')
	$("#customers").multiselect('disable')
	$("#benchmarks").multiselect('disable')
	$('.todisable').prop('disabled', true);

}
function enableInput() {
	$("#day").multiselect('enable')
	$("#country").multiselect('enable')
	$("#customers").multiselect('enable')
	$("#benchmarks").multiselect('enable')
	$('.todisable').prop('disabled', false);

}

function initBenchmarkMenus() {
	$("#benchmarks").multiselect('dataprovider', []);
}

// GRAPHS
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
		lang : {},
		/* Custom options */
		noData : {
			position : {},
			attr : {},
			style : {}
		}
	});

}

function plot(graph, id) {
	var suffix = "";
	if (graph == 'ACDA' || graph == 'ACDV') {
		suffix = " Seconds";
	} else if (graph == 'Buy' || graph == 'Sell' || graph == 'Revenue' || graph=="Gp") {
		suffix = " EUR Cent";
	} else {
		suffix = "";
	}

	var yserie = [];
	console.log(measures)
	$.each(measures, function(day, graphs) {
		var numbers = [];
		var days;
		console.log(day)
		if($('#day').val()=="0,1")
			days = ["0","1"];
		else{
			days = [$('#day').val()];
		}
		if ($.inArray(day, days) != -1) {
			for ( var g in graphs) {
				$.each(graphs[g], function(country, measuresData) {
					if ($('#country').val() == country.toString()) {
						numbers = [];
						for ( var m in measuresData) {
							var me = measuresData[m];
							numbers.push({
								x : me["Hour"],
								y : me[graph]
							});
						}
						yserie.push({
							name : country + customer() + " day "
									+ day,
							data : numbers
						});
					}
				});
			}
		}
		console.log(yserie)
		yserie.sort(function(a, b) {
			return a.x - b.x
		});

	});
	chart = $('#' + id).highcharts({
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
			position : {},
			attr : {},
			style : {}
		}

	});

};
function multiplot() {
	plot($('#sampleTabs>.active').text(), 'graph');
	plot('Minutes', 'minutes');
	plot('Revenue', 'revenue');
	plot('ACDV', 'acdv');
	plot('ARSV', 'arsv');
}

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

// CALLS
function bcall() {
	console.log("benchmark call")
	if (customer() != null && customer() != "none" && country() != null
			&& country() != "none") {

		var servlet = "/benchmark";
		var call = "?country=" + country() + "&customer=" + customer();
		var options_active = [];
		var options_disabled = [];

		initBenchmarkMenus();

		console.log(call);

		$.getJSON(servlet + call, function(json) {
			setTimeout(function() {
				// Clean old
				$.each($('#benchmakr_scope').children(), function(i, e) {
					e.remove();
				});

				$("#benchmakr_scope").append(
						'<optgroup id="alerted" label=Alerted></optgroup>');

				$.each(json["active_opt"], function(index, name) {
					$("#benchmakr_scope").children("#alerted").append(
							'<option value="' + name + '">' + name
									+ '</option>');
					options_active.push({
						label : name,
						value : name
					});
				});
				$("#benchmakr_scope").append(
						'<optgroup id="silenced" label=Silenced></optgroup>');

				$.each(json["disabled_opt"], function(index, name) {
					$("#benchmakr_scope").children("#silenced").append(
							'<option value="' + name + '">' + name
									+ '</option>');
					options_disabled.push({
						label : name,
						value : name
					});
				});

				$.each(json["toplot"], function(index, benchmark) {
					benchmarkPlot(benchmark['measures'], $(
							'#sampleTabs>.active').text(), 'graph');
				});

				$("#benchmarks").multiselect('dataprovider', options_active);
				$("#benchmarks").multiselect('dataprovider', options_disabled);

			}, 001);
		});

	}
}

// BUTTONS
function changeButtonText(id, val) {
	$('#' + id).html(val)
};

// ALERTING-CALLS
function markAsRead(id, button) {
	$.ajax({
		url : "/alert?id=" + id,
		type : 'GET',
		success : function() {
			$(button).parent().parent().parent().remove();
		}
	});
};

// USERS-CALLS
function makeAdmin(id, button) {
	$.ajax({
		url : "/user?admin=true&id=" + id,
		type : 'POST',

		success : function() {
			$(button).parent().parent().parent().remove();
			setTimeout(function() {
				location.reload();
			}, 0001);
		}
	});
};
function makeUser(id, button) {
	$.ajax({
		url : "/user?admin=false&id=" + id,
		type : 'POST',
		success : function() {
			$(button).parent().parent().parent().remove();
			setTimeout(function() {
				location.reload();
			}, 0001);
		}
	});
};
function upgrade(id, button) {
	$.ajax({
		url : "/user?up=true&id=" + id,
		type : 'POST',
		success : function() {
			$(button).parent().parent().parent().remove();
			setTimeout(function() {
				location.reload();
			}, 0001);
		}
	});
}
function downgrade(id, button) {
	$.ajax({
		url : "/user?down=true&id=" + id,
		type : 'POST',
		success : function() {
			$(button).parent().parent().parent().remove();
			setTimeout(function() {
				location.reload();
			}, 0001);
		}
	});
}

// ACTIONS
$(function() {

	$('#savebutton').click(
			function() {
				$.ajax({
					url : "/benchmark/calculate?customer=" + customer()
							+ "&country=" + country(),
					type : 'POST',
					success : function() {
						setTimeout(function() {
							bcall();
						}, 001);
					}
				});
				calculatedBenchmark = null;
			});

	$('#calcbutton').click(
			function() {

				var servlet = "/benchmark/calculate"
				var graph = $("ul#sampleTabs li.active").text();
				var call = "?day=" + day() + "&customer=" + customer()
						+ "&country=" + country();
				console.log(call)
				if (day() != null && customer() != null && country() != null) {
					setTimeout(function() {
						$.getJSON(servlet + call, function(json) {
							if (!$.isEmptyObject(json)) {
								calculatedBenchmark = json;
								console.log(json)
								benchmarkPlot(json, $('#sampleTabs>.active')
										.text(), 'graph');
							} else {
								console.log("No local benchmark");
								alert("No local benchmark, press calculate");
							}
						});
					}, 001);
				}
			});

});

// SELECT_MENU

function callToPlot() {
	if (country() != null && customer() != "none" && day() != "none") {
		loading();
		if (country() != null && customer() != null && day() != null) {
			setTimeout(function() {
				if (measures != null) {
					multiplot();
				}
				loaded();
			}, 001);

		}

	} else {
		return;
	}
}

function fillCountry() {
	console.log("fill");
	var options = [ {
		label : "Ready: None Selected",
		value : "none"
	} ];
	var optionsCheck = [];
	$.each(measures, function(day, arr) {
		if (day = $('#day').val()) {
			$.each(arr, function(index, data) {
				$.each(data, function(country, data) {
					if ($.inArray(country, optionsCheck) == -1) {
						optionsCheck.push(country);
						options.push({
							label : country,
							value : country
						});
					}
				});
			});
		}
	});
	setTimeout(function() {
		$("#country").multiselect('dataprovider', options)
		loaded();
	}, 005);
}

$(function() {

	$('#day').change(function() {
		var servlet = 'graph';
		var call = '?day=' + day() + '&customer=' + customer();

		if (customer() != "none" && country() != null && country != "none") {
			console.log(call);
			$.getJSON(servlet + call, function(jdata) {
				if (jQuery.isEmptyObject(jdata) || jdata["message"] != null) {
					console.log(jdata["message"]);
					$("#country").multiselect('disable')
					alert("No Data to plot");
					noPlot();
				} else {
					$("#country").multiselect('enable')
					measures = jdata;
					fillCountry();
				}
			});
		}

	});

	$('#customers').change(function() {
		var servlet = 'graph';
		var call = '?day=' + day() + '&customer=' + customer();

		if (day() != "none") {
			console.log(call);
			$.getJSON(servlet + call, function(jdata) {
				if (jQuery.isEmptyObject(jdata) || jdata["message"] != null) {
					console.log(jdata["message"]);
					$("#country").multiselect('disable');
					alert("No Data to plot");
					noPlot();
				} else {
					$("#country").multiselect('enable');
					measures = jdata;
					fillCountry();
				}
			});
		}

	});

	$('#country').change(function() {
		callToPlot();
		bcall();
	});

	$(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function(e) {
		var action = $('#sampleTabs>.active').text();
		if (action == "Benchmark") {

		} else if (country() != null && customer() != null && day() != null) {
			callToPlot();
			bcall();
		}

	});

	$('#benchmarks').change(
			function() {
				var servlet = "/benchmark";
				var benchmark = $(this).val();
				var call = "?id=" + benchmark + "&list=false";

				console.log(call)

				if (!$.isEmptyObject(benchmark)) {
					$.getJSON(servlet + call, function(jsonB) {
						benchmarkPlot(jsonB["measures"], $(
								'#sampleTabs>.active').text(), 'graph');
					});
				}
			});

});

function editBenchmark() {
	var active = $('#benchmark_active:checked').val();
	var alwaysPlot = $('#always_plot:checked').val();
	var id = $("#benchmakr_scope").val()
	$.ajax({
		url : "/benchmark/update?id=" + id + "&activation=" + active
				+ "&alwaysplot=" + alwaysPlot,
		type : 'POST',
		success : function() {
			bcall();
		}
	});

}
function deleteBenchmark() {
	var id = $("#benchmakr_scope").val()
	$.ajax({
		url : "/benchmark/delete?id=" + id,
		type : 'POST',
		success : function() {
			bcall();
		}
	});

}