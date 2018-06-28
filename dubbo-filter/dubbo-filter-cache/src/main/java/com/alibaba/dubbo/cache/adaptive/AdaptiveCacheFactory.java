package com.alibaba.dubbo.cache.adaptive;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.Invocation;

/**
 * 根据运行过程创建的CacheFactory适配工厂代码
 *
 * 方便调试跟踪
 *
 * @date 2018-06-17
 * @see ExtensionLoader
 * {@code createAdaptiveExtensionClass}
 * <p>
 * create by liaoxudong
 */
@Adaptive
public class AdaptiveCacheFactory implements CacheFactory {

    public Cache getCache(URL arg0, Invocation arg1) {
        if (arg0 == null) throw new IllegalArgumentException("url == null");
        URL url = arg0;
        if (arg1 == null) throw new IllegalArgumentException("invocation == null");
        String methodName = arg1.getMethodName();
        String extName = url.getMethodParameter(methodName, "cache", "lru");
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(CacheFactory) name from url(" + url.toString() + ") use keys([cache])");
        CacheFactory extension = ExtensionLoader.getExtensionLoader(CacheFactory.class).getExtension(extName);
        return extension.getCache(arg0, arg1);
    }
}
