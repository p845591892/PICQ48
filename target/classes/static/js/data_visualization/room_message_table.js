$(document).ready(function () {

    $("#room_message_table").bootstrapTable({
        columns: [/* {
			checkbox : true
		}, */{
                field: "id",
                title: "消息ID",
                visible: false
            }, {
                field: "senderId",
                title: "发送人ID",
                visible: false
            }, {
                field: "senderName",
                title: "发送人",
                width: 105
            }, {
                field: "roomId",
                title: "所属房间ID",
                visible: false
            }, {
                field: "msgObject",
                title: "消息类型",
                width: 95,
                formatter: msgObjectHtml
            }, {
                field: "msgContent",
                title: "消息内容",
                formatter: msgContentHtml
            }, {
                field: "msgTime",
                title: "消息发送时间",
                width: 138
            }, {
                field: "isSend",
                title: "是否发送过QQ消息",
                width: 100,
                formatter: isSendHtml
            }],
        // clickToSelect : true,
        url: "/resource/room-message",
        method: "GET",
        responseHandler: responseHandler,
        queryParamsType: '',
        queryParams: queryParams,
        striped: true,
        pagination: true,
        sidePagination: "server",
        pageNumber: 1,
        pageSize: 15,
        paginationLoop: true,
        pageList: [15, 25, 50, 100],
        cache: false,
        toolbar: "#toolbar",
        toolbarAlign: "left",
        showColumns: true,
        showRefresh: true
    });

    /* 查询 */
    $("#btn_select").on("click", function () {
        $("#room_message_table").bootstrapTable("refreshOptions", { pageNumber: 1 });
        $("#room_message_table").bootstrapTable("refresh");
    });

    /**
     * 初始化搜索栏
     */
    installSelect1();

});

/**
 * 消息类型
 */
var message_type = {
    "TEXT": "普通消息", "REPLY": "翻牌", "IMAGE": "图片", "LIVEPUSH": "生放送", "FLIPCARD": "付费翻牌", "EXPRESS": "特殊表情", "VIDEO": "视频",
    "VOTE": "投票", "AUDIO": "语音",
    "text": "普通消息(旧)", "faipaiText": "翻牌(旧)", "image": "图片(旧)", "live": "直播(旧)", "diantai": "电台(旧)", "idolFlip": "付费翻牌(旧)",
    "audio": "语音(旧)", "videoRecord": "视频(旧)"
}

/**
 * 消息类型列处理
 * @param {*} value 
 * @param {*} row 
 * @param {*} index 
 */
var msgObjectHtml = function (value, row, index) {
    var msgObject = row.messageObject;
    return message_type[msgObject];
}

/**
 * 消息内容列处理
 * @param {*} value 
 * @param {*} row 
 * @param {*} index 
 */
var msgContentHtml = function (value, row, index) {
    var msgObject = row.messageObject;
    var msgContent = row.msgContent;

    if (msgObject == "IMAGE" || msgObject == "image") {
        msgContent = msgContent.replace("<img>", "");
        msgContent = msgContent.replace("[图片]", "");
        return "<a target=\"_blank\" href=\""
            + msgContent
            + "\"><img src=\""
            + msgContent
            + "\" class=\"\" width=\"20%\" alt=\"\"/></a>";

    } else if (msgObject == "live" || msgObject == "diantai" || msgObject == "LIVEPUSH") {
        var temp = null;
        if (msgContent.indexOf("<img>") != -1) {
            temp = msgContent.split("<img>");
            temp = temp[0] + "<br>" + "<img src=\"" + temp[1] + "\" width=\"20%\"/>"
        } else if (msgContent.indexOf("[图片]" != -1)) {
            temp = msgContent.split("[图片]");
            temp = temp[0] + "<img src=\"" + temp[1] + "\" width=\"20%\"/>"
        }
        return temp;

    } else if (msgObject == "VIDEO" || msgObject == "AUDIO") {
        var temp = null;
        if (msgContent.indexOf("[视频]<br>点击➡️") != -1) {
            temp = msgContent.replace("[视频]<br>点击➡️", "");
            return "<video width=\"320\" height=\"240\" controls>" +
                "<source src=\"" + temp + "\"  type=\"video/mp4\">" +
                "<source src=\"" + temp + "\"  type=\"video/ogg\">" +
                "您的浏览器不支持 HTML5 video 标签。" +
                "</video>";
        } else if (msgContent.indexOf("[语音]<br>点击➡️") != -1) {
            temp = msgContent.replace("[语音]<br>点击➡️", "");
            return "<audio controls>" +
                "<source src=\"" + temp + "\" type=\"audio/mpeg\">" +
                "<source src=\"" + temp + "\" type=\"audio/ogg\">" +
                "您的浏览器不支持 audio 元素。" +
                "</audio>";
        }

    } else {
        return msgContent;
    }
}

/**
 * 是否发送过QQ消息列处理
 * @param {*} value 
 * @param {*} row 
 * @param {*} index 
 */
var isSendHtml = function (value, row, index) {
    var isSend = row.isSend;
    if (isSend == 1) {
        isSend = "未发送";
    } else if (isSend == 2) {
        isSend = "已发送";
    }
    return isSend;
}

/**
 * 表格响应处理
 * @param {*} res 
 */
var responseHandler = function (res) {
    return {
        pageNum: res.data.number,
        total: res.data.totalElements,
        rows: res.data.content
    };
}

/**
 * 表格请求参数
 * @param {*} params 
 */
var queryParams = function (params) {
    var searchText = $("#input1").val();
    var msgObject = $("#select1").val();
    params["searchText"] = searchText;
    params["msgObject"] = msgObject;
    return params;
}

/**
 * 初始化#select1下拉框
 */
function installSelect1() {
    for (var t in message_type) {
        $("#select1").append("<option value='" + t + "'>" + message_type[t]
            + "</option>");
    }
}