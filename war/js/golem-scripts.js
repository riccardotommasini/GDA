//VARIABLES
var currentCountries = [];
var lastbenchrmak = null;
var test = "test";
var measures;
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

function plot(jdata, country, customer, graph, day, id, suffix) {
	var suffix = "";
	if (graph == 'ACDA' || graph == 'ACDV') {
		suffix = " Seconds";
	} else if (graph == 'Buy' || graph == 'Sell') {
		suffix = " EUR Cent";

	} else if (graph == 'ARSV' || graph == 'GpPercent') {
		suffix = "%;"

	} else {

		suffix = " Minutes";
	}

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
				console.log(route);
				yserie.push({
					name :  customer +" "+ country +" day " + day,
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
			valueDecimals : 5,
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

function multiplot(jdata, country, customer, graph, day) {
	plot(jdata, country, customer, graph, day, 'graph');
	plot(jdata, country, customer, 'Minutes', day, 'minutes');
	plot(jdata, country, customer, 'Revenue', day, 'revenue');
	plot(jdata, country, customer, 'ACDV', day, 'acdv');
	plot(jdata, country, customer, 'ARSV', day, 'arsv');
}

function benchmarkPlot(benchmark, graph, html) {
	console.log(benchmark)
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
function bcall(country, customer) {

	var servlet = "/benchmark";
	var call = "?list=true&country=" + country + "&customer=" + customer;
	var options_active = [];
	var options_disabled = [];

	initBenchmarkMenus();

	console.log(call);

	$.getJSON(servlet + call, function(json) {
		setTimeout(function() {
			$.each(json["active_opt"], function(index, name) {
				options_active.push({
					label : name,
					value : name
				});
			});
			$.each(json["disabled_opt"], function(index, name) {
				options_disabled.push({
					label : name,
					value : name
				});
			});

			$.each(json["toplot"], function(index, benchmark) {
				benchmarkPlot(benchmark['measures'], $('#sampleTabs>.active')
						.text(), 'graph');
			});

			$("#benchmarks").multiselect('dataprovider', options_active);
			$("#benchmakr_scope").multiselect('dataprovider', options_active);

			$("#benchmarks").multiselect('dataprovider', options_disabled);

		}, 2000);
	});
}
function gcall(country, customer, graph, day) {
	var servlet = 'graph';
	var call = '?day=' + day + '&country=' + country + '&customer=' + customer;
	console.log(call);

	if (measures != null) {
		multiplot(measures, country, customer, graph, day);
	} else {
		$.getJSON(servlet + call, function(jdata) {
			console.log(jdata);
			if (jQuery.isEmptyObject(jdata) || jdata["message"] != null) {
				console.log(jdata["message"]);
				noPlot();
			} else {
				measures = jdata;
				multiplot(jdata, country, customer, graph, day);

			}
		});
	}
};

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

	$('#savebutton').click(function() {

		$.ajax({
			url : "/sliding?customer=" + customer() + "&country=" + country(),
			type : 'POST',
			success : function() {
				setTimeout(function() {
					bcall(country(), customer());
				}, 1000);
			}
		});
		calculatedBenchmark = null;
	});

	$('#calcbutton').click(
			function() {

				var servlet = "/sliding"
				var graph = $("ul#sampleTabs li.active").text();
				var call = "?day=" + day() + "&customer=" + customer()
						+ "&country=" + country();
				console.log(call)
				if (day() != null && customer() != null && country() != null) {
					$.getJSON(servlet + call, function(json) {
						if (!$.isEmptyObject(json)) {
							calculatedBenchmark = json;
							benchmarkPlot(calculatedBenchmark, $(
									'#sampleTabs>.active').text(), 'graph');
						} else {
							console.log("No local benchmark");
							alert("No local benchmark, press calculate");
						}
					});
				}
			});

});

// SELECT_MENU
$(function() {

	$('#day').change(function() {

		var days = [];

		days.push($(this).val());

		if (days == "0,1")
			days = [ "0", "1" ];

		if ($.isEmptyObject(day()))
			return;

		var call = '/routes?days=' + days
		console.log(call)
		loading();
		$.getJSON(call, function(j) {
			routeJson = j;
			var options = [ {
				label : "None Selected",
				value : "none"
			} ];
			var optionsCheck = [];
			$.each(days, function(index, d) {
				for (var i = 0; i < j.length; i++) {
					if (j[i].day == d) {
						if ($.inArray(j[i].customer, optionsCheck) == -1) {
							optionsCheck.push(j[i].customer);
							options.push({

								label : j[i].customer,
								value : j[i].customer
							})
						}

					}
				}

			});

			setTimeout(function() {
				$("#customers").multiselect('dataprovider', options)
				loaded();
			}, 005);

		}

		);

	});

	$('#customers')
			.change(
					function() {
						loading();
						if ($.isEmptyObject(customer()))
							return;

						if (routeJson == null) {

							$.getJSON('/routes?days=' + $('#day').val(),
									function(j) {
										routeJson = j;
										var options = '';
										j.sort(function(ja, jb) {
											return ja.order - jb.order
										});

										for (var i = 0; i < j.length; i++) {
											options.push({
												label : routeJson[i].country,
												value : routeJson[i].country
											})
										}
									});
						} else {
							var options = [];
							var optionsUnique = [];
							options.push({
								label : "None Selected",
								value : "none"
							})
							var days = [];
							days.push($('#day').val());
							if (days == "0,1")
								days = [ "0", "1" ];
							routeJson.sort(function(ja, jb) {
								return ja.order - jb.order
							});
							for (var i = 0; i < routeJson.length; i++) {
								$
										.each(
												days,
												function(index, d) {
													if (routeJson[i].day == d
															&& routeJson[i].customer == customer()
															&& (optionsUnique
																	.indexOf(routeJson[i].country) == -1)) {
														currentCountries
																.push(routeJson[i].country);
														optionsUnique
																.push(routeJson[i].country);
														options
																.push({
																	label : routeJson[i].country,
																	value : routeJson[i].country
																})
													}
												});
							}
						}
						setTimeout(function() {
							$("#country").multiselect('dataprovider', options);
							loaded();
						}, 001);

					});

	$('#country').change(
			function() {

				loading();
				measures = null;
				if (country() != null && customer() != null && day() != null) {
					setTimeout(function() {
						gcall(country(), customer(), $('#sampleTabs>.active')
								.text(), day());
						bcall(country(), customer());
						loaded();
					}, 001);

				}

			});

	$(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function(e) {
		var action = $('#sampleTabs>.active').text();
		if (action == "Benchmark") {

		} else if (country() != null && customer() != null && day() != null) {
			gcall(country(), customer(), action, day());
			bcall(country(), customer());
		}

	});

	$('#benchmarks').change(
			function() {

				measures = null;
				var servlet = "/benchmark";
				var benchmark = $(this).val();

				var call = "?benchmark=" + benchmark + "&list=false";

				console.log(call)

				if (!$.isEmptyObject(benchmark)) {
					$.getJSON(servlet + call, function(jsonB) {
						console.log(jsonB)
						buttonChange(jsonB, $(this).attr('ref'));
						benchmarkPlot(jsonB["measures"], $(
								'#sampleTabs>.active').text(), 'graph');

					});
				}
			});

});

function buttonChange(jb, ref) {
	if (jb["Alwaysplot"])
		changeButtonText("alwaysbutton_" + ref, "Always");
	else
		changeButtonText("alwaysbutton", "OnDemand");
}
