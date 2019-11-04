$(document).ready(function() {

	/* 系统用户表 */
	$("#user_table").bootstrapTable({
		columns : [ {
			radio : true
		}, {
			field : "id",
			title : "ID"
		}, {
			field : "username",
			title : "用户名"
		}, {
			field : "nickname",
			title : "昵称"
		}, {
			field : "salt",
			title : "盐码"
		}, {
			field : "email",
			title : "邮箱"
		}, {
			field : "state",
			title : "状态",
			formatter : function(value, row, index) {
				var state = row.state;

				if (state == 1) {
					state = "正常";
				} else if (state == 2) {
					state = "锁定";
				} else if (state == 0) {
					state = "创建未认证"
				}
				return state;
			}
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
		url : "/resource/system-user"
	});

});

/* 修改按钮弹窗 */
function updateVideoShow() {
	var selections = $("#user_table").bootstrapTable("getSelections");
	if (selections.length != 1) {
		layerMsg(417, "请选择一条数据");
		return;
	}
	var user = selections[0];
	var html = "<div id=\"layer_update\"> <div class=\"row\"><label class=\"control-label col-xs-3 text-right\">昵称：</label> <div class=\"col-xs-8\"><input name=\"nickname\" class=\"form-control\" value=\""
			+ user.nickname
			+ "\"></div> </div> <div class=\"row\"><label class=\"control-label col-xs-3 text-right\">状态：</label> <div class=\"col-xs-8\"> <select class=\"form-control\" name=\"state\"> <option value=\"0\">创建未认证</option> <option value=\"1\">正常</option> <option value=\"2\">锁定</option> </select> </div> </div> </div>";

	layer.open({
		title : "修改系统用户信息",
		type : 1,
		content : html,
		area : [ "600px" ],
		scrollbar : false,
		btn : [ "保存", "取消" ],
		success : function(layero, index) {
			$(layero).find("select[name='state']").val(user.state);
		},
		yes : function(index, layero) {
			openLoad();
			user.nickname = layero.find("input[name='nickname']").val();
			user.state = layero.find("select[name='state']").val();
			$.ajax({
				url : "/system-user/update",
				data : user,
				type : "post",
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					if (data.status == 200) {
						$("#user_table").bootstrapTable("refresh");
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

/* 角色设置弹窗按钮 */
function roleVideoShow() {
	var selections = $("#user_table").bootstrapTable("getSelections");
	if (selections.length != 1) {
		layerMsg(417, "请选择一条数据");
		return;
	}
	openLoad();
	var user = selections[0];
	layer.open({
		type : 2,
		title : [ "角色设置：" + user.nickname, "font-size:18px;" ],
		content : "/system-manage/system-user/role/set/" + user.id,
		area : [ "480px", "600px" ],
		success : function(layero, index) {
			closeLoad();
		}
	});
}