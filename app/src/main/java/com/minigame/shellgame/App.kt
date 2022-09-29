package com.minigame.shellgame

import android.app.Application
import com.minigame.shellgame.di.DaggerAppComponent

class App : Application() {
    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}