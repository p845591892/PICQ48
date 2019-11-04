<#-- Created by IntelliJ IDEA.
 User: zxm
 Date: 2018/1/15
 Time: 16:53
 To change this template use File | Settings | File Templates.
流程部署-->
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>模型列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport"
        content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8"/>
  <link rel="stylesheet" href="/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="/plugin/lenos/main.css"/>
  <script type="text/javascript" src="/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="/plugin/layui/layui.all.js"
          charset="utf-8"></script>
  <script type="text/javascript" src="/plugin/tools/tool.js" charset="utf-8"></script>
</head>

<body>
<div class="lenos-search">
  <div class="select">
    模型名称：
    <div class="layui-inline">
      <input class="layui-input" height="20px" id="name" autocomplete="off">
    </div>
    key：
    <div class="layui-inline">
      <input class="layui-input" height="20px" id="key" autocomplete="off">
    </div>
    <button class="select-on layui-btn layui-btn-sm" data-type="select"><i class="layui-icon"></i>
    </button>
    <button class="layui-btn layui-btn-sm icon-position-button" id="refresh" style="float: right;"
            data-type="reload">
      <i class="layui-icon">ဂ</i>
    </button>
  </div>
</div>
<div class="layui-col-md12">
    <div class="layui-btn-group">
        <button class="layui-btn layui-btn-normal" data-type="syncdata">
            <i class="layui-icon">&#xe618;</i>同步数据
        </button>
      <button class="layui-btn layui-btn-normal" id="processGroup" data-type="add">
        <i class="layui-icon">&#xe642;</i>新建流程
      </button>
    </div>
  </button>
</div>

<table id="actModelList" class="layui-hide" lay-filter="act"></table>
<script type="text/html" id="toolBar">
  <shiro.hasPermission name="control:del">
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="update"><i class="layui-icon">&#xe640;</i>编辑</a>
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="open"><i class="layui-icon">&#xe640;</i>发布</a>
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon">&#xe640;</i>删除</a>
  </shiro.hasPermission>
</script>
<script>
  layui.laytpl.toDateString = function(d, format){
    var date = new Date(d || new Date())
        ,ymd = [
      this.digit(date.getFullYear(), 4)
      ,this.digit(date.getMonth() + 1)
      ,this.digit(date.getDate())
    ]
        ,hms = [
      this.digit(date.getHours())
      ,this.digit(date.getMinutes())
      ,this.digit(date.getSeconds())
    ];

    format = format || 'yyyy-MM-dd HH:mm:ss';

    return format.replace(/yyyy/g, ymd[0])
    .replace(/MM/g, ymd[1])
    .replace(/dd/g, ymd[2])
    .replace(/HH/g, hms[0])
    .replace(/mm/g, hms[1])
    .replace(/ss/g, hms[2]);
  };
  $('#processGroup').on('mouseover',function(){
    layer.tips('设置流程节点的代办人/候选人/候选组，目前只开发到组', this,{time:2000});
  });
  //数字前置补零
  layui.laytpl.digit = function(num, length, end){
    var str = '';
    num = String(num);
    length = length || 2;
    for(var i = num.length; i < length; i++){
      str += '0';
    }
    return num < Math.pow(10, length) ? str + (num|0) : num;
  };

  document.onkeydown = function (e) { // 回车提交表单
    var theEvent = window.event || e;
    var code = theEvent.keyCode || theEvent.which;
    if (code == 13) {
      $(".select .select-on").click();
    }
  }

  layui.use('table', function () {
    var table = layui.table;
    //方法级渲染
    table.render({
      id: 'actModelList',
      elem: '#actModelList'
      , url: 'showAm'
      , cols: [[
          {checkbox: true, fixed: true, width: '5%'}
        , {field: 'id', title: '编号', width: '10%', sort: true}
        , {field: 'name', title: '流程名称', width: '10%', sort: true}
        , {field: 'key', title: 'key', width: '10%', sort: true}
        , {field: 'version', title: '版本', width: '10%', sort: true}
        , {field: 'createTime', title: '创建时间', width: '20%',templet: '<div>{{ layui.laytpl.toDateString(d.createTime,"yyyy-MM-dd HH:mm:ss") }}</div>'}
        , {field: 'text', title: '操作', width: '20%', toolbar:'#toolBar'}

      ]]
      , page: true
      ,  height: 'full-85'
      , response: { //定义后端 json 格式，详细参见官方文档
            statusName: 'status', //状态字段名称
            statusCode: '200', //状态字段成功值
            msgName: 'cause', //消息字段
            //countName: 'Total', //总数字段
            dataName: 'data' //数据字段
        }
      
    });

    var $ = layui.$, active = {
      select: function () {
        var name = $('#name').val();
        var key = $('#key').val();
        table.reload('actModelList', {
          where: {
            name: name,
            key: key
          }
        });
      },
     syncdata:function () {
        syncdata();
      }
      ,reload:function(){
       $('#name').val('');
       $('#key').val('');
        table.reload('actModelList', {
          where: {
            name: null,
            key: null
          }
        });
      },add:function(){
        var index =layer.open({
          id: 'act-add',
          type: 2,
          area: ['600px','350px'],
          fix: false,
          maxmin: true,
          shadeClose: false,
          shade: 0.4,
          title: '添加流程',
          content: 'goActiviti'
        });
        layer.full(index);
      }
    };
    //监听工具条
    table.on('tool(act)', function (obj) {
      var data = obj.data;
      if (obj.event === 'open') {
        open(data.id);
      }else if(obj.event === 'update'){
        var index =layer.open({
              id: 'act-update',
              type: 2,
              area: ['600px','350px'],
              fix: false,
              maxmin: true,
              shadeClose: false,
              shade: 0.4,
              title: '编辑流程',
              content: 'actUpdate/'+data.id
            });
        layer.full(index);
      }else if(obj.event === 'del') {
        layer.confirm('确定删除[' + data.name + ']？', {
          btn: ['确定', '取消'] //按钮
        }, function () {
          del(data.id);
        }, function () {
        });
      }
    });

    $('.layui-col-md12 .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });
    $('.select .layui-btn').on('click', function () {
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });

  });
  function del(id) {
    $.ajax({
      url: "delModel",
      type: "post",
      data: {id: id},
      dataType: "json", traditional: true,
      success: function (d) {
        if(d.flag){
          layer.msg(d.msg, {icon: 6});
          layui.table.reload('actModelList');
        }else{
          layer.msg(d.msg, {icon: 5});
        }
      }
    });
  }
  function syncdata() {
    $.ajax({
      url: "syncdata",
      type: "post",
      dataType: "json", traditional: true,
      success: function (data) {
        layer.msg(data.cause, {icon: 6});
        //layui.table.reload('actModeltList');
      }
    });
  }
  function open(id) {
    $.ajax({
      url: "open",
      type: "post",
      data: {id: id},
      dataType: "json", traditional: true,
      success: function d(data) {
        layer.msg(data.cause, {icon: 6});
        layui.table.reload('actModelList');
      }
    });
  }

</script>
</body>

</html>
