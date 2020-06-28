/* 查询按钮 */
$("#btn_select").click(function() {
	var detailIds = $("#detailIds").val();
	location.href = "/data-visualization/taoba-visual?detailIds=" + detailIds;
});