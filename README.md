# 少年科技AI平台
> 【前端演示】：http://ai.iamshaonian.top/ 
> 
>  体验账号：jiangzihao  
> 
>  密码：12345678
>
> 【后台管理端演示】：http://ai.iamshaonian.top:5601/
## 项目简介
少年科技AI平台：用户注册后可以通过该平台与不同AI助手对话，也可以自己创建AI助手，可以充值会员，购买次数等。管理员可以在后台管理端，管理用户信息，AI助手信息，用户对话信息，充值订单信息等。

## 技术栈
- Spring Boot 2.7.0
- Spring AOP 切面编程
- MySQL 8.0.29
- MyBatis + MyBatis-Plus
- Redis 6.2
- Websocket 
- Elasticsearch 7.17
- Hutool 工具库，Gson 解析库，Apache Commons Lang3 工具类
- 讯飞AI（目前只整合了讯飞AI,后期会继续补充）
## 项目特点
- 自定义 Prompt 预设模版， 可以创建不同特色的助手。
- 基于 Spring AOP 实现自定权限注解 @AuthCheck，控制用户访问权限。
- 通过 Redis 缓存用户对话记录，提升响应速度。
- 基于 Redisson 的 RateLimiter 实现限流，控制单用户的访问频率。
- Spring Session Redis 分布式登录。
- 通过 Websocket 即时通信，实现前端打字机式回复，提升用户体验。
- 使用 Logstash 定时同步 MySQL 中用户的对话记录到 Elasticsearch, 并统计用户对话词频，实现词云功能。
- 整合支付宝沙箱支付，实现充值功能。
## 其他
- 全局请求响应拦截器（记录日志）
- 全局异常处理器
- 封装通用响应类
- 全局跨域处理
- 多环境配置
- 自定义错误码
- Swagger + Knife4j 接口文档
## 项目部署
### 后端部署（jar包部署）
1. 项目打包，上传到服务器
2. 自定义启动脚本，和关闭脚本方便后续部署，目录结构如下

<img src="http://cdn.iamshaonian.top/202309102003201.png" alt="image-20230910195645868"  />

3. 启动脚本 start.sh

   ```shell
   nohup java -jar ../ai-platform.jar > ../output.txt 2> ../error.txt &
   echo "开启成功"
   ```

   

4. 关闭脚本 shutdown.sh

   ```shell
   #!/bin/bash
   pid=$(ps -ef|grep ai-platform.jar|grep -v grep|awk '{print $2}')
   
   if [ -z $pid ]; then
   	echo "进程不存在"
   else
   	echo "正在杀死进程"
   	kill -9 $pid
   	echo "进程杀死成功"
   fi
   
   ```

### 前端部署

 1. 通过 nginx 部署，修改nginx.conf文件，添加server

    ```nginx
    server
        {
            listen 80;
            server_name localhost;
            #error_page   404   /404.html;
            include enable-php.conf;
            
           location / {
                root   /root/ai-platform/ai-platform-front;
                index  index.html index.htm;
            }
            
            location /api{
              proxy_pass http://localhost:8101;
              proxy_send_timeout 12s;    
              #nginx代理默认不支持长连接，而websocket是基于长连接实现的
              #所以需要在nginx增加以下配置，使nginx代理支持长连接
              proxy_http_version 1.1;  
              proxy_set_header Upgrade $http_upgrade;
              proxy_set_header Connection "upgrade";
            }
            
            location /image {
              root /root/ai-platform;
            }
            
        }
    ```


2. 重载配置 ngixn reload