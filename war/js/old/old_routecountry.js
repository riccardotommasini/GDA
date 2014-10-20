$(function() {
	$('#customers').change(
			function() {
				var customer = $(this).val();
				$.getJSON('/routes?customer=' + customer, function(j) {
					var options = '';
					for (var i = 0; i < j.length; i++) {
						options += '<option value="' + j[i].country + '">'
								+ j[i].country + '</option>';
					}
					$("select#country").html(options);
				});
			});
});
