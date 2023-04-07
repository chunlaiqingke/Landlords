# Landlords
基于java实现的斗地主小游戏，netty + javafx

基于: [javafx-ratel-client](https://github.com/marmot-z/javafx-ratel-client)
和[ratel命令行](https://github.com/ainilili/ratel)项目进行改造，由于javafx-ratel-client这个项目
长期没有人维护，所以我这里单独发一个项目

## 使用方法简单介绍
环境要求：
* jdk8+(推荐jdk8,自带javafx), jdk11及以上去除了javafx，需要单独安装
* maven
1. 运行SimpleServer,控制台会显示端口号，说明服务启动成功
2. 运行SimpleClient,连接本地的127.0.0.1的ip，刚才控制台展示的端口
（如果是一个人测试，可以使用idea开启允许多实例运行SimpleClient）


### 添加内容
* 4人斗地主（2付牌）

### 进行中的内容...
* 添加拖拽选牌
* 添加炸弹牌型展示提示
* 添加出牌提示
* 添加"要不起"提示
* 添加只能机器人（AI）

### 修改的bug
* 下游玩家出的牌有遮挡，看不清第二行
