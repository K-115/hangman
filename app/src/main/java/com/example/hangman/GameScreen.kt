package com.example.hangman

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Locale

const val FILE_NAME = "words.txt"
const val MAX_MISTAKES = 6

val ActiveKeyBlue = Color(0xFFFFFFFF)
val DisabledKeyBlue = Color(0x0FFFFFF0).copy(alpha = 0.35f)

fun loadWordsPoolFromAssets(context: android.content.Context): List<String> {
    val assetManager = context.assets
    val assetExists = assetManager.list("")?.contains(FILE_NAME) ?: false
    if (!assetExists) return emptyList()

    val inputStream = assetManager.open(FILE_NAME)
    val reader = BufferedReader(InputStreamReader(inputStream))
    val lines = reader.readLines()
    reader.close()

    return lines
        .map { it.trim().uppercase(Locale.getDefault()) }
        .filter { it.length > 4 && it.all { char -> char.isLetter() } }
}

@Composable
fun GameScreen() {
    val context = LocalContext.current
    var guessedLetters by remember { mutableStateOf(setOf<Char>()) }
    var secretWord by remember { mutableStateOf("LOADING") }

    LaunchedEffect(Unit) {
        val words = loadWordsPoolFromAssets(context)
        if (words.isNotEmpty()) {
            secretWord = words.random()
        } else {
            secretWord = "ERRORNOFILE"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Placeholder hangman name thing",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = ActiveKeyBlue
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (secretWord == "LOADING") {
                Text(text = "Loading word pool...", fontSize = 20.sp)
            } else if (secretWord == "ERRORNOFILE") {
                Text(text = "Error: words.txt missing in assets!", fontSize = 18.sp, color = Color.Red)
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (char in secretWord) {
                        val displayChar = if (guessedLetters.contains(char)) char.toString() else "_"
                        Text(text = displayChar, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        HangmanKeyboard(
            guessedLetters = guessedLetters,
            onKeyClick = { chosenLetter ->
                if (secretWord != "LOADING" && secretWord != "ERRORNOFILE") {
                    guessedLetters = guessedLetters + chosenLetter
                }
            }
        )
    }
}
@Composable
fun HangmanKeyboard(
    guessedLetters: Set<Char>,
    onKeyClick: (Char) -> Unit,
    modifier: Modifier = Modifier
) {
    val row1 = ('A'..'M').toList()
    val row2 = ('N'..'Z').toList()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KeyboardRow(letters = row1, guessedLetters = guessedLetters, onKeyClick = onKeyClick)
        KeyboardRow(letters = row2, guessedLetters = guessedLetters, onKeyClick = onKeyClick)
    }
}

@Composable
fun KeyboardRow(
    letters: List<Char>,
    guessedLetters: Set<Char>,
    onKeyClick: (Char) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        for (letter in letters) {
            val isGuessed = guessedLetters.contains(letter)

            Button(
                onClick = { onKeyClick(letter) },
                enabled = !isGuessed,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ActiveKeyBlue,
                    contentColor = Color.White,
                    disabledContainerColor = DisabledKeyBlue,
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = letter.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


