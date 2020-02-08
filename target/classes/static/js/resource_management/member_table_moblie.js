$(document).ready(function() {
	/* 友情提示 */
	var unique_id = $.gritter.add({
		// (string | mandatory) the heading of the
		// notification
		title : '友情提示',
		// (string | mandatory) the text inside the
		// notification
		text : '本页面进行的配置前，需要先添加机器人QQ好友或拉入Q群中。',
		// (string | optional) the image to display on
		// the left
		image : '../assets/img/ui-sam.jpg',
		// (bool | optional) if you want it to fade out
		// on its own or just sit there
		sticky : true,
		// (int | optional) the time you want it to be
		// alive for before fading out
		time : '',
		// (string | optional) the class name you want
		// to apply to that specific message
		class_name : 'my-sticky-class'
	});

	/* 成员表格 */
	$("#member_table").bootstrapTable({
		columns : [ {
			field : "id",
			title : "成员ID",
			visible : false
		}, {
			field : "avatar",
			title : "公式照",
			formatter : avatarHtml
		}, {
			field : "name",
			title : "名字",
		}, {
			field : "pinyin",
			title : "名字拼音"
		}, {
			field : "nickname",
			title : "昵称"
		}, {
			field : "groupId",
			title : "所属团体ID",
			visible : false
		}, {
			field : "groupName",
			title : "所属团体"
		}, {
			field : "teamId",
			title : "所属队伍ID",
			visible : false
		}, {
			field : "teamName",
			title : "所属队伍"
		}, {
			field : "joinTime",
			title : "加入时间"
		}, {
			field : "birthplace",
			title : "出生地"
		}, {
			field : "roomId",
			title : "口袋房间ID",
			visible : false
		}, {
			field : "roomName",
			title : "口袋房间名字"
		}, {
			field : "topic",
			title : "话题"
		}, {
			field : "roomMonitor",
			title : "口袋房间监控",
			formatter : roomMonitorHtml,
			events : roomMonitorEvent
		}, {
			field : "monitorConfig",
			title : "监控配置",
			formatter : monitorConfigHtml,
			events : monitorConfigEvent
		} ],
		cardView : true,
		striped : true,
		pagination : true,
		sidePagination : "server",
		responseHandler : responseHandler,
		queryParamsType : '',
		queryParams : queryParams,
		pageNumber : 1,
		pageSize : 15,
		paginationLoop : false,
		pageList : [ 15, 25, 50 ],
		cache : false,
		showColumns : true,
		showRefresh : true,
		toolbar : "#toolbar",
		toolbarAlign : "left",
		url : "/resource/member"
	// detailView : true,
	// detailFormatter : detailFormatter
	});

	/* 更改团体后的操作 */
	$("#select1").change(function() {
		var group = $("#select1").val();
		getTeam(group);
	});

	/* 查询按钮 */
	$("#btn_select").on("click", function() {
		$("#member_table").bootstrapTable("refreshOptions", {
			pageNumber : 1
		});
		$("#member_table").bootstrapTable("refresh");
	});

	/* 点击头像弹窗 */
	jQuery(".fancybox").fancybox();
	// $("select.styled").customSelect();

});

/* 下拉框 */
var select_team = {
	"ALL" : [ "ALL" ],
	"SNH48" : [ "ALL", "TEAM SII", "TEAM NII", "TEAM HII", "TEAM X", "明星殿堂", "预备生", "TEAM FT", "TEAM XII" ],
	"GNZ48" : [ "ALL", "TEAM G", "TEAM NIII", "TEAM Z", "预备生" ],
	"BEJ48" : [ "ALL", "TEAM B", "TEAM E", "TEAM J", "预备生" ],
	"CKG48" : [ "ALL", "TEAM C", "TEAM K", "预备生" ],
	"SHY48" : [ "ALL", "TEAM SIII", "TEAM HIII", "预备生" ],
	"IDFT" : [ "ALL" ],
	"海外练习生" : [ "ALL" ]
};

/**
 * table请求参数
 * 
 * @param {*}
 *            params
 */
var queryParams = function(params) {
	params["groupName"] = $("#select1").val();
	params["teamName"] = $("#select2").val();
	params["roomMonitor"] = $("#select3").val();
	params["searchText"] = $("#input1").val();
	return params;
}

/**
 * 表格响应处理
 * 
 * @param {*}
 *            res
 */
var responseHandler = function(res) {
	return {
		pageNum : res.data.number,
		total : res.data.totalElements,
		rows : res.data.content
	};
}

/**
 * 头像列处理
 * 
 * @param {*}
 *            value
 * @param {*}
 *            row
 * @param {*}
 *            index
 */
