package com.example.guessthenumbergame

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guessthenumbergame.ui.theme.GuessTheNumberGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuessTheNumberGameTheme {
                GuessingGame()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuessingGame() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guess the Number Game") }
            )
        }
    ) {
        NumberGuessingContent()
    }
}

@Composable
fun NumberGuessingContent() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeightDp = configuration.screenHeightDp

    var randomNumber by remember { mutableStateOf((1..100).random()) }
    var guess by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf("") }
    var points by remember { mutableStateOf(0) }
    var showHint by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Points: $points", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = guess,
            onValueChange = { guess = it },
            label = { Text("Enter your guess") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                checkGuess(guess.toIntOrNull(), randomNumber, setHint = { hint = it })
                if (hint == "Congratulations! You guessed the correct number!") {
                    points += 1
                }
                keyboardController?.hide()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Guess")
        }

        Text(
            text = hint,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                randomNumber = (1..100).random()
                guess = ""
                hint = ""
                showHint = false
                keyboardController?.hide()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Restart")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (points > 0) {
                    points -= 1
                    showHint = true
                } else {
                    hint = "Not enough points for a hint!"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Get Hint (-1 point)")
        }

        AnimatedVisibility(visible = showHint) {
            Text(
                text = provideHint(randomNumber, guess.toIntOrNull()),
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

fun checkGuess(guess: Int?, randomNumber: Int, setHint: (String) -> Unit) {
    if (guess == null) {
        setHint("Please enter a valid number.")
    } else if (guess == randomNumber) {
        setHint("Congratulations! You guessed the correct number!")
    } else if (guess < randomNumber) {
        setHint("Too low! Try again.")
    } else {
        setHint("Too high! Try again.")
    }
}

fun provideHint(randomNumber: Int, guess: Int?): String {
    return if (guess == null) {
        "Try guessing a number between 1 and 100."
    } else {
        if (randomNumber % 2 == 0) {
            "The number is even."
        } else {
            "The number is odd."
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGuessingGame() {
    GuessTheNumberGameTheme {
        GuessingGame()
    }
}
