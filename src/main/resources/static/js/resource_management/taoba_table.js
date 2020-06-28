$(document).ready(function () {

    /* 友情提示 */
    var unique_id = $.gritter.add({
        title: '友情提示',
        text: '本页面进行的配置前，需要先添加机器人QQ好友或拉入Q群中。个人使用免费，咨询或开通权限联系QQ847109667，微博@JuFF_白羽。',
        image: '../assets/img/ui-sam.jpg',
        sticky: true,
        time: '',
        class_name: 'my-sticky-class'
    });

    /* 桃叭项目表格 */
    $("#taoba_table").bootstrapTable({
        columns: [{
            checkbox: true
        }, {
            field: "id",
            title: "项目ID"
        }, {
            field: "poster",
            title: "封面",
            formatter: posterHtml
        }, {
            field: "title",
            title: "集资项目",
            width: 150,
            formatter: titleHtml
        }, {
            field: "startTime",
            title: "开始时间",
            width: 140
        }, {
            field: "endTime",
            title: "结束时间",
            width: 140
        }, {
            field: "donation",
            title: "筹集金额",
            formatter: moneyHtml
        }, {
            field: "amount",
            title: "目标金额",
            formatter: moneyHtml
        }, {
            field: "nickname",
            title: "发起人"
        }, {
            field: "percent",
            title: "完成度",
            formatter: rateHtml
        }, {
            field: "joinUser",
            title: "支持人数"
        }, {
            field: "running",
            title: "项目状态",
            formatter: runningHtml
        }, {
            field: "osType",
            title: "OS类型",
            visible: false
        }],
        // clickToSelect : true,
        striped: true,
        pagination: true,
        sidePagination: "client",
        pageNumber: 1,
        pageSize: 15,
        paginationLoop: false,
        pageList: [15, 25, 50],
        cache: false,
        search: true,
        searchAlign: "right",
        showColumns: true,
        showRefresh: true,
        toolbar: "#toolbar",
        toolbarAlign: "left",
        url: "/resource/taoba",
        detailView: true,
        detailFormatter: detailView
    });

});

/**
 * 金额列处理
 */
var moneyHtml = function (value, row, index) {
    return value + "元";
}

/**
 * 百分比列处理
 */
var rateHtml = function (value, row, index) {
    return value + "%";
}

/**
 * 封面列处理
 */
var posterHtml = function (value, row, index) {
	return "<img src=\"" + value + "\"  width=\"150\"/>";
}

/**
 * 标题列处理
 */
var titleHtml = function (value, row, index) {
	var url = row.detailUrl;
	return "<a href=\"" + url + "\">" + value + "</a>";
}

/**
 * 项目状态列处理
 */
var runningHtml = function (value, row, index) {
	if (value) {
		return "进行中";
	} else {
		return "已结束";
	}
}

/**
 * 新增弹窗
 */