var avatarHtml = function(value, row, index) {
	var avatar = row.avatar;
	return "<div class=\"row mt\"> <div class=\"col-xs-5 col-sm-3 desc\"> <div class=\"project-wrapper\"> <div class=\"project\"> <div class=\"photo-wrapper\"> <div class=\"photo\"> <a class=\"fancybox\" href=\""
			+ avatar + "\"><img class=\"img-responsive\" src=\"" + avatar + "\" alt=\"\"></a> </div> <div class=\"overlay\"></div> </div> </div> </div> </div> </div>";
}

/**
 * 监控状态列处理
 * 
 * @param {*}
 *            value
 * @param {*}
 *            row
 * @param {*}
 *            index
 */
var roomMonitorHtml = function(value, row, index) {
	var roomMonitor = row.roomMonitor;
	var id = row.id;
	return getRoomMonitorButton(roomMonitor, id);
}

/**
 * 监控配置列处理
 * 
 * @param {*}
 *            value
 * @param {*}
 *            row
 * @param {*}
 *            index
 */
var monitorConfigHtml = function(value, row, index) {
	var id = row.id;
	var roomMonitor = row.roomMonitor;
	if (roomMonitor == 1 || roomMonitor == 2) {
		return "<button type=\"button\" class=\"btn btn-theme03 btn-sm btn-monitor-config\"><i class=\"fa fa-qq fa-lg\"></i> 配置</button>"
	}
}

/**
 * 监控状态列活动
 */
var roomMonitorEvent = {
	"click .btn-room-monitor" : function(event, value, row, index) {
		updateRoomMonitor(value, row, index);
	}
}

/**
 * 监控配置页面对象
 */
var monitorConfigPage = null;

/**
 * 监控配置列活动
 */
var monitorConfigEvent = {
	"click .btn-monitor-config" : function(event, value, row, index) {
		var htmltext = showDetailView(row);
		var title = [ row.name + "口袋房间监控配置", "background-color:#ffd777; color:#fff;" ];
		monitorConfigPage = layer.open({
			"type" : 1,
			"title" : title,
			"content" : htmltext,
			"anim" : "up",
			"style" : "position:fixed; left:0; top:0; width:100%; height:100%; border: none; -webkit-animation-duration: .5s; animation-duration: .5s;",
			"btn" : "关闭"
		});
	}
}

/**
 * 表格详细拓展视图
 * 
 * @param {*}
 *            index
 * @param {*}
 *            row
 */
var detailFormatter = function(index, row) {
	var htmltext = showDetailView(row);
	return htmltext;
}

/**
 * 开关同步房间消息
 * 
 * @param {*}
 *            btn
 * @param {*}
 *            id
 * @param {*}
 *            type
 */
