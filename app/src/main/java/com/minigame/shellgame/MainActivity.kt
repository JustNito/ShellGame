package com.minigame.shellgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.minigame.shellgame.ui.screen.GameScreen
import com.minigame.shellgame.ui.theme.ShellGameTheme
import com.minigame.shellgame.ui.viewmodel.GameViewModel
import com.minigame.shellgame.ui.viewmodel.GameViewModelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var gameViewModelFactory: GameViewModelFactory

    private val gameViewModel: GameViewModel by viewModels {
        gameViewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            ShellGameTheme {
                GameScreen(gameViewModel)
            }
        }
    }

}