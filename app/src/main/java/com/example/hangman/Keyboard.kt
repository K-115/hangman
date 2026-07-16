package com.example.hangman

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ActiveKeyBlue = Color(0xFF2196F3)
private val DisabledKeyBlue = Color(0xFF2196F3).copy(alpha = 0.35f)

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
        KeyboardRow(
            letters = row1,
            guessedLetters = guessedLetters,
            onKeyClick = onKeyClick
        )
        KeyboardRow(
            letters = row2,
            guessedLetters = guessedLetters,
            onKeyClick = onKeyClick
        )
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
        horizontalArrangement = Arrangement.spacedBy(4.dp,
            Alignment.CenterHorizontally)
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