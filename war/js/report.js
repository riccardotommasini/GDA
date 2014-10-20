$(function() {

	$(document).on('shown.bs.tab', 'a[data-toggle="tab"]', function(e) {
		var day = $('#day').val();	
		if ($(this).text() == "Report") {
			rcall(day);
		}

	});

	$('#day').change(function() {
		if ($("ul#sampleTabs li.active").text() == "Report") {
			var day = $('#day').val();
			rcall(day);
		}
	});

});

function rcall(day) {
	var servlet = 'events/email';
	var call = '?day=' + day;
	console.log(call)
	if (day != null) {
		console.log(call);
		$.getJSON(servlet + call, function(jdata) {
			report(jdata);
		});
	}
}

function report(jdata) {

	var contentHead = '<div class="col-md-3 alert">';
	var contentTail = '</div>';
	var content = "<h3>" + jdata.length + " Mail were received</h3>";
	for ( var m in jdata) {
		var j = jdata[m];
		var sender = j['Sender'];
		var date = j['Recevied'];
		var filename = j['FileName'];
		var lineNumber = j['LineNumber'];
		var alerts = j['Alerts'].length;
		content = content + contentHead + "<p>The Email Received at " + date
				+ " with title " + '<a href=/download?file='+filename+">"+filename+"</a> from " + sender + " had "
				+ lineNumber + " number of lines and produce " + alerts
				+ " alerts</p>" + contentTail;
	}
	$("#reportdiv").html(content);

};