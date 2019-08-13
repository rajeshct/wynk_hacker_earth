package tv.airtel.wynktest

import android.app.Application

class CustomApplication: Application() {
    companion object {
        @get:Synchronized
        lateinit var instance: CustomApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}