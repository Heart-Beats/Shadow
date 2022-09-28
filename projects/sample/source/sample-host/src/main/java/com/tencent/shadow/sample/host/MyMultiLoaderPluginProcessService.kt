package com.tencent.shadow.sample.host

import android.content.pm.ApplicationInfo
import android.content.res.Resources
import android.util.Log
import com.tencent.shadow.dynamic.host.MultiLoaderPluginProcessService
import com.tencent.shadow.sample.host.lib.LoadPluginCallback

/**
 * @author  张磊  on  2022/09/27 at 10:55
 * Email: 913305160@qq.com
 */
class MyMultiLoaderPluginProcessService: MultiLoaderPluginProcessService(){
    init {
        LoadPluginCallback.setCallback(object : LoadPluginCallback.Callback {
            override fun beforeLoadPlugin(partKey: String) {
                Log.d("PluginProcessPPS", "beforeLoadPlugin($partKey)")
            }

            override fun afterLoadPlugin(
                partKey: String,
                applicationInfo: ApplicationInfo,
                pluginClassLoader: ClassLoader,
                pluginResources: Resources
            ) {
                Log.d(
                    "PluginProcessPPS",
                    "afterLoadPlugin(" + partKey + "," + applicationInfo.className + "{metaData=" + applicationInfo.metaData + "}" + "," + pluginClassLoader + ")"
                )
            }
        })
    }
}