function updateRoomMonitor(value, row, index) {
	openLoad();
	var roomMonitor = 2;
	var id = row.id;
	/* value变化：1->2 关闭同步，2->1 开启同步 */
	if (value == 1) {
		roomMonitor = 2;
	} else if (value == 2) {
		roomMonitor = 1;
	}
	$.ajax({
		url : "/member/update/room-monitor",
		data : {
			"id" : id,
			"roomMonitor" : roomMonitor
		},
		type : "post",
		success : function(data) {
			closeLoad();
			layerMsg(data.status, data.cause);
			if (data.status == 200) {
				$("#member_table").bootstrapTable("updateRow", {
					"index" : index,
					"row" : {
						"roomMonitor" : roomMonitor
					}
				});
			}
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
}

/**
 * 展示房间的消息发送目标QQ群(展示配置)
 * 
 * @param {*}
 *            row
 */
function showDetailView(row) {
	openLoad();
	var htmltext = "";
	$.ajax({
		url : "/resource/meber/room-monitor/" + row.roomId,
		type : "get",
		async : false,
		success : function(data) {
			if (data.status == 200) {
				htmltext = data.data;
			}
			closeLoad();
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
	return htmltext;
}

/**
 * 新增房间监控配置
 * 
 * @param {*}
 *            btn
 * @param {*}
 *            roomId
 */
function showAddMonitor(btn, roomId) {
	$.ajax({
		url : "/resource/meber/add-monitor-layer/" + roomId,
		type : "get",
		success : function(data) {
			if (data.status == 200) {
				var layerHtml = null;
				layer.open({
					"type" : 1,
					"title" : [ "房间新增监控配置", "background-color:#ffd777; color:#fff;" ],
					"content" : data.data,
					"anim" : "up",
					"shadeClose" : false,
					"btn" : [ "保存", "取消" ],
					"yes" : function(index) {
						console.log(index)
						openLoad();
						var keyword = $("textarea[name = 'keyword']").val();
						var communityId = $("select[name = 'communityId']").val();
						$.ajax({
							url : "/room-monitor/add",
							data : {
								"keywords" : keyword,
								"communityId" : communityId,
								"roomId" : roomId
							},
							type : "post",
							success : function(data) {
								closeLoad();
								layerMsg(data.status, data.cause);
								if (data.status == 200) {
									$("#member_table").bootstrapTable("refresh");
									layer.close(index);
									layer.close(monitorConfigPage);
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
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
}

/**
 * 关键字配置修改按钮
 * 
 * @param {*}
 *            id
 */
function updateKeyword(id) {
	openLoad();
	var keyword = "";
	$.ajax({
		url : "/resource/room-monitor/" + id + "/keyword",
		type : "get",
		async : false,
		success : function(data) {
			closeLoad();
			if (data.status == 200) {
				keyword = data.data.keywords;
			}
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
	layer.open({
		title : [ "修改监控的关键字", "background-color:#ffd777; color:#fff;" ],
		type : 1,
		content : "<form id=\"monitor-form\" class=\"form-horizontal style-form\">" + "<div class=\"form-group\">" + "<div class=\"col-sm-1\"></div>"
				+ "<label class=\"col-sm-2 control-label\">关键字：</label><div class=\"col-sm-7\">" + "<textarea class=\"form-control\" rows=\"3\" name=\"keyword\"></textarea>"
				+ "<span class=\"help-block\">对监控的消息进行关键字筛选，只发送包含关键字的消息。（关键字用逗号隔开，为空则不做筛选）</span>" + "</div>" + "</div>" + "</form>",
		anim : "up",
		shadeClose : false,
		btn : [ "保存", "取消" ],
		success : function(elem) {
			$(elem).find("textarea").val(keyword);
		},
		yes : function(index) {
			openLoad();
			keyword = $("textarea[name = 'keyword']").val();
			$.ajax({
				url : "/room-monitor/update/keyword",
				type : "post",
				data : {
					"id" : id,
					"keyword" : keyword
				},
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					if (data.status == 200) {
						$("#member_table").bootstrapTable("refresh");
						layer.close(index);
						layer.close(monitorConfigPage);
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
 * 配置删除
 * 
 * @param {*}
 *            id
 */
function deleteMonitor(id) {
	layer.open({
		type : 0,
		content : "确定要删除该条配置吗？",
		skin : "footer",
		btn : [ '删除', '取消' ],
		yes : function(index) {
			openLoad();
			$.ajax({
				url : "/room-monitor/delete",
				type : "post",
				data : {
					"id" : id
				},
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					if (data.status == 200) {
						$("#member_table").bootstrapTable("refresh");
						layer.close(index);
						layer.close(monitorConfigPage);
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
 * 更新成员资料信息
 */
function refreshVideoShow() {
	openLoad();
	$.ajax({
		url : "/member/refresh",
		type : "get",
		success : function(data) {
			closeLoad();
			layerMsg(data.status, data.cause);
			$("#member_table").bootstrapTable("refresh");
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
}

/**
 * 获取队伍下拉框
 * 
 * @param {*}
 *            group
 */
function getTeam(group) {
	$("#select2").empty();
	var teams = select_team[group];
	for (let i = 0; i < teams.length; i++) {
		const team = teams[i];
		$("#select2").append("<option value='" + team + "'>" + team + "</option>");
	}
}

/**
 * 获取监控状态按钮
 * 
 * @param type
 *            监控状态类型
 * @param id
 *            成员ID
 * @returns 按钮的html字符串
 */
function getRoomMonitorButton(type) {
	if (type == 1) {// 同步开启中
		return "<button type=\"button\" class=\"btn btn-theme04 btn-sm btn-room-monitor\"><i class=\"fa fa-power-off fa-lg\"></i> 关闭</button>"
	} else if (type == 2) {// 同步关闭中
		return "<button type=\"button\" class=\"btn btn-success btn-sm btn-room-monitor\"><i class=\"fa fa-play-circle-o fa-lg\"></i> 开启</button>";
	} else if (type == 404) {// 无房间
		return "房间不存在";
	}
}

/**
 * 新增成员弹窗
 */
function addVideoShow() {
	layer.open({
		type : 0,
		title : [ "添加成员", "background-color:#428bca; color:#fff;" ],
		anim : "up",
		shadeClose : false,
		content : "<input class=\"form-control inp-add-member\" name=\"id\" placeholder=\"请输要新增的成员ID\" />",
		btn : [ "确认", "取消" ],
		yes : function(index) {
			openLoad();
			var memberId = $(".inp-add-member").val();
			$.ajax({
				url : "/member/add",
				type : "put",
				data : {
					id : memberId
				},
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					$("#member_table").bootstrapTable("refresh");
				},
				error : function(data) {
					closeLoad();
					layerMsg(500, "请求失败");
				}
			});
			layer.close(index);
		}
	});
}