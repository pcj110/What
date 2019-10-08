package com.yyxnb.arch.base

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.github.anzewei.parallaxbacklayout.ParallaxHelper
import com.jeremyliao.liveeventbus.LiveEventBus
import com.yyxnb.arch.Arch
import com.yyxnb.arch.utils.log.LogUtils
import me.jessyan.autosize.AutoSizeConfig


/**
 * Description: BaseApplication
 *
 * @author : yyx
 * @date ：2018/6/11
 */
open class BaseApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //突破65535的限制
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        Arch.init(this)

        AutoSizeConfig.getInstance().isCustomFragment = true

        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance())

        LiveEventBus
                .config()
                .supportBroadcast(applicationContext)
                .lifecycleObserverAlwaysActive(true)
                .autoClear(false)
        LogUtils.init()
                .setTag("Test")//设置全局tag
                .setShowThreadInfo(true).setDebug(Arch.isDebug) //是否显示日志，默认true，发布时最好关闭

    }

}
