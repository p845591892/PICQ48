var username, password, checkPassword, nickname, email;// 注册信息变量

$(document).ready(function() {

	/*
	 * 获得input焦点：清除msg内容，清除特殊class
	 */
	$("input").focus(function() {
		var input = $(this);
		var inputName = input.attr("name")
		input.removeClass("success-input");
		input.removeClass("error-input");

		if (inputName == "username") {
			$("#usernameMsg").html("");
		} else if (inputName == "password") {
			$("#passwordMsg").html("");
		} else if (inputName == "checkPassword") {
			$("#checkPasswordMsg").html("");
		} else if (inputName == "nickname") {
			$("#nicknameMsg").html("");
		}  else if (inputName == "email") {
			$("#emailMsg").html("");
		} 
	});

	/*
	 * 失去input焦点：进行验证，并写入msg，添加特殊class
	 */
	$("input").blur(function() {
		var input = $(this);
		var inputName = input.attr("name")
		if (inputName == "username") {
			username = input.val();
			validateUsername(input);
		} else if (inputName == "password") {
			password = input.val();
			validatePassword(input);
			validateCheckPassword($("input[name='checkPassword']"));
		} else if (inputName == "checkPassword") {
			checkPassword = input.val();
			validateCheckPassword(input);
			validatePassword($("input[name='password']"));
		} else if (inputName == "nickname") {
			nickname = input.val();
			validateNickname(input);
		}  else if (inputName == "email") {
			email = input.val();
			validateEmail(input);
		} 
	});

	/*
	 * 点击注册
	 */
	$("#register").click(function() {
		var inputs = $("input");
		for (var i = 0; i < inputs.length; i++) {
			var input = $(inputs[i]);
			var inputClass = input.attr("class");
			if (inputClass.indexOf("success-input") == -1) {
				var inputName = input.attr("name");
				if (inputName == "username") {
					validateUsername(input);
				} else if (inputName == "password") {
					validatePassword(input);
					validateCheckPassword($("input[name='checkPassword']"));
				} else if (inputName == "checkPassword") {
					validateCheckPassword(input);
					validatePassword($("input[name='password']"));
				} else if (inputName == "nickname") {
					validateNickname(input);
				}
				layerMsg(417, "含有不符合规范的信息");
				return;
			}
		}
		openLoad();
		$.ajax({
			url : "/doRegister",
			data : {
				"username" : username,
				"password" : password,
				"nickname" : nickname,
				"email" : email
			},
			type : "post",
			success : function(data) {
				closeLoad();
				if (data.status == 200) {
					layer.confirm(/*"注册成功，请注意查收邮件"*/"注册成功，请联系管理员【微博@JuFF_白羽】激活账号和赋权。", {
						icon : 6,
						title : '提示'
					}, function(index, layero) {
						// 按钮【按钮一】的回调
						window.location.href = "/login";
					}, function(index) {
						// 按钮【按钮二】的回调
						window.location.href = "/index";
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
	});

});

/*
 * 验证用户名
 */
function validateUsername(doc) {
	$.ajax({
		url : "/register/validate-username",
		data : {
			"username" : username
		},
		type : "post",
		success : function(data) {
			if (data.status == 200) {
				doc.addClass("success-input");
				registerState = true;
			} else {
				doc.addClass("error-input");
				$("#usernameMsg").html(data.cause);
				registerState = false;
			}
		},
		error : function(data) {
			closeLoad();
			layerMsg(500, "请求失败");
			registerState = false;
		}
	});
}

/*
 * 验证密码
 */
function validatePassword(doc) {
	if (password == null || password.trim() == "") {
		doc.addClass("error-input");
		$("#passwordMsg").html("密码不能为空");
	} else {
		doc.addClass("success-input");
	}
}

/*
 * 验证确认密码
 */
function validateCheckPassword(doc) {
	if (checkPassword == null || checkPassword.trim() == "") {
		doc.addClass("error-input");
		$("#checkPasswordMsg").html("确认密码不能为空");
	} else if (password != checkPassword) {
		doc.addClass("error-input");
		$("#checkPasswordMsg").html("密码不一致");
	} else {
		doc.addClass("success-input");
		$("#checkPasswordMsg").html("");
	}
}

/*
 * 验证昵称
 */
function validateNickname(doc) {
	if (nickname == null || nickname.trim() == "") {
		doc.addClass("error-input");
		$("#nicknameMsg").html("昵称不能为空");
	} else {
		doc.addClass("success-input");
	}
}

/*
 * 验证邮箱
 */
function validateEmail(doc) {
	if (email == null || email.trim() == "") {
		doc.addClass("error-input");
		$("#emailMsg").html("昵称不能为空");
	}
	var re = /^[A-Za-z\d]+([-_.][A-Za-z\d]+)*@([A-Za-z\d]+[-.])+[A-Za-z\d]{2,4}$/; 
    if (re.test(email)) {
    	doc.addClass("success-input");
    } else {
    	doc.addClass("error-input");
		$("#emailMsg").html("邮箱格式不正确");
    }
}