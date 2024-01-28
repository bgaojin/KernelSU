package me.weishu.kernelsu

import android.app.Application
import coil.Coil
import coil.ImageLoader
//import com.tencent.bugly.crashreport.CrashReport
import me.weishu.kernelsu.utils.SpUtils
import me.zhanghai.android.appiconloader.coil.AppIconFetcher
import me.zhanghai.android.appiconloader.coil.AppIconKeyer

lateinit var ksuApp: KernelSUApplication

class KernelSUApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ksuApp = this

//        CrashReport.initCrashReport(applicationContext, "44ff331451", false)

        SpUtils.getInstance().initSp(ksuApp)
        val context = this
        val iconSize = resources.getDimensionPixelSize(android.R.dimen.app_icon_size)
        Coil.setImageLoader(
            ImageLoader.Builder(context)
                .components {
                    add(AppIconKeyer())
                    add(AppIconFetcher.Factory(iconSize, false, context))
                }
                .build()
        )
    }


}