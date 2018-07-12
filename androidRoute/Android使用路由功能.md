# 路由的定义

> 不加密
## ucs://100?param1=1&param2=2&param3=3
## http://www.baidu.com
## msg://敬请期待
## image://http://www.baidu.com.png

> 加密
ucs://xxxx?param1=1&param2=2&param3=3

或者自定义的协议

# 用法:

在项目之中定义一个config来处理自定义的操作

public class CustomRouteConfig implements UcsSchemeConfig {

    @Override
    public IUcsPlatForm getPlotForm(RouteUri uri) {

        if (uri.scheme().equals(UcsScheme.UCS.toString())) {
            return new UcsPlatForm();
        }
        return null;
    }
}


其中 uri.scheme() 对应协议之中的ucs/http/https/msg/image/... 根据不同的scheme返回处理的platform

然后加上配置就可以正常使用了

        UcsRouteConfig.getConfig().setUcsSchemeConfig(new CustomRouteConfig());



例如:

    String ucsUrl = "ucs://100?param1=1&param2=2&param3=3";
     
     new UcsRoute.Builder(this).uri(RouteUri.parse(url)).create().go();
     
这样子就能跳转到100对应的页面,在mainfest之中配置

<activity android:name=".ui.TestActivity">
            <meta-data
                android:name="tag"
                android:value="100,101,102" />
        </activity>
        
