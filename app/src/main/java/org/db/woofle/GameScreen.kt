package org.db.woofle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GameScreen(
  gameViewModel: GameViewModel = viewModel()

) {
  val context = LocalContext.current

  LaunchedEffect(Unit) {
    gameViewModel.loadCurrentLevel()
    gameViewModel.loadWords(context)

  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    horizontalAlignment = CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Text(text = "Level: ${gameViewModel.level}", fontSize = 24.sp, color = Color.Black)
    Spacer(modifier = Modifier.height(16.dp))
    Text(text = gameViewModel.message, fontSize = 24.sp, color = Color.Black)
    Spacer(modifier = Modifier.height(16.dp))
    Column(
      verticalArrangement = Arrangement.spacedBy(8.dp),
      horizontalAlignment = CenterHorizontally
    ) {
      gameViewModel.guesses.forEachIndexed { index, guess ->
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),

        ) {
          for (i in 0 until 5) {
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(gameViewModel.colors[index][i], RoundedCornerShape(4.dp)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = guess.getOrNull(i)?.toString() ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
              )
            }
          }
        }
      }
      if (gameViewModel.guesses.size < 6) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          for (i in 0 until 5) { // Assuming the word length is 5
            Box(
              modifier = Modifier
                .size(48.dp)
                .background(Color.LightGray, RoundedCornerShape(4.dp)),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = gameViewModel.currentGuess.getOrNull(i)?.toString() ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
              )
            }
          }
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
      if (gameViewModel.isGuessCorrect) {
        Button(onClick = { gameViewModel.proceedToNextLevel() }) {
          Text("Next Level")
        }
      } else {
        Keyboard(
          onKeyPress = { char -> gameViewModel.updateGuess(char) },
          onBackspace = { gameViewModel.deleteLastChar() },
          keyStates = gameViewModel.keyStates
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
          gameViewModel.submitGuess()
        }) {
          Text("Submit")
        }
      }
    }
  }
}



