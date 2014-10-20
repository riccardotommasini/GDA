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
			console.log( "/user?up=true&id=" + id)
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