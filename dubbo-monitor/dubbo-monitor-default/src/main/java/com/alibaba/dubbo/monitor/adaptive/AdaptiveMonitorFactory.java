package com.alibaba.dubbo.monitor.adaptive;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.monitor.Monitor;
import com.alibaba.dubbo.monitor.MonitorFactory;

/**
 * 根据运行过程创建的MonitorFactory适配工厂代码
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
public class AdaptiveMonitorFactory implements MonitorFactory {

    public Monitor getMonitor(URL arg0) {
        if (arg0 == null) throw new IllegalArgumentException("url == null");
        URL url = arg0;
        String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(MonitorFactory) name from url(" + url.toString() + ") use keys([protocol])");
        MonitorFactory extension = ExtensionLoader.getExtensionLoader(MonitorFactory.class).getExtension(extName);
        return extension.getMonitor(arg0);
    }
}
