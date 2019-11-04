var permissionTree;
var updateLayer;

$(document).ready(function() {

	/* 资源树 */
	permissionTree = $.fn.zTree.init($("#permissionTree"), {
		// zTree 的参数配置
		async : {
			enable : true,
			dataType : "text",
			type : "get",
			url : "/resource/system-permission",
			dataFilter : function(treeId, parentNode, responseData) {
				return responseData.data;
			}
		},
		data : {
			key : {
				name : "name", // 节点显示的值
				url : "xUrl"
			},
			simpleData : {
				enable : true,// 如果为true，可以直接把从数据库中得到的List集合自动转换为Array格式。而不必转换为json传递
				idKey : "id",// 节点的id
				pIdKey : "parentId",// 节点的父节点id
				rootPId : -1
			}
		},
		callback : {
			onClick : zTreeOnClick
		}
	});

	/* 权限资源表 */
	$("#permission_table").bootstrapTable({
		uniqueId : "id",
		columns : [ {
			radio : true
		}, {
			field : "id",
			title : "ID"
		}, {
			field : "permission",
			title : "资源编码"
		}, {
			field : "name",
			title : "资源名称"
		}, {
			field : "resourceType",
			title : "类型",
			formatter : function(value, row, index) {
				var resourceType;
				if (row.resourceType == "menu") {
					resourceType = "菜单";
				} else if (row.resourceType == "button") {
					resourceType = "按钮";
				}
				return resourceType;
			}
		}, {
			field : "url",
			title : "URL接口"
		}, {
			field : "parentId",
			title : "父资源ID"
		}, {
			field : "available",
			title : "状态",
			visible : false
		} ],
		clickToSelect : true,
		striped : true,
		paginationLoop : false,
		pagination : true,
		sidePagination : "client",
		pageNumber : 1,
		pageSize : 9,
		pageList : "unlimited",
		cache : false,
		search : true,
		searchAlign : "right",
		toolbar : "#toolbar",
		toolbarAlign : "left",
		showColumns : true,
		showRefresh : true,
		url : "/resource/system-permission"
	});

});

/* 点击树节点，刷新右侧table */
function zTreeOnClick(event, treeId, treeNode) {
	$("#permission_table").bootstrapTable("refresh", {
		query : {
			"id" : treeNode.id,
			"pid" : treeNode.id
		}
	});
};

/* 刷新树和表格 */
function refreshTreeAndTable() {
	$("#permission_table").bootstrapTable("refresh");
	permissionTree.reAsyncChildNodes(null, "refresh", true);
}

