package com.minigame.shellgame.ui.screen

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.minigame.shellgame.R
import com.minigame.shellgame.ui.model.ShellModel
import com.minigame.shellgame.ui.theme.ShellGameTheme
import com.minigame.shellgame.ui.utils.GameStatus
import com.minigame.shellgame.ui.viewmodel.GameViewModel

@Composable
fun GameScreen(gameViewModel: GameViewModel) {

    if(gameViewModel.gameStatus != GameStatus.GameOver) {
        Game(
            score = gameViewModel.score,
            ballsAmount = gameViewModel.ballsAmount,
            shells = gameViewModel.shells,
            ballsSelected = gameViewModel.ballsSelected,
            incBalls = gameViewModel::incBall,
            decBalls = gameViewModel::decBall,
            startGame = gameViewModel::startGame,
            onCupSelect = gameViewModel::onCupSelect,
            gameStatus = gameViewModel.gameStatus
        )
    } else {
        ResultDialog(
            score = gameViewModel.score,
            highScore = gameViewModel.highScore,
            restartGame = gameViewModel::restartGame
        )
    }
}

@Composable
fun Game(
    score: Int,
    ballsAmount: Int,
    shells: List<ShellModel>,
    ballsSelected: Int,
    incBalls: () -> Unit,
    decBalls: () -> Unit,
    startGame: () -> Unit,
    onCupSelect: (Int) -> Unit,
    gameStatus: GameStatus
) {
    Column {
        Header(
            modifier = Modifier.weight(1f),
            score = score,
            ballsAmount = ballsAmount
        )
        Shells(
            modifier = Modifier.weight(1f),
            shells = shells,
            onCupSelect = onCupSelect,
            gameStatus = gameStatus
        )
        Footer(
            ballsSelected = ballsSelected,
            incBalls = incBalls,
            decBalls = decBalls,
            ballsAmount = ballsAmount,
            startGame = startGame,
            gameStatus = gameStatus
        )
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    score: Int,
    ballsAmount: Int
) {
    Row(
        modifier = modifier.padding(4.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.score, score),
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_ball),
                contentDescription = "balls"
            )
            Text(
                text = "$ballsAmount",
                style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Shells(
    modifier: Modifier = Modifier,
    onCupSelect: (Int) -> Unit,
    shells: List<ShellModel>,
    gameStatus: GameStatus
){
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(items = shells, key = {it.id}) { shell ->
            val cupPadding: Dp by animateDpAsState(
                targetValue = if(shell.isUp) 50.dp else 0.dp
            )
            Box(
                modifier = Modifier
                    .padding(top = 50.dp - cupPadding)
                    .animateItemPlacement(),
                contentAlignment = Alignment.Center
            ) {
                if(shell.hasBall) {
                    Image(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.BottomCenter),
                        painter = painterResource(id = R.drawable.ic_ball),
                        contentDescription = "ball"
                    )
                }
                Image(
                    modifier = Modifier
                        .padding(bottom = cupPadding)
                        .size(90.dp)
                        .clickable(enabled = gameStatus == GameStatus.WaitForChoose) {
                            onCupSelect(shell.id)
                        },
                    painter = painterResource(id = R.drawable.ic_cup),
                    contentDescription = "cup"
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Shell(
    modifier: Modifier = Modifier,
    hasBall: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if(hasBall) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.BottomCenter),
                painter = painterResource(id = R.drawable.ic_ball),
                contentDescription = "ball"
            )
        }
        Image(
            modifier = Modifier.size(90.dp),
            painter = painterResource(id = R.drawable.ic_cup),
            contentDescription = "cup"
        )
    }
}

@Composable
fun BallsSelected(
    modifier: Modifier = Modifier,
    ballsSelected: Int,
    incBalls: () -> Unit,
    decBalls: () -> Unit,
    ballsAmount: Int,
    gameStatus: GameStatus
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .clickable(
                    enabled = ballsSelected > 1 && gameStatus == GameStatus.WaitForStart,
                    onClick = decBalls
                ),
            painter = painterResource( id =
                if(ballsSelected > 1 && gameStatus == GameStatus.WaitForStart) {
                    R.drawable.ic_arrow_left
                } else {
                    R.drawable.ic_arrow_left_disable
                }
            ),
            contentDescription = "arrow left"
        )
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = "$ballsSelected",
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
        )
        Image(
            modifier = Modifier
                .size(40.dp)
                .clickable(
                    enabled = ballsSelected != ballsAmount && gameStatus == GameStatus.WaitForStart,
                    onClick = incBalls
                ),
            painter = painterResource( id =
                if(ballsSelected != ballsAmount && gameStatus == GameStatus.WaitForStart) {
                    R.drawable.ic_arrow_right
                } else {
                    R.drawable.ic_arrow_right_disable
                }
            ),
            contentDescription = "arrow right"
        )
    }
}

@Composable
fun Footer(
    modifier: Modifier = Modifier,
    incBalls: () -> Unit,
    decBalls: () -> Unit,
    ballsAmount: Int,
    startGame: () -> Unit,
    ballsSelected: Int,
    gameStatus: GameStatus
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        BallsSelected(
            modifier = Modifier.weight(1F),
            ballsSelected = ballsSelected,
            incBalls = incBalls,
            decBalls = decBalls,
            ballsAmount = ballsAmount,
            gameStatus = gameStatus
        )
        Button(
            modifier = Modifier.padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primaryVariant
            ),
            enabled = gameStatus == GameStatus.WaitForStart,
            onClick = startGame
        ) {
            Text("Go!")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGame() {
    ShellGameTheme {
        Game(
            score = 0,
            ballsAmount = 0,
            listOf(
                ShellModel(id = 1, hasBall = false),
                ShellModel(id = 2, hasBall = true),
                ShellModel(id = 3, hasBall = false)
            ),
            ballsSelected = 1,
            incBalls = {},
            decBalls = {},
            startGame = {},
            onCupSelect = {},
            gameStatus = GameStatus.WaitForStart
        )
    }
}

@Composable
fun ResultDialog(
    score: Int,
    highScore: Int,
    restartGame: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.5F)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Game Over",
                style = MaterialTheme.typography.h2,
                color = MaterialTheme.colors.primary
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "Score: $score",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = "High Score: $highScore",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary
            )
            Button(
                onClick = restartGame
            ) {
                Text("Play Again")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHeader(){
    Header(
        score = 1,
        ballsAmount = 3
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewShell() {
    Shell(hasBall = true)
}

@Preview(showBackground = true)
@Composable
fun PreviewShells() {
    Shells(
        shells = listOf(
            ShellModel(id = 1, hasBall = false),
            ShellModel(id = 2, hasBall = true),
            ShellModel(id = 3, hasBall = false)
        ),
        onCupSelect = {},
        gameStatus = GameStatus.WaitForStart
    )
}
