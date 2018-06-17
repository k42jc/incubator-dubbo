package com.alibaba.dubbo.rpc.adaptive;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.ProxyFactory;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * 根据运行过程创建的ProxyFactory适配工厂代码
 *
 * 方便调试跟踪
 * @date 2018-06-17
 * @see ExtensionLoader
 * {@code createAdaptiveExtensionClass}
 * <p>
 * create by liaoxudong
 */
@Adaptive
public class AdaptiveProxyFactory implements ProxyFactory {

    public Invoker getInvoker(Object impl, Class interfaceClass, URL url) throws RpcException {
        if (url == null) throw new IllegalArgumentException("url == null");
        String extName = url.getParameter("proxy", "javassist");
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(ProxyFactory) name from url(" + url.toString() + ") use keys([proxy])");
        ProxyFactory extension = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(extName);
        return extension.getInvoker(impl, interfaceClass, url);
    }

    public Object getProxy(Invoker invoker, boolean arg1) throws RpcException {
        if (invoker == null) throw new IllegalArgumentException("Invoker argument == null");
        if (invoker.getUrl() == null)
            throw new IllegalArgumentException("Invoker argument getUrl() == null");
        URL url = invoker.getUrl();
        String extName = url.getParameter("proxy", "javassist");
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(ProxyFactory) name from url(" + url.toString() + ") use keys([proxy])");
        ProxyFactory extension = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(extName);
        return extension.getProxy(invoker, arg1);
    }

    public Object getProxy(Invoker invoker) throws RpcException {
        if (invoker == null) throw new IllegalArgumentException("Invoker argument == null");
        if (invoker.getUrl() == null)
            throw new IllegalArgumentException("Invoker argument getUrl() == null");
        URL url = invoker.getUrl();
        String extName = url.getParameter("proxy", "javassist");
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(ProxyFactory) name from url(" + url.toString() + ") use keys([proxy])");
        ProxyFactory extension = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(extName);
        return extension.getProxy(invoker);
    }
}
