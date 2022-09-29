package com.minigame.shellgame.di

import com.minigame.shellgame.data.LocalStorage
import com.minigame.shellgame.data.Prefs
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {

    @Binds
    abstract fun provideLocalStorage(localStorage: Prefs): LocalStorage
}