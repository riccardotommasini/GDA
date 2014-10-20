function markAsRead(id, button) {
	$.ajax({
				url : "/alert?id="+id,
				type : 'GET',
				success : function() {
					$(button).parent().parent().parent().remove();
				}
			});
};