$(function() {
	$('#savebutton')
			.click(	function() {
						$(this).prop('disabled', true);
						var date = new Date();
						var customer = $('#customers').val();
						var country = $('#country').val();
						var twoDigitMonth = ((date.getMonth().length + 1) === 1) ? (date)
								: '0' + (date.getMonth() + 1);
						var dateName = date.getFullYear() + "" + twoDigitMonth
								+ "" + date.getDate()

						var name = dateName + "_" + customer + "_" + country
								+ "_benchmark";
						$('#loading').show();
						disableInput();
						$.ajax({
							url : "/sliding?customer=" + customer + "&country="
									+ country,
							type : 'POST',
							success : function() {
								setTimeout(function() {
									$('#loading').hide();
									enableInput();
									bcall(country, customer);
								}, 1000);
							}
						});
						calculatedBenchmark = null;
					});

	$('#calcbutton')
			.click(
					function() {
						$(this).prop('disabled', true); // disattiva il bottone
						var servlet = "/sliding"
						var day = $('#day').val();
						var customer = $('#customers').val();
						var country = $('#country').val();
						var graph = $("ul#sampleTabs li.active").text();
						var call = "?day=" + day + "&customer=" + customer
								+ "&country=" + country;
						console.log(call)
						if (day != null && customer != null && country != null) {
							$('#loading').show();
							$
									.getJSON(
											servlet + call,
											function(json) {
												calculatedBenchmark = json;
												var date = new Date();
												var twoDigitMonth = ((date
														.getMonth().length + 1) === 1) ? (date)
														: '0'
																+ (date
																		.getMonth() + 1);
												var dateName = date
														.getFullYear()
														+ ""
														+ twoDigitMonth
														+ "" + date.getDate()
												var name = dateName + "_"
														+ customer + "_"
														+ countrys;
												setTimeout(	function() {
															$('#loading').hide();
															$("#cbenchmark")
																	.multiselect(
																			'dataprovider',
																			[ {
																				label : "TEMP_"
																						+ name,
																				value : name
																			} ])

														}, 2000);
											});
						}
					});

	$('#plotbutton').click(
			function() {
				var toplot = $('#cbenchmark > option:selected').text();
				var graph = $("ul#sampleTabs li.active").text();

				if (toplot.search("TEMP") != -1) {
					if (calculatedBenchmark != null
							&& !$.isEmptyObject(calculatedBenchmark)) {
						benchmarkPlot(calculatedBenchmark, graph, 'graph');
					} else {
						console.log("No local benchmark");
						alert("No local benchmark, press calculate");
					}

				} else {
					var benchmark = $('#cbenchmark > option:selected').text();
					var servlet = "/benchmark";
					var call = "?benchmark=" + benchmark + "&list=false";

					$.getJSON(servlet + call, function(jsonB) {
						if (jsonB["Alwaysplot"])
							changeButtonText("defaultbutton", "AlwaysPlot");
						else
							changeButtonText("defaultbutton", "OnDemand");
						if (jsonB["Active"])
							changeButtonText("activationbutton", "Enabled");
						else
							changeButtonText("activationbutton", "Disabled");
						benchmarkPlot(jsonB["measures"], graph, 'graph');
					});
				}
			});
	$('#alwaysbutton')
			.click(
					function() {
						var customer = $('#customers').val();
						var country = $('#country').val();
						var benchmark = selectedBenchmark();
						$
								.ajax({
									url : "/benchmark?alwaysplot=true&activation=false&benchmark="
											+ benchmark,
									type : 'POST',
									success : function(jb) {
										bcall(country, customer);
									}
								});
					});
	$('#activationbutton')
			.click(
					function() {
						var customer = $('#customers').val();
						var country = $('#country').val();
						var benchmark = selectedBenchmark();
						$
								.ajax({
									url : "/benchmark?activation=true&alwaysplot=false&benchmark="
											+ benchmark,
									type : 'POST',
									success : function(jb) {
										bcall(country, customer);
									}
								});
					});

});


function changeButtonText(id, val) {
	$('#' + id).html(val)
};

