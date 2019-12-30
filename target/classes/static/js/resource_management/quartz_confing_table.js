$(document).ready(function() {
	/* 轮询配置表格 */
	$("#quartz_confing_table").bootstrapTable({
		columns : [ {
			radio : true
		}, {
			field : "id",
			title : "ID",
			visible : false
		}, {
			field : "jobName",
			title : "定时任务"
		}, {
			field : "jobDesc",
			title : "任务说明"
		}, {
			field : "cron",
			title : "轮询规则"
		}, {
			field : "classPath",
			title : "任务类"
		}, {
			field : "status",
			title : "操作",
			formatter : operateHtml
		} ],
		clickToSelect : true,
		striped : true,
		pagination : true,
		sidePagination : "client",
		pageNumber : 1,
		pageSize : 15,
		pageList : [ 15, 25 ],
		paginationLoop : false,
		cache : false,
		search : true,
		searchAlign : "right",
		toolbar : "#toolbar",
		toolbarAlign : "left",
		showColumns : true,
		showRefresh : true,
		url : "/resource/quartz-config"
	});
})

/* 修改按钮弹窗 */
function updateVideoShow() {
	var selections = $("#quartz_confing_table").bootstrapTable("getSelections");
	if (selections.length > 1) {
		layerMsg(417, "修改操作只能选择一条信息");
	} else if (selections.length == 0) {
		layerMsg(417, "请选择一条信息");
	} else {
		var id = selections[0].id;
		layer.open({
			title : "轮询配置",
			type : 2,
			content : "/resource-management/quartz-confing-table/edit/" + id,
			area : [ "400px", "360px" ],
			maxmin : true,
			btn : [ "保存", "取消" ],
			yes : function(index, layero) {
				openLoad();
				var body = layer.getChildFrame('body', index);
				var jobName = body.find("input[name='jobName']").val();
				var jobDesc = body.find("textarea[name='jobDesc']").val();
				var cron = body.find("input[name='cron']").val();
				var classPath = body.find("input[name='classPath']").val();
				$.ajax({
					url : "/quartz-config/update",
					data : {
						"id" : id,
						"jobName" : jobName,
						"jobDesc" : jobDesc,
						"cron" : cron,
						"classPath" : classPath
					},
					type : "post",
					success : function(data) {
						closeLoad();
						layerMsg(data.status, data.cause);
						if (data.status == 200) {
							$("#quartz_confing_table").bootstrapTable("refresh");
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

/**
 * 操作列处理
 * 
 * @param {*}
 *            value
 * @param {*}
 *            row
 * @param {*}
 *            index
 */
var operateHtml = function(value, row, index) {
	var buttonStr;
	if (value) {
		buttonStr = "<button type=\"button\" class=\"btn btn-theme04\" onclick=\"shutdownJob(this, " + row.id + ")\">关闭</button>";
	} else {
		buttonStr = "<button type=\"button\" class=\"btn btn-success\" onclick=\"startJob(this, " + row.id + ")\">启动 </button>";
	}
	return buttonStr;
}

/**
 * 启动定时任务
 * 
 * @param btn
 *            当前节点
 * @param id
 *            任务ID
 */
function startJob(btn, id) {
	openLoad();
	$.ajax({
		url : "/quartz-config/start",
		data : {
			"id" : id
		},
		type : "post",
		success : function(data) {
			closeLoad();
			layerMsg(data.status, data.cause);
			if (data.status == 200) {
				var parent = btn.parentNode;
				btn.remove();
				var newbtn = "<button type=\"button\" class=\"btn btn-theme04\" onclick=\"shutdownJob(this, " + id + ")\">关闭</button>";
				parent.innerHTML = newbtn;
			}
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
}

/**
 * 关闭定时任务
 * 
 * @param btn
 *            当前节点
 * @param id
 *            任务ID
 */
function shutdownJob(btn, id) {
	openLoad();
	$.ajax({
		url : "/quartz-config/shutdown",
		data : {
			"id" : id
		},
		type : "post",
		success : function(data) {
			closeLoad();
			layerMsg(data.status, data.cause);
			if (data.status == 200) {
				var parent = btn.parentNode;
				btn.remove();
				var newbtn = "<button type=\"button\" class=\"btn btn-success\" onclick=\"startJob(this, " + id + ")\">启动</button>";
				parent.innerHTML = newbtn;
			}
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
}

/**
 * 新增按钮弹窗
 */
function addVideoShow() {
	layer.open({
		title : "轮询配置",
		type : 2,
		content : "/resource-management/quartz-confing-table/edit",
		area : [ "400px", "360px" ],
		maxmin : true,
		btn : [ "保存", "取消" ],
		yes : function(index, layero) {
			openLoad();
			var body = layer.getChildFrame('body', index);
			var jobName = body.find("input[name='jobName']").val();
			var jobDesc = body.find("textarea[name='jobDesc']").val();
			var cron = body.find("input[name='cron']").val();
			var classPath = body.find("input[name='classPath']").val();
			$.ajax({
				url : "/quartz-config/add",
				data : {
					"jobName" : jobName,
					"jobDesc" : jobDesc,
					"cron" : cron,
					"classPath" : classPath
				},
				type : "post",
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					if (data.status == 200) {
						$("#quartz_confing_table").bootstrapTable("refresh");
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
 * 删除按钮弹窗
 */
function deleteVideoShow() {
	var selections = $("#quartz_confing_table").bootstrapTable("getSelections");
	var id = selections[0].id;
	var jobName = selections[0].jobName;
	layer.confirm("是否要删除【" + jobName + "】定时任务", {
		icon : 3,
		title : '提示'
	}, function(index) {
		openLoad();
		$.ajax({
			url : "/quartz-config/delete",
			type : "post",
			data : {
				"id" : id
			},
			success : function(data) {
				closeLoad();
				layerMsg(data.status, data.cause);
				if (data.status == 200) {
					$("#quartz_confing_table").bootstrapTable("refresh");
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