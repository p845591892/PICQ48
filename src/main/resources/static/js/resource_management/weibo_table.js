$(document)
		.ready(
				function() {

					/* 友情提示 */
					var unique_id = $.gritter
							.add({
								title : '友情提示',
								text : '本页面进行的配置后，需要在服务器使用QQ打开对应的聊天窗口后，才能在同步消息的同时把微博消息发送到对应的聊天窗口中。',
								image : '../assets/img/ui-sam.jpg',
								sticky : true,
								time : '',
								class_name : 'my-sticky-class'
							});

					/* 微博用户表格 */
					$("#weibo_table")
							.bootstrapTable(
									{
										columns : [
												{
													checkbox : true
												},
												{
													field : "id",
													title : "用户ID",
													visible : false
												},
												{
													field : "avatarHd",
													title : "公式照",
													width : 70,
													formatter : function(value,
															row, index) {
														var avatar = row.avatarHd;
														return "<div class=\"project-wrapper\"><div class=\"project\"><div class=\"photo-wrapper img-circle\"><div class=\"photo\"><a class=\"fancybox\" href=\""
																+ avatar
																+ "\"><img src=\""
																+ avatar
																+ "\" class=\"img-circle\" width=\"60\" alt=\"\"/></a></div><div class=\"overlay\"></div></div></div></div>";
													}
												}, {
													field : "userName",
													title : "用户名"
												}, {
													field : "followCount",
													title : "关注数"
												}, {
													field : "followersCount",
													title : "粉丝数"
												}, {
													field : "containerUserId",
													title : "容器ID(用户)",
													visible : false
												}, {
													field : "containerDynamicId",
													title : "容器ID(动态)",
													visible : false
												} ],
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
										showColumns : true,
										showRefresh : true,
										toolbar : "#toolbar",
										toolbarAlign : "left",
										url : "/resource/weibo",
										detailView : true,
										detailFormatter : function(index, row) {
											var htmltext = showDetailView(row);
											return htmltext;
										}
									});

					/* 点击头像弹窗 */
					jQuery(".fancybox").fancybox();
					$("select.styled").customSelect();

				});

/* 展示微博动态发送目标QQ群(展示配置) */
function showDetailView(row) {
	openLoad();
	var htmltext = "";
	$.ajax({
		url : "/resource/weibo/dynamic-monitor/" + row.id,
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

/* 新增微博动态监控配置 */
function showAddMonitor(btn, userId) {
	openLoad();
	$.ajax({
		url : "/resource/weibo/add-monitor-layer/" + userId,
		type : "get",
		success : function(data) {
			closeLoad();
			if (data.status == 200) {
				layer.open({
					title : "房间新增监控配置",
					type : 1,
					content : data.data,
					area : "600px",
					scrollbar : false,
					btn : [ "保存", "取消" ],
					yes : function(index, layero) {
						openLoad();
						var communityId = layero.find("select").val();
						$.ajax({
							url : "/dynamic-monitor/add",
							data : {
								"userId" : userId,
								"communityId" : communityId
							},
							type : "post",
							success : function(data) {
								closeLoad();
								layerMsg(data.status, data.cause);
								if (data.status == 200) {
									$("#weibo_table").bootstrapTable(
											"refresh");
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
			} else {
				layerMsg(data.status, data.cause);
			}
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
		}
	});
}

/* 删除微博动态监控数据 */
function deleteMonitor(id) {
	layer.confirm('确定要删除该条配置吗？', {
		icon : 3,
		title : '提示'
	}, function(index) {
		openLoad();
		$.ajax({
			url : "/dynamic-monitor/delete",
			type : "post",
			data : {
				"id" : id
			},
			success : function(data) {
				closeLoad();
				layerMsg(data.status, data.cause);
				if (data.status == 200) {
					$("#weibo_table").bootstrapTable("refresh");
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

/* 新增监控的微博用户 */
function addVideoShow() {
	var html = "<div id=\"layer_add\"><div class=\"row\">"
			+ "<label class=\"control-label col-xs-3 text-right\">容器ID：</label><div class=\"col-xs-8\">"
			+ "<input name=\"containerId\" class=\"form-control\">"
			+ "<span class=\"help-block\">例如：[https://m.weibo.cn/p/1005056501690381]<br>其中的1005056501690381就是容器ID</span>"
			+ "</div></div></div>";

	layer.open({
		title : "新增监控的微博用户",
		type : 1,
		content : html,
		area : "600px",
		scrollbar : false,
		btn : [ "保存", "取消" ],
		yes : function(index, layero) {
			openLoad();
			var containerId = layero.find("input[name='containerId']").val();
			$.ajax({
				url : "/weibo/add",
				data : {
					"containerId" : containerId
				},
				type : "post",
				success : function(data) {
					closeLoad();
					layerMsg(data.status, data.cause);
					if (data.status == 200) {
						$("#weibo_table").bootstrapTable("refresh");
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

/* 删除监控的微博用户 */
function deleteVideoShow() {
	var selections = $("#weibo_table").bootstrapTable("getSelections");
	var id = "";
	if (selections.length == 0) {
		layerMsg(417, "请至少选择一条信息");
		return;
	} else {
		for (var int = 0; int < selections.length; int++) {
			var weiboUser = selections[int];
			if (int == 0) {
				id = id + weiboUser.id;
			} else {
				id = id + "," + weiboUser.id;
			}
		}
	}
	layer.confirm("删除微博信息后关联的配置也会一并删除", {
		icon : 3,
		title : '提示'
	}, function(index) {
		openLoad();
		$.ajax({
			url : "/weibo/delete",
			type : "post",
			data : {
				"id" : id
			},
			success : function(data) {
				closeLoad();
				layerMsg(data.status, data.cause);
				if (data.status == 200) {
					$("#weibo_table").bootstrapTable("refresh");
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