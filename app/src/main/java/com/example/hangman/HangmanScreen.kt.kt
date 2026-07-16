package com.example.hangman

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangman.ui.theme.HangmanTheme
import java.io.IOException

enum class Difficulty(val setting: String, val minLength: Int, val maxLength: Int) {
    EASY("Easy", 3, 5),
    MEDIUM("Medium", 6, 8),
    HARD("Hard", 9, 15)
}
@Composable
fun HangmanScreen() {
    var currentDifficulty by remember { mutableStateOf(Difficulty.EASY) }
    var secretWord by remember { mutableStateOf("HANGMAN") }
    var guessedLetters by remember { mutableStateOf(setOf<Char>()) }
    val context = LocalContext.current
    LaunchedEffect(currentDifficulty) {
        secretWord = getRandomWord(context, currentDifficulty)
        guessedLetters = emptySet()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "WORD TO GUESS:",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Difficulty.entries.forEach { difficulty ->
                    val isSelected = currentDifficulty == difficulty
                    Button(
                        onClick = { currentDifficulty = difficulty },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF2196F3) else Color(0xFF2196F3).copy(alpha = 0.5f)
                        ),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = difficulty.setting,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            WordToGuess(
                secretWord = secretWord,
                guessedLetters = guessedLetters
            )
        }
        HangmanKeyboard(
            guessedLetters = guessedLetters,
            onKeyClick = { letter ->
                guessedLetters = guessedLetters + letter
            }
        )
    }
}

@Composable
fun WordToGuess(secretWord: String, guessedLetters: Set<Char>) {
    val slotWidth = when {
        secretWord.length <= 5 -> 48.dp
        secretWord.length <= 7 -> 38.dp
        secretWord.length <= 9 -> 30.dp
        else -> 22.dp
    }
    val fontSize = when {
        secretWord.length <= 5 -> 38.sp
        secretWord.length <= 7 -> 30.sp
        secretWord.length <= 9 -> 24.sp
        else -> 18.sp
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        secretWord.forEach { char ->
            val isGuessed = guessedLetters.contains(char)
            Column(
                modifier = Modifier
                    .width(slotWidth)
                    .padding(horizontal = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = if (isGuessed) char.toString() else " ",
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp))
                        .background(Color.Black)
                )
            }
        }
    }
}

fun getRandomWord(context: Context, difficulty: Difficulty): String {
    return try {
        context.assets.open("words.txt").bufferedReader().use { reader ->
            val wordsList = reader
                .readLines()
                .filter { it.isNotBlank() }
            val filteredWords = wordsList.filter { word ->
                val cleanWord = word.trim()
                cleanWord.length in difficulty.minLength..difficulty.maxLength
            }
            if (filteredWords.isNotEmpty()) {
                filteredWords.random().uppercase()
            } else {
                "HANGMAN"
            }
        }
    } catch (error: IOException) {
        error.printStackTrace()
        "HANGMAN"
    }
}

@Preview(showBackground = true)
@Composable
fun HangmanScreenPreview() {
    HangmanTheme {
        HangmanScreen()
    }
}