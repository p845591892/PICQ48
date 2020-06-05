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

    /* 摩点项目表格 */
    $("#modian_table").bootstrapTable({
        columns: [{
            checkbox: true
        }, {
            field: "id",
            title: "项目ID"
        }, {
            field: "name",
            title: "项目名称"
        }, {
            field: "startTime",
            title: "项目开始时间"
        }, {
            field: "endTime",
            title: "项目结束时间"
        }, {
            field: "city",
            title: "发起位置"
        }, {
            field: "installMoney",
            title: "目标金额",
            formatter: moneyHtml
        }, {
            field: "backerMoney",
            title: "已筹金额",
            formatter: moneyHtml
        }, {
            field: "rate",
            title: "完成度",
            formatter: rateHtml
        }, {
            field: "backerCount",
            title: "支持人数"
        }, {
            field: "status",
            title: "项目状态"
        }, {
            field: "postId",
            title: "评论请求ID",
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
        url: "/resource/modian",
        detailView: true,
        detailFormatter: detailView
    });

});

/**
 * 金额列处理
 * @param {*} value 
 * @param {*} row 
 * @param {*} index 
 */
var moneyHtml = function (value, row, index) {
    return value + "元";
}

/**
 * 百分比列处理
 * @param {*} value 
 * @param {*} row 
 * @param {*} index 
 */
var rateHtml = function (value, row, index) {
    return value + "%";
}

/**
 * 新增弹窗
 */
function addVideoShow() {
    var html = "<div id=\"layer_add\"><div class=\"row\">"
        + "<label class=\"control-label col-xs-3 text-right\">项目ID：</label><div class=\"col-xs-8\">"
        + "<input name=\"id\" class=\"form-control\">"
        + "<span class=\"help-block\">例如：[https://zhongchou.modian.com/item/27659.html?_mdsf1=1383779_share_unkown_ios_xiangmu_27659]<br>其中的27659就是该项目ID</span>"
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
            var id = layero.find("input[name='id']").val();
            $.ajax({
                url: "/modian/add",
                data: {
                    "id": id
                },
                type: "post",
                success: function (data) {
                    closeLoad();
                    layerMsg(data.status, data.cause);
                    if (data.status == 200) {
                        $("#modian_table").bootstrapTable("refresh");
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
    layer.confirm("删除摩点项目后关联的集资记录也会一并删除", {
        icon: 3,
        title: '提示'
    }, function (index) {
        openLoad();
        $.ajax({
            url: "/modian/delete",
            type: "post",
            data: {
                "id": ids
            },
            success: function (data) {
                closeLoad();
                layerMsg(data.status, data.cause);
                if (data.status == 200) {
                    $("#modian_table").bootstrapTable("refresh");
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
 * 展示摩点项目发送目标QQ群(展示配置)
 * @param {*} index 
 * @param {*} row 
 */
var detailView = function (index, row) {
    openLoad();
    var htmltext = "";
    $.ajax({
        url: "/resource/modian/comment-monitor/" + row.id,
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
 * @param {*} btn 
 * @param {*} projectId 
 */
function showAddMonitor(btn, projectId) {
    openLoad();
    $.ajax({
        url: "/resource/modian/add-monitor-layer/" + projectId,
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
                            url: "/modian-monitor/add",
                            data: {
                                "projectId": projectId,
                                "communityId": communityId
                            },
                            type: "post",
                            success: function (data) {
                                closeLoad();
                                layerMsg(data.status, data.cause);
                                if (data.status == 200) {
                                    $("#modian_table")
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
 * @param {*} id 
 */
function deleteMonitor(id) {
    layer.confirm('确定要删除该条配置吗？', {
        icon: 3,
        title: '提示'
    }, function (index) {
        openLoad();
        $.ajax({
            url: "/modian-monitor/delete",
            type: "post",
            data: {
                "id": id
            },
            success: function (data) {
                closeLoad();
                layerMsg(data.status, data.cause);
                if (data.status == 200) {
                    $("#modian_table").bootstrapTable("refresh");
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
    var selections = $("#modian_table").bootstrapTable("getSelections");
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
        window.location.href = "/data-visualization/modian-visual?projectIds=" + ids;
    }
}