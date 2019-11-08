$(document).ready(function() {

	/* QQ表格 */
	$("#qq_table").bootstrapTable({
		columns : [ {
			checkbox : true
		}, {
			field : "id",
			title : "QQ号"
		}, {
			field : "communityName",
			title : "QQ群名"
		}, {
			field : "qqType",
			title : "类型",
			formatter : qqTypeHtml
		} ],
		// clickToSelect : true,
		striped : true,
		pagination : true,
		sidePagination : "client",
		pageNumber : 1,
		pageSize : 10,
		paginationLoop : false,
		pageList : "unlimited",
		cache : false,
		search : true,
		searchAlign : "right",
		toolbar : "#toolbar",
		toolbarAlign : "left",
		showColumns : true,
		showRefresh : true,
		url : "/resource/qqCommunity"
	});

});

/* 新增按钮弹窗 */
function addVideoShow() {
	var html = "<div id=\"layer_add\"><div class=\"row\">"
			+ "<label class=\"control-label col-xs-3 text-right\">QQ号：</label><div class=\"col-xs-8\">"
			+ "<input name=\"id\" class=\"form-control\"></div></div><div class=\"row\">"
			+ "<label class=\"control-label col-xs-3 text-right\">QQ群名称：</label><div class=\"col-xs-8\">"
			+ "<input name=\"communityName\" class=\"form-control\"></div></div></div>";

	layer.open({
		title : "新增QQ信息",
		type : 1,
		content : html,
		area : "600px",
		scrollbar : false,
		btn : [ "保存", "取消" ],
		yes : function(index, layero) {
			openLoad();
			var id = layero.find("input[name='id']").val();
			var communityName = layero.find("input[name='communityName']")
					.val();
			$.ajax({
				url : "/qq-community/add",
				data : {
					"id" : id,
					"communityName" : communityName
				},
				type : "post",
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					if (data.status == 200) {
						$("#qq_table").bootstrapTable("refresh");
						layer.close(index);
					}
				},
				error : function(data) {
					closeLoad();
					layerMsg(500, "请求失败");
				}
			});
		}
	});
}

/**
 * 
 */
var qqTypeHtml = function(value, row, index) {
	if (value == "GROUP") {
		return "群";
	} else if (value == "FRIEND") {
		return "好友";
	} else {
		return "异常";
	}
}

/* 修改按钮弹窗 */
function updateVideoShow() {
	var selections = $("#qq_table").bootstrapTable("getSelections");
	if (selections.length > 1) {
		layerMsg(471, "修改操作只能选择一条信息");
	} else if (selections.length == 0) {
		layerMsg(417, "请选择一条信息");
	} else {
		var qqCommunity = selections[0];
		var html = "<div id=\"layer_add\"><div class=\"row\">"
				+ "<label class=\"control-label col-xs-3 text-right\">QQ号：</label><div class=\"col-xs-8\">"
				+ "<input name=\"id\" class=\"form-control\" readonly=\"readonly\" value=\""
				+ qqCommunity.id
				+ "\"></div></div><div class=\"row\">"
				+ "<label class=\"control-label col-xs-3 text-right\">QQ群名称：</label><div class=\"col-xs-8\">"
				+ "<input name=\"communityName\" class=\"form-control\" value=\""
				+ qqCommunity.communityName + "\"></div></div></div>";

		layer.open({
			title : "修改QQ信息",
			type : 1,
			content : html,
			area : "600px",
			scrollbar : false,
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				openLoad();
				var id = layero.find("input[name='id']").val();
				var communityName = layero.find("input[name='communityName']")
						.val();
				$.ajax({
					url : "/qq-community/update",
					data : {
						"id" : id,
						"communityName" : communityName
					},
					type : "post",
					success : function(data) {
						closeLoad();
						layerMsg(data.status, data.cause);
						if (data.status == 200) {
							$("#qq_table").bootstrapTable("refresh");
							layer.close(index);
						}
					},
					error : function(data) {
						closeLoad();
						layerMsg(500, "请求失败");
					}
				});
			}
		});
	}
}

/* 删除按钮弹窗 */
function deleteVideoShow() {
	var selections = $("#qq_table").bootstrapTable("getSelections");
	var id = "";
	if (selections.length == 0) {
		layerMsg(417, "请至少选择一条信息");
		return;
	} else {
		for (var int = 0; int < selections.length; int++) {
			var qqCommunity = selections[int];
			if (int == 0) {
				id = id + qqCommunity.id;
			} else {
				id = id + "," + qqCommunity.id;
			}
		}
	}
	layer.confirm("删除QQ信息后关联的配置也会一并删除", {
		icon : 3,
		title : '提示'
	}, function(index) {
		openLoad();
		$.ajax({
			url : "/qq-community/delete",
			type : "post",
			data : {
				"id" : id
			},
			success : function(data) {
				closeLoad();
				layerMsg(data.status, data.cause);
				if (data.status == 200) {
					$("#qq_table").bootstrapTable("refresh");
					layer.close(index);
				}
			},
			error : function(data) {
				closeLoad();
				layerMsg(500, "请求失败");
			}
		});
	});
}