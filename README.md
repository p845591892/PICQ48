# PICQ48
基于酷Q的可视化SNH48机器人 / 口袋房间 / 微博 / 摩点 / ... 监控

本项目基于zztyyh项目进行改造，重构数据监控模块逻辑，重写发送消息模块，但未改变表数据结构，前端页面也未做更改。<br/>
原项目地址：https://github.com/p845591892/zztyyh  <br/>
本项目的QQ交互部分使用基于酷Q的HTTP插件：https://github.com/HyDevelop/PicqBotX

软件：酷Q Air / 酷Q Pro Windows环境下安装

数据库：MySQL 5.7.2x 编码**utf8mb4**（为了保存emoji表情）

项目主体：Spring Boot 2.2.0 + JDK 1.8

持久层框架：JPA（spring-boot-starter-data-jpa） + MyBatis（mybatis-spring-boot-starter 2.1.0）

模板引擎：Thymeleaf（spring-boot-starter-thymeleaf）

轮询框架：Quartz（spring-boot-starter-quartz）

安全框架：Shiro 1.4.0（shiro-spring 1.4.0）

缓存：jedis 2.9.0（shiro-redis 3.2.3）

酷Q的HTTP插件：PicqBotX 4.12.0.991.PRE

<br/>
<br/>

# 开发环境配置
application.yml为通用配置，这边的内容几乎可以不更改，而 *spring.profiles.active* 启动不同环境的配置，这里我只提供了dev，需要根据自己实际的开发环境进行修改，内需要人为手动修改的配置都为 ***${}*** 这样的形式。

<br/>
<br/>

## 截止 的更新：
* 基于酷Q指令功能的信息查询、监控设置、QQ管理等若干指令。
* 基于酷Q监听功能的若干监听反馈，例如欢迎新群员等。
* 实现了口袋房间的语音转发。

<br/>

# 主要功能(初始化)
## 轮询模块
* 同步SNH48成员信息<br/>
* 同步并发送口袋48房间消息<br/>
* 同步微博用户信息<br/>
* 同步并发送微博用户动态<br/>

<br/>

## 配置管理模块
* 口袋48房间监控配置<br/>
  （1）成员房间监控开关<br/>
  （2）设置监控后消息发送的QQ群/QQ<br/>
  （3）设置关键字监控<br/>
  （4）多条件查询SNH48成员<br/>
* 微博用户的动态监控配置<br/>
  （1）新增监控的微博用户<br/>
  （2）设置监控后动态发送的QQ群/QQ<br/>
* 摩点集资项目监控配置<br/>
  （1）新增监控的摩点集资项目<br/>
  （2）设置监控后集资动态发送的QQ群/QQ
* 定时任务配置（管理轮询模块）<br/>
  （1）新增定时任务<br/>
  （2）启动定时任务<br/>
  （3）停止定时任务<br/>
  （4）修改定时任务<br/>
  
<br/>

## 数据展示模块
* 摩点集资项目的集资统计
* 多条件查询已监控到的口袋48房间消息

