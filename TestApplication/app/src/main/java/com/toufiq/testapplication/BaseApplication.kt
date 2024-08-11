package com.toufiq.testapplication

import android.app.Application


class BaseApplication : Application() {



    override fun onCreate() {
        appInstance = this
        super.onCreate()
    }


    companion object {

        private var appInstance: BaseApplication? = null

        val instance: BaseApplication
            @Synchronized get() {
                if (appInstance == null) {
                    appInstance =
                        BaseApplication()
                }
                return appInstance!!
            }
    }

}