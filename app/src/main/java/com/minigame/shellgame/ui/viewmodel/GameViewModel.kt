package com.minigame.shellgame.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minigame.shellgame.data.LocalStorage
import com.minigame.shellgame.ui.model.ShellModel
import com.minigame.shellgame.ui.utils.GameStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameViewModel @Inject constructor(val localStorage: LocalStorage) : ViewModel() {

    private var _gameStatus by mutableStateOf(GameStatus.WaitForStart)
    val gameStatus
        get() = _gameStatus

    private var _shells by mutableStateOf(
        listOf(
            ShellModel(0,false),
            ShellModel(1,true),
            ShellModel(2,false),
        )
    )
    val shells
        get() = _shells

    private var _score by mutableStateOf(5)
    val score
        get() = _score

    private var _highScore by mutableStateOf(localStorage.getScore())
    val highScore
        get() = _highScore

    private var _ballsAmount by mutableStateOf(5)
    val ballsAmount
        get() = _ballsAmount

    private var _ballsSelected by mutableStateOf(1)
    val ballsSelected
        get() = _ballsSelected


    fun incBall() {
        _ballsSelected++
    }

    fun onCupSelect(index: Int) = viewModelScope.launch {
        upAndDownCup(index)
        if(_shells.find { it.id == index }!!.hasBall) {
            win()
        } else {
            loss()
        }
    }

    fun startGame() = viewModelScope.launch {
        _ballsAmount -= ballsSelected
        _gameStatus = GameStatus.ShuffleAnimation
        upAndDownCup(1)
        repeat(5) {
            _shells = _shells.shuffled()
            delay(500)
        }
        _gameStatus = GameStatus.WaitForChoose
    }
    private suspend fun upAndDownCup(index: Int) {
        changeCupPositionByIndex(index, true)
        delay(2000)
        changeCupPositionByIndex(index,false)
        delay(1000)
    }
    private fun changeCupPositionByIndex(index: Int, isUp: Boolean) {
        _shells = ArrayList(_shells).map {
            if(it.id == index)
                it.copy(isUp = isUp)
            else it.copy()
        }
    }
    fun decBall() {
        _ballsSelected--
    }

    private fun win() {
        _ballsAmount += ballsSelected * 2
        if(score < ballsAmount)
            _score = ballsAmount
        resetRound()
    }

    fun restartGame() {
        _score = 5
        _ballsAmount = 5
        resetRound()
    }

    private fun resetRound() {
        _shells = _shells.sortedBy { it.id }
        _ballsSelected = 1
        _gameStatus = GameStatus.WaitForStart
    }

    private fun loss() {
        if(ballsAmount != 0) {
            resetRound()
        } else {
            if(highScore < score) {
                localStorage.setScore(score)
                _highScore = score
            }
            _gameStatus = GameStatus.GameOver
        }
    }
}