# general-netty
基础netty服务,总的规划是,netty-http服务,然后是netty-rpc服务。代码持续更新
## 1.使用说明
启动http server,运行com.yugioh.netty.http.server.HttpNettyServerMain的main方法,可在resource/config/main.properties中配置相关启动参数
## 参数 ##
### 公共参数 ###
| 参数名     | 含义         | 是否必传           | 示例                             | 类型   |
| ---------- | ------------ | ------------------ | -------------------------------- | ------ |
| appId      | 接口调用凭证 | 是                 | 5F29F10E1459C45F5D70BBBD227CC936 | String |
| timestamp  | 时间戳       | 是                 | 1493127901776                    | long   |
| nonceStr   | 随机字符串   | 是                 | UrLldtPeerMpBQXaknhVgUUcqzUJmuWq | String |
| encrypt    | 加密类型     | 否                 | AES                              | String |
| encryptKey | 加密密钥     | 否(混合加密时必传) | 12345678                         | String |
| body       | 请求业务参数 | 否(视具体业务而定) | {"orderNo" : "1234567890"}       | String |
| sign       | 签名         | 是                 | 19881281BBCC16C87631F8D73D06F7D4 | String |

## 响应

### 公用响应

| 参数名   | 含义                                                         | 是否必传 | 示例                             | 类型   |
| -------- | ------------------------------------------------------------ | -------- | -------------------------------- | ------ |
| code     | 状态码(基本含义同http状态码,特殊,当code小于200时,客户端不会收到签名) | 是       | 200                              | int    |
| time     | 时间戳                                                       | 是       | 1493127901776                    | long   |
| nonceStr | 随机字符串                                                   | 是       | UrLldtPeerMpBQXaknhVgUUcqzUJmuWq | String |
| msg      | 业务处理消息                                                 | 否       | 您无此权限                       | String |
| body     | 业务数据                                                     | 否       | {"orderNo" : "1231231"}          | String |
| sign     | 签名                                                         | 是       | 19881281BBCC16C87631F8D73D06F7D4 | String |

## 2.http服务开发 ##

### 1.常规http服务(常规http服务强制要求使用上述公用参数和公用响应格式)

1. 创建处理业务Handle继承com.yugioh.netty.http.server.business.BaseHandle
2. 重写handle和checkParam方法
3. 使用com.yugioh.netty.http.server.annotation.RequestMapping注解配置访问路由
4. 测试

### 2.自定义http服务(自定义http服务不强制使用,常用语对接三方服务时,三方服务通知)

1. 创建处理业务Handle继承com.yugioh.netty.http.server.business.BaseHandle
2. 重写freeHandle方法
3. 使用com.yugioh.netty.http.server.annotation.RequestMapping注解配置访问路由
4. 使用com.yugioh.netty.http.server.annotation.CustomHandle注解标识为自定义服务
5. 测试

注意：添加了@CustomHandle注解会强制校验freeHandle方法的存在，如果不存在不会成为http服务,相对于的,没有使用@CustomHandle注解则会强制校验handle方法的存在,如果不存在,也不会被注册成为服务。

## 3.附录1-参数加密方式 ##

使用混合加密的原因是,RSA(非对称加密)相对来说较为安全,但是加解密速度慢，而AES/DES相对来说没那么安全,但是速度高效。

- AES

  常规的AES加密,密钥由接口方颁发

- DES

  常规的DES加密,密钥由接口方颁发

- RSA

  调用方使用公钥加解密,接口方使用私钥加解密。密钥对长队为2048，加密分段为245，解密分段为256。加密后数据使用base64。

- RSA+AES

  使用动态的AES密钥对参数加密,然后使用RSA公钥对AES的密钥加密,加密后密钥传入encryptKey字段，这种状态下，返回的业务数据也会使用相同方式的密钥加密

- RSA+DES

  使用动态的DES密钥对参数加密,然后使用RSA公钥对DES的密钥加密,加密后密钥传入encryptKey字段，这种状态下，返回的业务数据也会使用相同方式的密钥加密

## 4.附录2-参数签名

签名使用仅针对常规http服务：(同微信签名)

第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。

特别注意以下重要规则：

1. ◆ 参数名ASCII码从小到大排序（字典序）；
2. ◆ 如果参数的值为空不参与签名；
3. ◆ 参数名区分大小写；
4. ◆ 验证调用返回或微信主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。
5. ◆ 微信接口可能增加字段，验证签名时必须支持增加的扩展字段

第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。

## 5.待解决问题：

1. 多个handle使用相同的路由,spring的方式是启动直接报错，然后启动不成功，采用同样的方式？