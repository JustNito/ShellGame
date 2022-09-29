package com.minigame.shellgame.data

interface LocalStorage {

    fun getScore(): Int

    fun setScore(score: Int)
}