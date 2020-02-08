// 本js为自定义的layer的通用方法。 

var layerLoad = null; // 加载层对象

/*
 * layer的msg弹窗: 根据返回的状态码不同，弹出不同图标的消息窗。 参数1：status 状态码，例如200、400、403 参数2：msg
 * 要弹出的消息
 */
function layerMsg(status, msg) {
	if (status == 200) {
		msg = "操作成功";
	}
	layer.open({
		type : 0,
		content : msg,
		skin : "footer",
		time : 2
	});
}

/* 开启加载层 */
function openLoad() {
	layerLoad = layer.open({
		type : 2,
		shadeClose : false,
		time : 120
	});
}

/* 关闭加载层 */
function closeLoad() {
	layer.close(layerLoad);
}
