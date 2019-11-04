var haventTree, haveTree; // zTree 的对象

$(document).ready(function() {

	/* 初始化资源树 */
	haventTree = $.fn.zTree.init($("#haventTree"), {
		// zTree 的参数配置
		async : {
			enable : true,
			dataType : "text",
			type : "get",
			url : "/resource/system-role/permission/havent",
			otherParam : {
				"rid" : rid
			},
			dataFilter : function(treeId, parentNode, responseData) {
				return responseData.data;
			}
		},
		data : {
			keep : {
				name : "name" // 节点显示的值
			},
			simpleData : {
				enable : true,// 如果为true，可以直接把从数据库中得到的List集合自动转换为Array格式。而不必转换为json传递
				idKey : "id",// 节点的id
				pIdKey : "parentId",// 节点的父节点id
				rootPId : -1
			}
		},
		check : {
			enable : true,
			autoCheckTrigger : true,
			chkboxType : {
				"Y" : "p",
				"N" : "s"
			}
		}
	});

	/* 初始化已获赋权的资源树 */
	haveTree = $.fn.zTree.init($("#haveTree"), {
		// zTree 的参数配置
		async : {
			enable : true,
			dataType : "text",
			type : "get",
			url : "/resource/system-role/permission/have",
			otherParam : {
				"rid" : rid
			},
			dataFilter : function(treeId, parentNode, responseData) {
				return responseData.data;
			}
		},
		data : {
			keep : {
				name : "name" // 节点显示的值
			},
			simpleData : {
				enable : true,// 如果为true，可以直接把从数据库中得到的List集合自动转换为Array格式。而不必转换为json传递
				idKey : "id",// 节点的id
				pIdKey : "parentId",// 节点的父节点id
				rootPId : -1
			}
		},
		check : {
			enable : true,
			autoCheckTrigger : true,
			chkboxType : {
				"Y" : "s",
				"N" : "s"
			}
		}
	});

	/* →按钮：为用户添加角色 */
	$("#button-right").click(function() {
		var nodes = haventTree.getChangeCheckedNodes();
		if (nodes.length == 0) {
			layerMsg(417, "请选择至少一条数据");
			return;
		}
		openLoad();
		var pids = "";
		for (var i = 0; i < nodes.length; i++) {
			var permission = nodes[i];
			if (i == 0) {
				pids = permission.id;
			} else {
				pids = pids + "," + permission.id;
			}
		}
		$.ajax({
			url : "/system-role/permission/add",
			data : {
				"rid" : rid,
				"pids" : pids
			},
			type : "post",
			success : function(data) {
				closeLoad();
				if (data.status == 200) {
					refreshTrees();
				} else {
					layerMsg(data.status, data.cause);
				}
			},
			error : function(data) {
				closeLoad();
				layerMsg(500, "请求失败");
			}
		});
	});

	/* ←按钮：为用户删除角色 */
	$("#button-left").click(function() {
		var nodes = haveTree.getChangeCheckedNodes();
		if (nodes.length == 0) {
			layerMsg(417, "请选择至少一条数据");
			return;
		}
		openLoad();
		var pids = "";
		for (var i = 0; i < nodes.length; i++) {
			var permission = nodes[i];
			if (i == 0) {
				pids = permission.id;
			} else {
				pids = pids + "," + permission.id;
			}
		}
		$.ajax({
			url : "/system-role/permission/delete",
			data : {
				"rid" : rid,
				"pids" : pids
			},
			type : "post",
			success : function(data) {
				closeLoad();
				if (data.status == 200) {
					refreshTrees();
				} else {
					layerMsg(data.status, data.cause);
				}
			},
			error : function(data) {
				closeLoad();
				layerMsg(500, "请求失败");
			}
		});
	});
});

/* 刷新表格 */
function refreshTrees() {
	haventTree.reAsyncChildNodes(null, "refresh", true);
	haveTree.reAsyncChildNodes(null, "refresh", true);
}