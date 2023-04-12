# Landlords
环境要求：
![](https://img.shields.io/badge/java-1.8%2B-blue)  
* jdk8+(新手推荐jdk8,自带javafx), jdk11及以上去除了javafx，需要单独安装,高版本jdk可见下面参考链接，jdk17的代码已升级
* maven

基于java实现的斗地主小游戏，netty + javafx

基于: [javafx-ratel-client](https://github.com/marmot-z/javafx-ratel-client)
和[ratel命令行](https://github.com/ainilili/ratel)项目进行改造，由于javafx-ratel-client这个项目
长期没有人维护，所以我这里单独发一个项目

## 使用方法简单介绍

1. 运行SimpleServer,控制台会显示端口号，说明服务启动成功
2. 运行SimpleClient,连接本地的127.0.0.1的ip，刚才控制台展示的端口
（如果是一个人测试，可以使用idea开启允许多实例运行SimpleClient）
<img width="600" alt="image" src="https://user-images.githubusercontent.com/19192068/230633318-647c8a63-8e47-4d61-8c24-ea994ddd6792.png">
<img width="600" alt="image" src="https://user-images.githubusercontent.com/19192068/230633554-e99f4813-1344-4562-b182-73ecb322fa4b.png">
<img width="1200" alt="image" src="https://user-images.githubusercontent.com/19192068/230633700-f4158537-03ff-4b22-9fd7-09ad317539cc.png">



### 添加内容
* 4人斗地主（2付牌）
* 添加炸弹牌型展示提示
* 地主标签高亮
* 添加出牌提示

### 进行中的内容...
* 添加拖拽选牌
* 添加"要不起"提示
* 添加只能机器人（AI）
* 托管机制
* 时间结束，交给托管
* 断线重连


### 修改的bug
* 下游玩家出的牌有遮挡，看不清第二行
* 其他玩家大于16张牌是，会遮挡
* 修复出牌不符合牌型或者过小时，出现按钮点不动的情况

### 待修改的bug
* 第一列出牌，其他人的计时器不见了
* 有人退掉，房间不能结束，所有人推掉房间才结束



#### native打包方式
[packr](https://github.com/libgdx/packr)
[参考连接](https://blog.csdn.net/weixin_44480167/article/details/121318205)  


交流群：  
<img width="274" alt="image" src="https://user-images.githubusercontent.com/19192068/231477324-1644b750-85d1-472c-8c0b-23fda8e3be4d.png">



