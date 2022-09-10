package com.tencent.shadow.sample.host

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.hl.shadow.Shadow
import com.hl.shadow.logger.AndroidLoggerFactory
import com.hl.shadow.logger.LogLevel
import com.tencent.shadow.core.common.LoggerFactory
import com.tencent.shadow.dynamic.host.DynamicRuntime

class MyApplication : Application() {

	override fun onCreate() {
		super.onCreate()

		// InitApplication.onApplicationCreate(this);

		if (isMainProcess()) {

			Shadow.initShadowLog(LogLevel.DEBUG) { logLevel, message, t ->

			}
		} else if (this.isProcess(":plugin")) {
			println("当前为插件进程--------------------")

			// if (BuildConfig.DEBUG) {
			// 	// 多进程时调试使用 ，该函数会等待调试器attach（附着进程）。该函数在调试器attach后立刻返回，
			// 	Debug.waitForDebugger()
			// }

			try {
				LoggerFactory.getILoggerFactory()
			} catch (e: Exception) {
				// 插件进程也有 log 打印需要初始化
				val logLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.INFO
				LoggerFactory.setILoggerFactory(AndroidLoggerFactory.getInstance(logLevel) { logLevel, message, t ->

				})
			}

			//在全动态架构中，Activity组件没有打包在宿主而是位于被动态加载的 runtime，
			//为了防止插件crash后，系统自动恢复crash前的Activity组件，此时由于没有加载runtime而发生classNotFound异常，导致二次crash
			//因此这里恢复加载上一次的runtime
			DynamicRuntime.recoveryRuntime(this)

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
				WebView.setDataDirectorySuffix("plugins")
			}
		}
	}
}