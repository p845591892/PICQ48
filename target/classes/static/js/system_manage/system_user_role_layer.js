$(document).ready(function() {

	/* 未拥有的角色列表 */
	$("#havent_table").bootstrapTable({
		columns : [ {
			checkbox : true
		}, {
			field : "id",
			title : "ID",
			visible : false
		}, {
			field : "description",
			title : "未获赋权角色",
			formatter : function(value, row, index) {
				var html = "<a href=\"javascript:;\">" + row.description + "</a>"
				return html;
			}
		} ],
		clickToSelect : true,
		striped : true,
		pagination : true,
		paginationLoop : false,
		sidePagination : "client",
		pageNumber : 1,
		pageSize : 10,
		pageList : "unlimited",
		cache : false,
		search : true,
		searchAlign : "right",
		height : 550,
		url : "/resource/system-user/role/havent/" + uid
	});

	/* 已拥有的角色列表 */
	$("#have_table").bootstrapTable({
		columns : [ {
			checkbox : true
		}, {
			field : "id",
			title : "ID",
			visible : false
		}, {
			field : "description",
			title : "已获赋权角色",
			formatter : function(value, row, index) {
				var html = "<a href=\"javascript:;\">" + row.description + "</a>"
				return html;
			}
		} ],
		clickToSelect : true,
		striped : true,
		pagination : true,
		paginationLoop : false,
		sidePagination : "client",
		pageNumber : 1,
		pageSize : 10,
		pageList : "unlimited",
		cache : false,
		search : true,
		searchAlign : "right",
		height : 550,
		url : "/resource/system-user/role/have/" + uid
	});

	/* →按钮：为用户添加角色 */
	$("#button-right").click(function() {
		var selections = $("#havent_table").bootstrapTable("getSelections");
		if (selections.length == 0) {
			layerMsg(417, "请选择至少一条数据");
			return;
		}
		openLoad();
		var rids = "";
		for (var i = 0; i < selections.length; i++) {
			var role = selections[i];
			if (i == 0) {
				rids = role.id;
			} else {
				rids = rids + "," + role.id;
			}
		}
		$.ajax({
			url : "/system-user/role/add",
			data : {
				"uid" : uid,
				"rids" : rids
			},
			type : "post",
			success : function(data) {
				closeLoad();
				if (data.status == 200) {
					refreshTables();
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
		var selections = $("#have_table").bootstrapTable("getSelections");
		if (selections.length == 0) {
			layerMsg(417, "请选择至少一条数据");
			return;
		}
		openLoad();
		var rids = "";
		for (var i = 0; i < selections.length; i++) {
			var role = selections[i];
			if (i == 0) {
				rids = role.id;
			} else {
				rids = rids + "," + role.id;
			}
		}
		$.ajax({
			url : "/system-user/role/delete",
			data : {
				"uid" : uid,
				"rids" : rids
			},
			type : "post",
			success : function(data) {
				closeLoad();
				if (data.status == 200) {
					refreshTables();
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
function refreshTables() {
	$("#havent_table").bootstrapTable("refresh");
	$("#have_table").bootstrapTable("refresh");
}