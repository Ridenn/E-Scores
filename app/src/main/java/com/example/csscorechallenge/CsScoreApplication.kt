package com.example.csscorechallenge

import android.app.Application
import com.example.csscorechallenge.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CsScoreApplication : Application()  {

    override fun onCreate() {
        super.onCreate()

        handleModules()
    }

    private fun handleModules() {
        startKoin {
            androidContext(this@CsScoreApplication)
            modules(
                listOf(
                    mainModule
                )
            )
        }
    }

}