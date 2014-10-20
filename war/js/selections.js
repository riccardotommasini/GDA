var currentCountries = [];
$(function() {

	$('#day').change(function() {
		var days = [];
		if (test == test)
			test = "funziona"
		days.push($(this).val());
		if (days == "0,1")
			days = [ "0", "1" ];
		$('#loading').show();
		disableInput();
		$.getJSON('/routes?days=' + $(this).val(), function(j) {
			routeJson = j;
			var options = [];
			var optionsCheck = [];
			$.each(days, function(index, d) {
				for (var i = 0; i < j.length; i++) {
					if (j[i].day == d) {
						if ($.inArray(j[i].customer, optionsCheck) == -1) {
							optionsCheck.push(j[i].customer);
						}

					}
				}

			});
			optionsCheck.sort();
			$.each(optionsCheck, function(index, customer) {
				options.push({
					label : customer,
					value : customer
				})
			});
			setTimeout(function() {
				enableInput();
				$('#loading').hide();
				$("#customers").multiselect('dataprovider', options)
			}, 2000);

		}

		);

	});

	$('#customers').change(
			function() {
				var customer = $(this).val();
				$('#loading').show();
				disableInput();
				if (routeJson == null) {
					$.getJSON('/routes?days=' + $('#day').val(), function(j) {
						routeJson = j;
						var options = '';
						j.sort(function(ja, jb) {
							return ja.order - jb.order
						});

						for (var i = 0; i < j.length; i++) {
							options += '<option value="' + j[i].country + '">'
									+ j[i].country + '</option>';
						}
					});
				} else {
					var options = [];
					var days = [];
					days.push($('#day').val());
					if (days == "0,1")
						days = [ "0", "1" ];
					routeJson.sort(function(ja, jb) {
						return ja.order - jb.order
					});
					for (var i = 0; i < routeJson.length; i++) {
						$.each(days, function(index, d) {
							if (routeJson[i].day == d
									&& routeJson[i].customer == customer) {
								currentCountries.push(routeJson[i].country);
								options.push({
									label : routeJson[i].country,
									value : routeJson[i].country
								})
							}
						});
					}
				}
				setTimeout(
						function() {
							enableInput();
							$('#loading').hide();
							$("#country").multiselect('dataprovider', options)
						}, 2000);

			});
	$('#country').change(function() {
		$('#calcbutton').prop('disabled', false);
		$('#savebutton').prop('disabled', false);
		measures = null;
		if (country() != null && customer() != null && day() != null) {
			gcall(country(), customer(), graph(), day());
			bcall(country(), customer());
		}

	});

	$(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function(e) {
		if (country() != null && customer() != null && day() != null) {
			gcall(country(), customer(), graph(), day());
			bcall(country(), customer());
		}

	});
	$('#cbenchmark').change(function() {
		$('#savebutton').prop('disabled', false);
		measures = null;
		console.log("/benchmark?benchmark=" + selectedBenchmark())
		$.ajax({
			url : "/benchmark?benchmark=" + selectedBenchmark(),
			type : 'POST',
			success : function(jb) {
				if (jb["Alwaysplot"])
					changeButtonText("alwaysbutton", "Always");
				else
					changeButtonText("alwaysbutton", "OnDemand");
				if (jb["Active"])
					changeButtonText("activationbutton", "Enabled");
				else
					changeButtonText("activationbutton", "Disabled");
			}
		});

	});
	$('#disabledbenchmark').change(function() {
		$('#savebutton').prop('disabled', true);
		measures = null;
		$.ajax({
			url : "/benchmark?benchmark=" + selectedBenchmark(),
			type : 'POST',
			success : function(jb) {
				
			}
		});

	});

});
function selectedBenchmark() {
	if ($('#cbenchmark > option:selected').text() == "")
		return $('#disabledbenchmark > option:selected').text();
	if ($('#disabledbenchmark > option:selected').text() == "")
		return $('#cbenchmark > option:selected').text();
}

function customer() {
	return $('#customers').val();
}
function country() {
	return $('#country').val();
}
function graph() {
	return $('#sampleTabs>.active').text();
}

function day() {
	return $('#day').val();
}

function nextCountry() {
	if (currentCountries.indexOf(country()) < currentCountries.length - 1)
		return currentCountries[currentCountries.indexOf(country()) + 1];
	else
		return country();
}
function prevCountry() {
	if (currentCountries.indexOf(country()) > 0)
		return currentCountries[currentCountries.indexOf(country()) - 1];
	else
		return country();
}

function disableInput() {
	$("#day").multiselect('disable')
	$("#country").multiselect('disable')
	$("#customers").multiselect('disable')
	$("#cbenchmark").multiselect('disable')
	$("#disabledbenchmark").multiselect('disable')

}
function enableInput() {
	$("#day").multiselect('enable')
	$("#country").multiselect('enable')
	$("#customers").multiselect('enable')
	$("#cbenchmark").multiselect('enable')
	$("#disabledbenchmark").multiselect('enable')

}