package com.crp.wikisearcher

import android.app.Application
import com.crp.wikisearcher.di.networkModule
import com.crp.wikisearcher.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WikiApp :Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WikiApp)
            modules(listOf(networkModule,viewModelModule))
        }

    }
}