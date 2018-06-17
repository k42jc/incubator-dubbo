package com.alibaba.dubbo.common.threadpool.adaptive;

import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.threadpool.ThreadPool;

/**
 * 根据运行过程创建的ThreadPool适配工厂代码
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
public class AdaptiveThreadPool implements ThreadPool {

    public java.util.concurrent.Executor getExecutor(com.alibaba.dubbo.common.URL arg0) {
        if (arg0 == null) throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg0;
        String extName = url.getParameter("threadpool", "fixed");
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.common.threadpool.ThreadPool) name from url(" + url.toString() + ") use keys([threadpool])");
        com.alibaba.dubbo.common.threadpool.ThreadPool extension = (com.alibaba.dubbo.common.threadpool.ThreadPool) ExtensionLoader.getExtensionLoader(com.alibaba.dubbo.common.threadpool.ThreadPool.class).getExtension(extName);
        return extension.getExecutor(arg0);
    }
}