function addVideoShow() {
    var html = "<div id=\"layer_add\"><div class=\"row\">"
        + "<label class=\"control-label col-xs-3 text-right\">桃叭链接：</label><div class=\"col-xs-8\">"
        + "<input name=\"detailUrl\" class=\"form-control\">"
        + "<span class=\"help-block\">例如：https://www.tao-ba.club/#/pages/idols/detail?id=5005</span>"
        + "</div></div></div>";

    layer.open({
        title: "新增摩点项目",
        type: 1,
        content: html,
        area: "600px",
        scrollbar: false,
        btn: ["保存", "取消"],
        yes: function (index, layero) {
            openLoad();
            var detailUrl = layero.find("input[name='detailUrl']").val();
            $.ajax({
                url: "/taoba/add",
                data: {
                    "detailUrl": detailUrl
                },
                type: "post",
                success: function (data) {
                    closeLoad();
                    layerMsg(data.status, data.cause);
                    if (data.status == 200) {
                        $("#taoba_table").bootstrapTable("refresh");
                        layer.close(index);
                    }
                },
                error: function (data) {
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
    var ids = getSelectedIds();
    if (ids == null) {
        return;
    }
    layer.confirm("删除桃叭项目后关联的集资记录和监控配置也会一并删除，是否要执行删除操作？", {
        icon: 3,
        title: '提示'
    }, function (index) {
        openLoad();
        $.ajax({
            url: "/taoba/delete",
            type: "post",
            data: {
                "detailIds": ids
            },
            success: function (data) {
                closeLoad();
                layerMsg(data.status, data.cause);
                if (data.status == 200) {
                    $("#taoba_table").bootstrapTable("refresh");
                    layer.close(index);
                }
            },
            error: function (data) {
                closeLoad();
                layerMsg(500, "请求失败");
            }
        });
    });
}

/**
 * 展示桃叭项目发送目标QQ群(展示配置)
 */
var detailView = function (index, row) {
    openLoad();
    var htmltext = "";
    $.ajax({
        url: "/resource/taoba/monitor/" + row.id,
        type: "get",
        async: false,
        success: function (data) {
            if (data.status == 200) {
                htmltext = data.data;
            }
            closeLoad();
        },
        error: function (data) {
            closeLoad();
            layerMsg(500, "请求失败");
        }
    });
    return htmltext;
}

/**
 * 新增房间监控配置
 */
function showAddMonitor(btn, detailId) {
    openLoad();
    $.ajax({
        url: "/resource/taoba/add-monitor-layer/" + detailId,
        type: "get",
        success: function (data) {
            closeLoad();
            if (data.status == 200) {
                layer.open({
                    title: "房间新增监控配置",
                    type: 1,
                    content: data.data,
                    area: "600px",
                    scrollbar: false,
                    btn: ["保存", "取消"],
                    yes: function (index, layero) {
                        openLoad();
                        var communityId = layero.find("select").val();
                        $.ajax({
                            url: "/taoba/monitor/add",
                            data: {
                                "detailId": detailId,
                                "communityId": communityId
                            },
                            type: "post",
                            success: function (data) {
                                closeLoad();
                                layerMsg(data.status, data.cause);
                                if (data.status == 200) {
                                    $("#taoba_table")
                                        .bootstrapTable("refresh");
                                    layer.close(index);
                                }
                            },
                            error: function (data) {
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
        error: function (data) {
            closeLoad();
            layerMsg(500, "请求失败");
        }
    });
}

/**
 * 删除摩点监控数据
 * 
 * @param {*}
 *            id
 */
function deleteMonitor(monitorId, detailId) {
    layer.confirm('确定要删除该条配置吗？', {
        icon: 3,
        title: '提示'
    }, function (index) {
        openLoad();
        $.ajax({
            url: "/taoba/monitor/delete",
            type: "post",
            data: {
                "monitorId": monitorId,
                "detailId": detailId
            },
            success: function (data) {
                closeLoad();
                layerMsg(data.status, data.cause);
                if (data.status == 200) {
                    $("#taoba_table").bootstrapTable("refresh");
                    layer.close(index);
                }
            },
            error: function (data) {
                closeLoad();
                layerMsg(500, "请求失败");
            }
        });
    });
}

/**
 * 获取选中的行的id，多个id用逗号连接
 */
function getSelectedIds() {
    var selections = $("#taoba_table").bootstrapTable("getSelections");
    var ids = "";
    if (selections.length == 0) {
        layerMsg(417, "请至少选择一条信息");
        return null;
    } else {
        for (var int = 0; int < selections.length; int++) {
            var modian = selections[int];
            if (int == 0) {
                ids = ids + modian.id;
            } else {
                ids = ids + "," + modian.id;
            }
        }
    }
    return ids;
}

/**
 * 跳转摩点项目数据页面
 */
function toVisual() {
    var ids = getSelectedIds();
    if (ids != null) {
        window.location.href = "/data-visualization/taoba-visual?detailIds=" + ids;
    }
}