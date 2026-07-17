package com.example.hangman

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ChalkFont = FontFamily(
    Font(resId = R.font.chalk)
)
private val KeyboardActiveBlue = Color(0xFF1E88E5)
private val KeyboardDisabledBlue = Color(0xFF1E88E5).copy(alpha = 0.35f)
@Composable
fun HangmanKeyboard(
    guessedLetters: Set<Char>,
    onKeyClick: (Char) -> Unit,
    modifier: Modifier = Modifier
) {
    val row1 = listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P')
    val row2 = listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L')
    val row3 = listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M')

    Box(
        modifier = modifier
            .fillMaxWidth(0.72f)
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            KeyboardRow(letters = row1, guessedLetters = guessedLetters, onKeyClick = onKeyClick)
            KeyboardRow(letters = row2, guessedLetters = guessedLetters, onKeyClick = onKeyClick)

            Box(modifier = Modifier.fillMaxWidth(0.7f), contentAlignment = Alignment.Center) {
                KeyboardRow(letters = row3, guessedLetters = guessedLetters, onKeyClick = onKeyClick)
            }
        }
    }
}





@Composable
fun KeyboardRow(
    letters: List<Char>,
    guessedLetters: Set<Char>,
    onKeyClick: (Char) -> Unit
) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterHorizontally)
    ) {
        for (letter in letters) {
            val isGuessed = guessedLetters.contains(letter)

            Button(
                onClick = { onKeyClick(letter) },
                enabled = !isGuessed,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(0.65f),
//                    .width(30.dp)
//                    .height(44.dp),
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = KeyboardActiveBlue,
                    contentColor = Color.White,
                    disabledContainerColor = KeyboardDisabledBlue,
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = letter.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = ChalkFont
                )
            }
        }
    }
}
