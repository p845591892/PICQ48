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
  <title>流程部署</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport"
        content="width=device-width,user-scalable=yes, minimum-scale=0.4, initial-scale=0.8,target-densitydpi=low-dpi"/>
  <link rel="stylesheet" href="/plugin/layui/css/layui.css">
  <link rel="stylesheet" href="/plugin/lenos/main.css">
  <script type="text/javascript" src="/plugin/jquery/jquery-3.2.1.min.js"></script>
  <script type="text/javascript" src="/plugin/layui/layui.all.js"
          charset="utf-8"></script>
  <script type="text/javascript" src="/plugin/tools/tool.js" charset="utf-8"></script>
</head>

<body>
<div class="lenos-search">
  <div class="select">
    部署id：
    <div class="layui-inline">
      <input class="layui-input" height="20px" id="deploymentId" autocomplete="off">
    </div>
    流程名称：
    <div class="layui-inline">
      <input class="layui-input" height="20px" id="name" autocomplete="off">
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
     <#-- <button class="layui-btn layui-btn-normal" id="processGroup" data-type="assignee">
        <i class="layui-icon">&#xe642;</i>节点处理人设置
      </button>-->
    </div>
</div>

<table id="actList" class="layui-hide" lay-filter="act"></table>
<script type="text/html" id="toolBar">
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon">&#xe640;</i>删除</a>
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
      id: 'actList',
      elem: '#actList'
      , url: 'showAct'
      , cols: [[
          {checkbox: true, fixed: true, width: '5%'}
        , {field: 'id', title: '编号', width: '15%', sort: true}
        , {field: 'name', title: '流程名称', width: '10%', sort: true}
        , {field: 'key', title: 'key', width: '12%', sort: true}
        , {field: 'deploymentId', title: '部署id', width: '5%', sort: true}
        , {field: 'diagramResourceName', title: '流程图资源', width: '15%', sort: true}
        , {field: 'category', title: '版本', width: '15%', sort: true}
        , {field: 'resourceName', title: '资源名称', width: '10%', sort: true}
        , {field: 'text', title: '操作', width: '10%', toolbar:'#toolBar'}

      ]]
      , page: true
      ,  height: 'full-85'
    });

    var $ = layui.$, active = {
      select: function () {
        var deploymentId = $('#deploymentId').val();
        var name = $('#name').val();
        table.reload('actList', {
          where: {
            deploymentId: deploymentId,
            name: name
          }
        });
      }
      /*,assignee:function(){
        var checkStatus = table.checkStatus('actList')
            , data = checkStatus.data;
        if (data.length !=1) {
          layer.msg('请选择一个流程', {icon: 5});
          return false;
        }
        assignee(data[0].id,data[0].deploymentId);
      }*/
      ,reload:function(){
        $('#deploymentId').val('');
        $('#name').val('');
        table.reload('actList', {
          where: {
            deploymentId: null,
            name: null
          }
        });
      }
    };
    //监听工具条
      table.on('tool(act)', function (obj) {
      var data = obj.data;
      if (obj.event === 'del') {
        layer.confirm('将会删除正在执行的流程,确定删除？', {
          btn: ['确定','取消'] //按钮
        }, function(){
          del(data.deploymentId);
        }, function(){
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
      url: "delDeploy",
      type: "post",
      data: {id: id},
      dataType: "json", traditional: true,
      success: function (d) {
        if(d.flag){
          layer.msg(d.msg, {icon: 6});
          layui.table.reload('actList');
        }else{
          layer.msg(d.msg, {icon: 5});
        }
      }
    });
  }
  /**
   * 流程绑定节点
   * @param id
   */
 /* function assignee(id,deploymentId){
    var index =
        layer.open({
          id: 'assignee',
          type: 2,
          area: ['600px',  '350px'],
          fix: false,
          maxmin: true,
          shadeClose: false,
          shade: 0.4,
          title: '设置流程节点',
          content: 'goAssignee/'+deploymentId
        });
    layer.full(index);
  }*/


</script>
</body>

</html>
