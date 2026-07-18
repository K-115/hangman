package com.example.hangman

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangman.ui.theme.HangmanTheme

val ActiveKeyBlue = Color(0xFF1E88E5)
val ResetRed = Color(0xFFD32F2F)
val SoftGreyBackground = Color(0xFFE0E0E0)

enum class ScreenState {
    HOME,
    DIFFICULTY,
    GAME
}

enum class GameDifficulty {
    EASY,
    MEDIUM,
    HARD
}


@Composable
fun MainNavigationEngine(modifier: Modifier = Modifier) {
    var currentScreen by remember { mutableStateOf(ScreenState.HOME) }
    var selectedDifficulty by remember { mutableStateOf(GameDifficulty.EASY) }


    Box(modifier = modifier.fillMaxSize()) {
        when (currentScreen) {
            ScreenState.HOME -> HomeScreen(
                onPlayClick = { currentScreen = ScreenState.DIFFICULTY }
            )
            ScreenState.DIFFICULTY -> DifficultyScreen(
                onDifficultySelected = { difficulty ->
                    selectedDifficulty = difficulty
                    currentScreen = ScreenState.GAME
                },
                onBackClick = { currentScreen = ScreenState.HOME }
            )
            ScreenState.GAME -> HangmanScreen(
                difficulty = selectedDifficulty,
                onBackToHome = { currentScreen = ScreenState.HOME }
            )
        }
    }
}


@Composable
fun HomeScreen(onPlayClick: () -> Unit) {
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.homepage),
            contentDescription = "Homer Background Graphic",
            modifier = Modifier.fillMaxSize()
                .scale(1.1f),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = onPlayClick,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ActiveKeyBlue)
            ) {
                Text(
                    text = "PLAY",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { (context as? Activity)?.finishAffinity() },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ResetRed)
            ) {
                Text(
                    text = "QUIT",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun DifficultyScreen(
    onDifficultySelected: (GameDifficulty) -> Unit,
    onBackClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.homepage),
            contentDescription = "Homer Background Graphic",
            modifier = Modifier.fillMaxSize()
                .scale(1.1f),
            contentScale = ContentScale.Crop
        )

        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SELECT DIFFICULTY",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { onDifficultySelected(GameDifficulty.EASY) },
                modifier = Modifier.fillMaxWidth(0.7f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green
            ) {
                Text(text = "EASY (4-5 Letters)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onDifficultySelected(GameDifficulty.MEDIUM) },
                modifier = Modifier.fillMaxWidth(0.7f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)) // Orange
            ) {
                Text(text = "MEDIUM (6-7 Letters)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onDifficultySelected(GameDifficulty.HARD) },
                modifier = Modifier.fillMaxWidth(0.7f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)) // Red
            ) {
                Text(text = "HARD (8+ Letters)", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
            ) {
                Text(text = "BACK TO MENU", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HangmanTheme {
        HomeScreen(onPlayClick = {})
    }
}
