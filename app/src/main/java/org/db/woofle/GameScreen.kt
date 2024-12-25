package org.db.woofle

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun GameScreen(
  gameViewModel: GameViewModel = viewModel()

) {
  val context = LocalContext.current
  val scores = gameViewModel.history
  val level = gameViewModel.level
  val message = gameViewModel.message

  LaunchedEffect(Unit) {
    gameViewModel.loadCurrentLevel()
    gameViewModel.loadHistory()
    gameViewModel.loadWords(context)

  }

  Box(
    Modifier
      .fillMaxSize()
      .padding(horizontal = 8.dp, vertical = 16.dp)
  ) {
    Image(
      painter = painterResource(id = R.drawable.izzy),
      contentDescription = null,
      modifier = Modifier.fillMaxSize(),
      alpha = 0.60f,
      contentScale = ContentScale.Crop
    )
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
      horizontalAlignment = CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {

      GameHeader(level, message)

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
        Spacer(modifier = Modifier.height(12.dp))
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
          Spacer(modifier = Modifier.height(12.dp))
          Button(onClick = {
            gameViewModel.submitGuess()
          }) {
            Text("Submit")
          }
        }
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, Color.Gray)
            .padding(8.dp)
        ) {
          val maxScore = scores.maxOrNull() ?: 1
          scores.forEachIndexed {index, value ->
            Bar(index, value, maxScore, MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(8.dp))
          }
        }

      }
    }
  }
}

@Composable
fun Bar(
  index: Int,
  value: Int,
  maxValue: Int,
  color: Color
) {
  val barWidth = (value.toFloat() / maxValue) * 500.dp.value

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(16.dp),
    verticalAlignment = Alignment.Bottom
  ) {
    Text(
      text = (index + 1).toString(),
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onPrimary,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Canvas(
      modifier = Modifier
        .weight(1f)
        .height(12.dp)
    ) {
      drawIntoCanvas { canvas ->
        drawRect(
          color = color,
          size = Size(barWidth, size.height)
        )
      }
    }
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = value.toString(),
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onPrimary,
    )
  }

}



