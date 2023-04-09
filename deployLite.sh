#!/bin/zsh
#查看当前是否安装过jdk8及以上
## wget 无法下载jdk
#if [ ! -f jdk-8u202-linux-x64.tar.gz ]; then
#  wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=https%3A%2F%2Fwww.oracle.com%2Fcn%2Fjava%2Ftechnologies%2Fjavase%2Fjavase8-archive-downloads.html;oraclelicense=139" https://download.oracle.com/otn/java/jdk/8u202-b08/1961070e4c9b4e26a04e7f5a083f551e/jdk-8u202-linux-x64.tar.gz
#fi

if [ ! -d "/home/zhaojun/java" ]; then
  mkdir -p /home/zhaojun/java
fi
if [ ! -d "/home/zhaojun/java/jdk1.8.0_202" ]; then
  tar -xzvf jdk-8u202-linux-x64.tar.gz -C /home/zhaojun/java
fi

if [ $JAVA_HOME ]; then
  echo $JAVA_HOME
else
  echo "export JAVA_HOME=/home/zhaojun/java/jdk1.8.0_202" >> /etc/profile
  source /etc/profile
  echo "export CLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib" >> /etc/profile
  echo "export PATH=$JAVA_HOME/bin:$PATH" >> /etc/profile
  source /etc/profile
fi

if [ -f "landlords-server-1.0.0.jar" ]; then
  java -jar landlords-server-1.0.0.jar -p 1024
fi