package com.example.hangman

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangman.ui.theme.HangmanTheme
import java.io.IOException

@Composable
fun HangmanScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var secretWord by remember { mutableStateOf("PREVIEW") }
    var guessedLetters by remember { mutableStateOf(setOf<Char>()) }

    LaunchedEffect(Unit) {
        secretWord = getRandomWord(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        WordToGuess(
            secretWord = secretWord,
            guessedLetters = guessedLetters
        )

        HangmanKeyboard(
            guessedLetters = guessedLetters,
            onKeyClick = { letter ->
                guessedLetters = guessedLetters + letter
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