/* 新增按钮弹窗 */
function addVideoShow() {
	var nodes = permissionTree.getSelectedNodes();
	if (nodes.length != 1) {
		layerMsg(417, "请在左侧选择一个MENU作为父节点");
		return;
	}
	var html = "<div id=\"layer_add\"> <div class=\"row\"><label class=\"control-label col-lg-3 text-right\">资源编码：</label> <div class=\"col-lg-9\"><input name=\"permission\" class=\"form-control\" value=\"\"></div> </div> <div class=\"row\"><label class=\"control-label col-lg-3 text-right\">资源名称：</label> <div class=\"col-lg-9\"><input name=\"name\" class=\"form-control\" value=\"\"></div> </div> <div class=\"row\"><label class=\"control-label col-lg-3 text-right\">资源类型：</label> <div class=\"col-lg-9\"> <select name=\"resourceType\" class=\"form-control\"> <option value=\"menu\">菜单</option> <option value=\"button\">按钮</option> </select> </div> </div> <div class=\"row\"><label class=\"control-label col-lg-3 text-right\">URL接口：</label> <div class=\"col-lg-9\"><input name=\"url\" class=\"form-control\" value=\"\"></div> </div> <div class=\"row\"><label class=\"control-label col-lg-2 text-right\">父资源ID：</label> <div class=\"col-lg-2\"><input name=\"parentId\" class=\"form-control\" value=\""
			+ nodes[0].id
			+ "\" disabled=\"disabled\"></div> <label class=\"control-label col-lg-2 text-right\">父资源名称：</label> <div class=\"col-lg-6\"><input name=\"parentName\" class=\"form-control\" value=\""
			+ nodes[0].name + "\" disabled=\"disabled\"></div> </div> </div>";

	layer
			.open({
				title : "新增资源",
				type : 1,
				content : html,
				area : "650px",
				scrollbar : false,
				btn : [ "保存", "取消" ],
				yes : function(index, layero) {
					openLoad();
					var permission = layero.find("input[name='permission']")
							.val();
					var name = layero.find("input[name='name']").val();
					var resourceType = layero.find(
							"select[name='resourceType']").val();
					var url = layero.find("input[name='url']").val();
					var parentId = layero.find("input[name='parentId']").val();
					$.ajax({
						url : "/system-permission/add",
						data : {
							"permission" : permission,
							"resourceType" : resourceType,
							"parentId" : parentId,
							"url" : url,
							"name" : name
						},
						type : "post",
						success : function(data) {
							closeLoad();
							layerMsg(data.status, data.cause);
							if (data.status == 200) {
								refreshTreeAndTable();
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
	var selections = $("#permission_table").bootstrapTable("getSelections");
	if (selections.length != 1) {
		layerMsg(417, "请选择一条数据");
		return;
	}
	openLoad();
	var permission = selections[0];
	var parentId = null;
	var parentName = null;
	if (permission.parentId != null) {
		$.ajax({
			url : "../resource/system-permission",
			async : false,
			data : {
				"id" : permission.parentId
			},
			type : "get",
			success : function(data) {
				var arr = data.data;
				parentId = arr[0].id;
				parentName = arr[0].name;
			},
			error : function(data) {
				closeLoad();
				layerMsg(500, "请求失败");
			}
		});
	}
	var html = "<div id=\"layer_update\"> <div class=\"row\"><label class=\"control-label col-lg-3 text-right\">资源编码：</label> <div class=\"col-lg-9\"><input name=\"permission\" class=\"form-control\" value=\""
			+ permission.permission
			+ "\"></div> </div> <div class=\"row\"><label class=\"control-label col-lg-3 text-right\">资源名称：</label> <div class=\"col-lg-9\"><input name=\"name\" class=\"form-control\" value=\""
			+ permission.name
			+ "\"></div> </div> <div class=\"row\"><label class=\"control-label col-lg-3 text-right\">资源类型：</label> <div class=\"col-lg-9\"> <select name=\"resourceType\" class=\"form-control\"> <option value=\"menu\">菜单</option> <option value=\"button\">按钮</option> </select> </div> </div> <div class=\"row\"><label class=\"control-label col-lg-3 text-right\">URL接口：</label> <div class=\"col-lg-9\"><input name=\"url\" class=\"form-control\" value=\""
			+ permission.url
			+ "\"></div> </div> <div class=\"row\"><label class=\"control-label col-lg-2 text-right\">父资源ID：</label> <div class=\"col-lg-2\"><input name=\"parentId\" class=\"form-control\" value=\""
			+ parentId
			+ "\" disabled=\"disabled\"></div> <label class=\"control-label col-lg-2 text-right\">父资源名称：</label> <div class=\"col-lg-4\"><input name=\"parentName\" class=\"form-control\" value=\""
			+ parentName
			+ "\" disabled=\"disabled\"></div><div class=\"col-lg-1\"><button class=\"btn btn-success\" onclick=\"updateParentPermission()\"><i class=\"fa fa-pencil fa-lg\"></i> 修改</button></div> </div> </div>";

	layer
			.open({
				title : "修改资源",
				type : 1,
				content : html,
				area : "650px",
				scrollbar : false,
				btn : [ "保存", "取消" ],
				success : function(layero, index) {
					updateLayer = layero;
					layero.find("select[name='resourceType']").val(
							permission.resourceType);
					closeLoad();
				},
				yes : function(index, layero) {
					openLoad();
					var percode = layero.find("input[name='permission']").val();
					var name = layero.find("input[name='name']").val();
					var url = layero.find("input[name='url']").val();
					var resourceType = layero.find(
							"select[name='resourceType']").val();
					var parentId = layero.find("input[name='parentId']").val();
					$.ajax({
						url : "/system-permission/update",
						data : {
							"id" : permission.id,
							"permission" : percode,
							"name" : name,
							"url" : url,
							"resourceType" : resourceType,
							"parentId" : parentId
						},
						type : "post",
						success : function(data) {
							closeLoad();
							layerMsg(data.status, data.cause);
							if (data.status == 200) {
								refreshTreeAndTable();
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

/* 修改弹窗中的修改按钮弹窗 */
function updateParentPermission() {
	layer.open({
		title : "修改父资源",
		type : 1,
		content : $("#permissionTree"),
		area : [ "300px" ],
		scrollbar : false,
		btn : [ "保存", "取消" ],
		yes : function(index, layero) {
			var permission = permissionTree.getSelectedNodes();
			updateLayer.find("input[name='parentId']").val(permission[0].id);
			updateLayer.find("input[name='parentName']")
					.val(permission[0].name);
			layer.close(index);
		}
	});
}

/* 删除按钮弹窗 */
function deleteVideoShow() {
	var selections = $("#permission_table").bootstrapTable("getSelections");
	if (selections.length != 1) {
		layerMsg(417, "请选择至少一条数据");
		return;
	}
	var permission = selections[0];
	layer.confirm("是否要删除资源【" + permission.name + "[" + permission.permission
			+ "]】，以及其相关配置？", {
		icon : 3,
		title : '提示'
	}, function(index) {
		openLoad();
		$.ajax({
			url : "/system-permission/delete",
			type : "post",
			data : {
				"id" : permission.id
			},
			success : function(data) {
				closeLoad();
				layerMsg(data.status, data.cause);
				if (data.status == 200) {
					refreshTreeAndTable();
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