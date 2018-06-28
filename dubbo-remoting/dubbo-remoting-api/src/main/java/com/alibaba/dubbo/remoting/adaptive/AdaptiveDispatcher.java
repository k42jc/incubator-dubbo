package com.alibaba.dubbo.remoting.adaptive;

import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.Dispatcher;


/**
 * 根据运行过程创建的Dispatcher适配工厂代码
 * <p>
 * 方便调试跟踪
 *
 * @date 2018-06-17
 * @see ExtensionLoader
 * {@code createAdaptiveExtensionClass}
 * <p>
 * create by liaoxudong
 */
@Adaptive
public class AdaptiveDispatcher implements Dispatcher {

    public com.alibaba.dubbo.remoting.ChannelHandler dispatch(com.alibaba.dubbo.remoting.ChannelHandler arg0, com.alibaba.dubbo.common.URL arg1) {
        if (arg1 == null) throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg1;
        String extName = url.getParameter("dispatcher", url.getParameter("dispather", url.getParameter("channel.handler", "all")));
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.remoting.Dispatcher) name from url(" + url.toString() + ") use keys([dispatcher, dispather, channel.handler])");
        com.alibaba.dubbo.remoting.Dispatcher extension = (com.alibaba.dubbo.remoting.Dispatcher) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.remoting.Dispatcher.class).getExtension(extName);
        return extension.dispatch(arg0, arg1);
    }
}
