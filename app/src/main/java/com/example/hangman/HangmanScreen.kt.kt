package com.example.hangman

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangman.ui.theme.HangmanTheme
import java.io.IOException

@Composable
fun HangmanScreen() {
    val context = LocalContext.current

    var secretWord by remember { mutableStateOf("PREVIEW") }
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

    LaunchedEffect(resetCount) {
        secretWord = getRandomWord(context)
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
fun WordToGuess(
    secretWord: String,
    guessedLetters: Set<Char>
) {
    var displayFontSize by remember(secretWord) { mutableStateOf(44.sp) }
    val displayString = secretWord
        .map { char ->
            if (char in guessedLetters) char else '_'
        }
        .joinToString(" ")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "WORD TO GUESS:",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = displayString,
            fontSize = displayFontSize,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            letterSpacing = 4.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    displayFontSize = (displayFontSize.value * 0.9f).sp
                }
            }
        )
    }
}

fun getRandomWord(context: Context): String {
    return try {
        context.assets.open("words.txt").bufferedReader().use { reader ->
            val wordsList = reader
                .readLines()
                .filter { it.isNotBlank() }
            if (wordsList.isNotEmpty()) {
                wordsList.random().uppercase()
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