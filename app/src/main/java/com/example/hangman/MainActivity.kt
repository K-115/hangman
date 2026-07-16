package com.example.hangman

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hangman.ui.theme.HangmanTheme
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HangmanTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HangmanScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HangmanScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    var secretWord by remember { mutableStateOf("PREVIEW") }
    val guessedLetters = remember { mutableListOf<Char>() }

    LaunchedEffect(Unit) {
        secretWord = getRandomWord(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        WordToGuess(
            secretWord = secretWord,
            guessedLetters = guessedLetters
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            val testLetters = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G')
            val nextGuess = testLetters.firstOrNull { it !in guessedLetters }
            if (nextGuess != null) {
                guessedLetters.add(nextGuess)
            }
        }
        ) {
            Text(text = stringResource(R.string.test_button))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HangmanScreenPreview() {
    HangmanTheme {
        HangmanScreen()
    }
}

@Composable
fun WordToGuess(
    secretWord: String,
    guessedLetters: List<Char>,
    modifier: Modifier = Modifier
) {
    val displayString = secretWord.map { char -> if (char in guessedLetters) char else '_' }
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
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 4.sp
        )
    }
}

fun getRandomWord(context: Context): String {
    return try {
        context.assets.open("words.txt").bufferedReader().use { reader ->
            val wordsList = reader.readLines().filter { it.isNotBlank() }
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