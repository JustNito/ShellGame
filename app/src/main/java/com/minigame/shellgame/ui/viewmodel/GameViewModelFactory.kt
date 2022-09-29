package com.minigame.shellgame.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.minigame.shellgame.data.LocalStorage
import javax.inject.Inject

class GameViewModelFactory @Inject constructor(
    val localStorage: LocalStorage
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(localStorage = localStorage) as T
    }
}