$(document).ready(function() {

	/* 角色表 */
	$("#role_table").bootstrapTable({
		columns : [ {
			radio : true
		}, {
			field : "id",
			title : "ID"
		}, {
			field : "role",
			title : "角色编码"
		}, {
			field : "description",
			title : "角色"
		}, {
			field : "available",
			title : "状态",
			visible : false
		} ],
		clickToSelect : true,
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
		url : "/resource/system-role"
	});

});

/* 新增按钮弹窗 */
function addVideoShow() {
	var html = "<div id=\"layer_add\"><div class=\"row\">"
			+ "<label class=\"control-label col-xs-3 text-right\">角色名：</label><div class=\"col-xs-8\">"
			+ "<input name=\"description\" class=\"form-control\"></div></div><div class=\"row\">"
			+ "<label class=\"control-label col-xs-3 text-right\">角色编码：</label><div class=\"col-xs-8\">"
			+ "<input name=\"role\" class=\"form-control\"></div></div></div>";

	layer.open({
		title : "新增角色",
		type : 1,
		content : html,
		area : "600px",
		scrollbar : false,
		btn : [ "保存", "取消" ],
		yes : function(index, layero) {
			openLoad();
			var description = layero.find("input[name='description']").val();
			var role = layero.find("input[name='role']").val();
			$.ajax({
				url : "/system-role/add",
				data : {
					"description" : description,
					"role" : role
				},
				type : "post",
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					if (data.status == 200) {
						$("#role_table").bootstrapTable("refresh");
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

/* 修改按钮弹窗 */
function updateVideoShow() {
	var selections = $("#role_table").bootstrapTable("getSelections");
	if (selections.length != 1) {
		layerMsg(417, "请选择一条数据");
		return;
	}
	var role = selections[0];
	var html = "<div id=\"layer_update\"> <div class=\"row\"><label class=\"control-label col-xs-3 text-right\">ID：</label> <div class=\"col-xs-8\"><input name=\"id\" class=\"form-control\" value=\""
			+ role.id
			+ "\" readonly=\"readonly\"></div> </div><div class=\"row\"><label class=\"control-label col-xs-3 text-right\">角色名：</label> <div class=\"col-xs-8\"><input name=\"description\" class=\"form-control\" value=\""
			+ role.description
			+ "\"></div> </div> <div class=\"row\"><label class=\"control-label col-xs-3 text-right\">角色编码：</label> <div class=\"col-xs-8\"><input name=\"role\" class=\"form-control\" value=\""
			+ role.role + "\"></div> </div> </div>";

	layer.open({
		title : "新增角色",
		type : 1,
		content : html,
		area : "600px",
		scrollbar : false,
		btn : [ "保存", "取消" ],
		yes : function(index, layero) {
			openLoad();
			var description = layero.find("input[name='description']").val();
			$.ajax({
				url : "/system-role/update",
				data : {
					"description" : description,
					"id" : role.id
				},
				type : "post",
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					if (data.status == 200) {
						$("#role_table").bootstrapTable("refresh");
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

/* 删除按钮弹窗 */
function deleteVideoShow() {
	var selections = $("#role_table").bootstrapTable("getSelections");
	if (selections.length != 1) {
		layerMsg(417, "请选择一条数据");
		return;
	}
	var role = selections[0];
	layer.confirm("是否要删除角色【" + role.description + "】，以及其相关配置？", {
		icon : 3,
		title : '提示'
	}, function(index) {
		openLoad();
		$.ajax({
			url : "/system-role/delete",
			type : "post",
			data : {
				"id" : role.id
			},
			success : function(data) {
				closeLoad();
				layerMsg(data.status, data.cause);
				if (data.status == 200) {
					$("#role_table").bootstrapTable("refresh");
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

/* 权限设置弹窗 */
function permissionVideoShow() {
	var selections = $("#role_table").bootstrapTable("getSelections");
	if (selections.length != 1) {
		layerMsg(417, "请选择一条数据");
		return;
	}
	var role = selections[0];
	openLoad();
	layer.open({
		type : 2,
		title : [ "权限设置：" + role.description, "font-size:18px;" ],
		content : "/system-manage/system-role/permission/set/" + role.id,
		area : [ "630px", "580px" ],
		success : function(layero, index) {
			closeLoad();
		}
	});
}