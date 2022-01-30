package com.rspackage.kotlindemo.support

import android.app.Application
import com.rspackage.kotlindemo.support.data.api.ApiFactory
import com.rspackage.kotlindemo.support.network.ConnectionInterceptor
import com.rspackage.kotlindemo.ui.main.viewmodel.MainRepository
import com.rspackage.kotlindemo.ui.main.viewmodel.MainviewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class DemoApplication : Application(), KodeinAware {

    companion object {
        lateinit var instance: DemoApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override val kodein = Kodein.lazy {
        import(androidXModule(this@DemoApplication))

        bind() from singleton { ConnectionInterceptor(instance()) }

        bind() from singleton { ApiFactory(instance()) }
      //  bind() from singleton { AppDatabase(instance()) }

        bind() from singleton { MainRepository(instance()/*, instance()*/) }
        bind() from singleton { MainviewModelFactory(instance()) }


    }

}