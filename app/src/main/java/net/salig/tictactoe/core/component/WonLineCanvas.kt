package net.salig.tictactoe.core.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.delay
import net.salig.tictactoe.R
import net.salig.tictactoe.core.theme.CircleBlue
import net.salig.tictactoe.core.theme.CrossRed

@Composable
fun WonLineCanvas(
    winner: Int,
    animVal: Animatable<Float, AnimationVector>,
    wonDirection: String,
    list: List<Float>,
) {
    val anim = remember {
        Animatable(0f)
    }

    LaunchedEffect(winner) {
        animVal.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = LinearEasing)
        )

        if (!animVal.isRunning) {
            delay(1000L)
            animVal.snapTo(0f)
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = if (winner == R.string.player_one) CrossRed else CircleBlue,
            start = Offset(x = list[0], y = list[1]),
            end =
            when (wonDirection) {
                "|" -> {
                    Offset(x = list[2], y = list[3] * animVal.value)
                }
                "-" -> {
                    Offset(x = list[2] * animVal.value, y = list[3])
                }
                "/" -> {
                    Offset(x = list[2], y = list[3])
                }
                "\\" -> {
                    Offset(x = list[2] * animVal.value, y = list[3] * animVal.value)
                }
                else -> {
                    Offset(x = list[2], y = list[3])
                }
            },
            strokeWidth = 10f
        )
    }
}