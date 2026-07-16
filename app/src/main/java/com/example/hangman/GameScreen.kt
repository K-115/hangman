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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color definitions
val ActiveKeyBlue = Color(0xFF1E88E5)
val DisabledKeyBlue = Color(0xFF1E88E5).copy(alpha = 0.35f)

@Composable
fun GameScreen() {
    var guessedLetters by remember { mutableStateOf(setOf<Char>()) }
    val secretWord = "KOTLIN" // Temporary hardcoded word for safety testing

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween, // Pushes keyboard down, leaves room for blanks
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Upper UI area placeholder to avoid layout rendering collapse crashes
        Column(
            modifier = Modifier.padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "HANGMAN",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = ActiveKeyBlue
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Displays simple blanks so the app has content to draw
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (char in secretWord) {
                    val displayChar = if (guessedLetters.contains(char)) char.toString() else "_"
                    Text(text = displayChar, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Keyboard safely locked to the bottom
        HangmanKeyboard(
            guessedLetters = guessedLetters,
            onKeyClick = { chosenLetter ->
                guessedLetters = guessedLetters + chosenLetter
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
