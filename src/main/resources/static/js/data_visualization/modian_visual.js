/* 查询按钮 */
$("#btn_select").click(function() {
	var projectIds = $("#projectIds").val();
	location.href = "/data-visualization/modian-visual?projectIds=" + projectIds;
});