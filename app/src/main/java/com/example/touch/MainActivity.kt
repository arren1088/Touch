package com.example.touch

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.touch.ui.theme.TouchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TouchTheme {
                Greeting()
                DrawCircle()
                DrawPath()
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "多指觸控Compose實例",
                fontFamily = FontFamily(Font(R.font.finger)),
                fontSize = 25.sp,
                color = Color.Blue
            )
            Image(
                painter = painterResource(id = R.drawable.hand),
                contentDescription = "手掌圖片",
                alpha = 0.7f,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Blue)
                    .padding(start = 8.dp)
            )
        }
        Text(
            text = "作者：陳恩儒",
            fontFamily = FontFamily(Font(R.font.finger)),
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawCircle() {
    var X = remember { mutableStateListOf<Float>() }
    var Y = remember { mutableStateListOf<Float>() }
    var Fingers by remember { mutableStateOf(0) }
    val colors = listOf(
        Color.Red, Color(0xFFFF5722), Color.Yellow, Color.Green,
        Color.Blue, Color(0xFF3F51B5), Color(0xFF9C27B0)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                Fingers = event.pointerCount
                // Update positions for each finger
                X.clear()
                Y.clear()
                for (i in 0 until Fingers) {
                    X.add(event.getX(i))
                    Y.add(event.getY(i))
                }
                true
            }
    ) {
        Canvas(modifier = Modifier) {
            for (i in 0 until Fingers) {
                val paintColor = colors[i % colors.size]
                drawCircle(paintColor, 100f, Offset(X[i], Y[i]))
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawPath() {
    val paths = remember { mutableStateListOf<Offset>() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        paths.clear() // Start a new path on a new touch
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        paths.add(Offset(event.x, event.y)) // Add point on move
                        true
                    }
                    else -> false
                }
            }
    ) {
        Canvas(modifier = Modifier) {
            val path = Path()
            if (paths.isNotEmpty()) {
                path.moveTo(paths[0].x, paths[0].y)
                for (i in 1 until paths.size) {
                    path.lineTo(paths[i].x, paths[i].y)
                }
                drawPath(path, color = Color.Black, style = Stroke(width = 30f, join = StrokeJoin.Round))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTouchDrawing() {
    TouchTheme {
        DrawCircle()
        DrawPath()
    }
}
