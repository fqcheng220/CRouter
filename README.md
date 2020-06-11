# CRouter
android通用路由库

1.支持retrofit service接口调用（路由表项定义） aspectj apt注解简化路由表注册（ARouter需要主动去指定package下加载类完成注册）  
2.路由查找、路由分发 支持拼接请求 目标页面对象的参数获取支持自动注入  
3.支持rxjava路由结果返回 异步rxjava拦截路由跳转 （ARouter是在当前线程等待执行）  
