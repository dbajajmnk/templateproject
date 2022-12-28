package com.example.templateproject.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.example.templateproject.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class TemplateApp: Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        startKoin {
            androidLogger()
            androidContext(this@TemplateApp)
            modules(appModule)
        }
    }
}