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
import androidx.compose.runtime.mutableIntStateOf
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
    var currentStreak by remember { mutableIntStateOf(0) }
    var bestStreak by remember { mutableIntStateOf(0) }
    var resetCount by remember { mutableIntStateOf(0) }
    var hasGivenUp by remember { mutableStateOf(false) }
    val isWon = secretWord.isNotEmpty() &&
            !hasGivenUp &&
            secretWord.all { char -> guessedLetters.contains(char) }
    val maxIncorrectGuesses = 6
    val incorrectGuesses = guessedLetters.filter { char -> !secretWord.contains(char) }.size
    val isLost = incorrectGuesses >= maxIncorrectGuesses || hasGivenUp
    val isGameOver = isWon || isLost
    val context = LocalContext.current
    LaunchedEffect(isWon, isLost) {
        if (isWon) {
            currentStreak ++
            if (currentStreak > bestStreak) {
                bestStreak = currentStreak
            }
        } else if (isLost) {
            currentStreak = 0
        }
    }
    LaunchedEffect(currentDifficulty, resetCount) {
        secretWord = getRandomWord(context, currentDifficulty)
        guessedLetters = emptySet()
        hasGivenUp = false
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
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.wrapContentWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "🔥 Streak: $currentStreak",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF9800)
                )
                Text(
                    text = "🏆 Best: $bestStreak",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
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
            Spacer(modifier = Modifier.height(24.dp))
            if (isGameOver) {
                Button(
                    onClick = {
                        guessedLetters = emptySet()
                        hasGivenUp = false
                        resetCount++
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "Next Word ➡️",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Button(
                    onClick = {
                        currentStreak = 0
                        hasGivenUp = true
                        guessedLetters = guessedLetters + secretWord.toSet()
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935)
                    ),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(
                        text = "Give Up 🏳️",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        HangmanKeyboard(
            guessedLetters = guessedLetters,
            onKeyClick = { letter ->
                if (!isGameOver) {
                    val updatedGuesses = guessedLetters + letter
                    guessedLetters = updatedGuesses
                    val playerWonNow = secretWord.isNotEmpty() && secretWord.all { char -> updatedGuesses.contains(char) }
                    if (playerWonNow) {
                        currentStreak++
                        if (currentStreak > bestStreak) {
                            bestStreak = currentStreak
                        }
                    } else if (updatedGuesses.filter { char -> !secretWord.contains(char) }.size >= maxIncorrectGuesses) {
                        currentStreak = 0
                    }
                }
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