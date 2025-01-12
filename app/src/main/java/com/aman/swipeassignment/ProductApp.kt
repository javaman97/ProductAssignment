package com.aman.swipeassignment

import android.app.Application
import com.aman.swipeassignment.di.AppModule.loadModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class ProductApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ProductApp)
            loadModules()
        }
    }
}