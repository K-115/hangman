package com.example.hangman

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import java.io.IOException

val WordToGuessFont = FontFamily(
    Font(resId = R.font.urbanheroes)
)

const val MAX_MISTAKES = 5

@Composable
fun HangmanScreen(
    difficulty: GameDifficulty,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var resetTrigger by remember { mutableStateOf(0) }

    var secretWord by remember { mutableStateOf("LOADING") }
    var guessedLetters by remember { mutableStateOf(setOf<Char>()) }


    val currentMistakes = guessedLetters.count { it !in secretWord }

    val isGameWon = secretWord != "LOADING" && secretWord.all { it in guessedLetters }
    val isGameLost = currentMistakes >= MAX_MISTAKES

    LaunchedEffect(difficulty, resetTrigger) {
        secretWord = "LOADING"
        secretWord = getRandomWord(context, difficulty)
    }

    val hangmanImages = listOf(
        R.drawable.background,
        R.drawable.life1,
        R.drawable.life2,
        R.drawable.life3,
        R.drawable.life4,
        R.drawable.gameover
    )

    val currentImageRes = hangmanImages.getOrElse(currentMistakes) { hangmanImages.last() }

    Box(modifier = modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Full Screen Game Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (secretWord != "LOADING" && currentMistakes < 4) {
            Image(
                painter = painterResource(id = currentImageRes),
                contentDescription = "Game Life Progression Indicator State",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Image(
            painter = painterResource(id = R.drawable.bartshands),
            contentDescription = "Bart's Hands Framing Graphic",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.BottomCenter),
            contentScale = ContentScale.FillBounds
        )

        if (secretWord != "LOADING" && currentMistakes >= 4) {
            Image(
                painter = painterResource(id = currentImageRes),
                contentDescription = "Critical Game Life Progress or Game Over Screen",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
        ) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                WordToGuess(
                    secretWord = secretWord,
                    guessedLetters = guessedLetters,
                    difficulty = difficulty
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .padding(bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onBackToHome,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text(text = "MAIN MENU", fontSize = 8.sp, color = Color.White)
                }
                val livesRemaining = MAX_MISTAKES - currentMistakes

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Lives Left: $livesRemaining ❤️",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = when {
                            livesRemaining <= 1 -> Color.Red
                            livesRemaining <= 3 -> Color(0xFFFF9800)
                            else -> Color(0xFF4CAF50)
                        }
                    )
                }
            }


            HangmanKeyboard(
                guessedLetters = guessedLetters,
                onKeyClick = { letter ->
                    if (!isGameWon && !isGameLost) {
                        guessedLetters = guessedLetters + letter
                    }
                }
            )
        }
    }
    if (isGameWon) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "🎉 YOU WIN!",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // 1. Android Native GIF Decoder View Integration
                    AndroidView(
                        factory = { viewContext ->
                            android.widget.ImageView(viewContext).apply {
                                scaleType = android.widget.ImageView.ScaleType.FIT_CENTER

                                // Decodes and sets the animated GIF from your local drawable resource file
                                val source = android.graphics.ImageDecoder.createSource(
                                    viewContext.resources,
                                    R.drawable.itchyscratchy
                                )
                                val drawable = android.graphics.ImageDecoder.decodeDrawable(source)
                                setImageDrawable(drawable)

                                // Forces the decoded animated drawable timeline loops to play immediately
                                if (drawable is android.graphics.drawable.AnimatedImageDrawable) {
                                    drawable.start()
                                }
                            }
                        },
                        modifier = Modifier
                            .size(160.dp) // Constrains the size within the dialog
                            .padding(vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // FIXED: Cleared the invalid text nesting parameter structure
                    Text(
                        text = "Awesome job! You guessed the word '$secretWord' correctly.",
                        textAlign = TextAlign.Center
                    )
                } // FIXED: Added missing closing brace for the Column container here
            },
            confirmButton = {
                Button(
                    onClick = {
                        guessedLetters = emptySet()
                        resetTrigger++
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("PLAY AGAIN", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = onBackToHome,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("HOME", color = Color.White)
                }
            }
        )
    }


    if (isGameLost) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    text = "💀 WHY YOU LITTLE...",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFE53935)
                )
            },
            text = { Text(text = "You ran out of lives! The word was '$secretWord'.") },
            confirmButton = {
                Button(
                    onClick = {
                        guessedLetters = emptySet()
                        resetTrigger++
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
                ) {
                    Text("TRY AGAIN", color = Color.White)
                }
            },
            dismissButton = {
                Button(
                    onClick = onBackToHome,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("HOME", color = Color.White)
                }
            }
        )
    }
}

@Composable
fun WordToGuess(
    secretWord: String,
    guessedLetters: Set<Char>,
    difficulty: GameDifficulty
) {
    var displayFontSize by remember(secretWord) { mutableStateOf(32.sp) }
    val displayString = secretWord
        .map { char -> if (char in guessedLetters) char else '_' }
        .joinToString(" ")

    Box(
        modifier = Modifier
            .wrapContentSize()
            .graphicsLayer(
                rotationX = -40f,
                rotationY = 0f,
                rotationZ = -2f,
                cameraDistance = 12f,
                transformOrigin = TransformOrigin.Center
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayString,
            fontSize = displayFontSize,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            letterSpacing = 6.sp,
            fontFamily = WordToGuessFont,
            color = Color(0xFF39FF14),
            modifier = Modifier.padding(horizontal = 16.dp),
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.hasVisualOverflow) {
                    displayFontSize = (displayFontSize.value * 0.9f).sp
                }
            }
        )
    }
}


fun getRandomWord(context: Context, difficulty: GameDifficulty): String {
    val assetManager = context.assets

    val wordsList = try {
        assetManager.open("words.txt").bufferedReader().use { reader ->
            reader.readLines()
                .map { it.trim().uppercase() }
                .filter { word ->
                    word.isNotEmpty() && word.all { it.isLetter() } && when (difficulty) {
                        GameDifficulty.EASY -> word.length in 4..5
                        GameDifficulty.MEDIUM -> word.length in 6..7
                        GameDifficulty.HARD -> word.length > 7
                    }
                }
        }
    } catch (e: IOException) {
        emptyList()
    }

    return wordsList.randomOrNull() ?: when (difficulty) {
        GameDifficulty.EASY -> "PLANET"
        GameDifficulty.MEDIUM -> "KOTLIN"
        GameDifficulty.HARD -> "DEVELOPER"
    }
}