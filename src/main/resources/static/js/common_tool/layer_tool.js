// 本js为自定义的layer的通用方法。 

var layerLoad = null;// 加载层对象


/*
 * layer的msg弹窗: 根据返回的状态码不同，弹出不同图标的消息窗。
 *  参数1：status 状态码，例如200、400、403
 *  参数2：msg 要弹出的消息
 */
function layerMsg(status, msg) {
	var i = 0;
	if (status == 500) {
		i = 5;
	} else if (status == 403) {
		i = 4;
	} else if (status == 417) {
		i = 0;
	} else if (status == 400) {
		i = 2;
	} else if (status == 200) {
		i = 1;
		msg = "操作成功";
	}
	layer.msg(msg, {
		icon : i
	});
}

/* 开启加载层 */
function openLoad() {
	layerLoad = layer.load(0, {
		time : 120 * 1000
	});
}

/* 关闭加载层 */
function closeLoad() {
	layer.close(layerLoad);
}