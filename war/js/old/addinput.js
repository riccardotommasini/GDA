var num =1;
$(document).ready(function() {
	var removebutton = '<div class="col-md-3"><button type="button" id="removebutton"onClick=remover($(this).parent().parent()) class="remove btn btn-md btn-danger">Rimuovi campo</button></div>'
		$("#addbutton").click(function() {
		var count= $("#attrgroup").children().size();
		var newline = $("#attrline"+num).clone();
		var select =$("#attrline"+num+" > select").clone();
		num++;

		newline.attr("id", "attrline"+(1+count));
		newline.find("input").attr("name", "value"+(1+count));
		newline.find("input").attr("id", "value"+(1+count));
		newline.find("select").attr("name", "value"+(1+count));
		newline.find("select > option:selected").remove();
		if(num==2)
			newline.append(removebutton)
		$("#attrgroup").append(newline);
	});
	

});

$('.select').change(function() {
	$("#"+$(this)[0].name).attr("name", $(this).val());
});

function remover(p){
	var parent = p[0];
	if(parent.id!="attrline1")
		
		parent.remove();
	num--;
}