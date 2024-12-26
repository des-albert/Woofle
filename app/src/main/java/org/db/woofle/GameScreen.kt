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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun GameScreen(
  gameViewModel: GameViewModel = viewModel()

) {

  val scores = gameViewModel.history
  val level = gameViewModel.level
  val message = gameViewModel.message
  val displayColors = listOf(
    MaterialTheme.colorScheme.surface,
    MaterialTheme.colorScheme.error,
    MaterialTheme.colorScheme.onSurface
  )

  LaunchedEffect(Unit) {
    gameViewModel.loadCurrentLevel()
    gameViewModel.loadHistory()
    gameViewModel.loadWords()
    gameViewModel.setColors(displayColors)
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
      alpha = 0.50f,
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
                  color = MaterialTheme.colorScheme.onPrimary
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
                  .background(MaterialTheme.colorScheme.background, RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = gameViewModel.currentGuess.getOrNull(i)?.toString() ?: "",
                  fontSize = 24.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onPrimary
                )
              }
            }
          }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (gameViewModel.isGuessCorrect) {
          ElevatedButton(
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = MaterialTheme.colorScheme.background
            ),
            onClick = {
              gameViewModel.proceedToNextLevel()
            }) {
            Text(
              text = "Next Level",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onBackground)
          }
        } else {
          Keyboard(
            onKeyPress = { char -> gameViewModel.updateGuess(char) },
            onBackspace = { gameViewModel.deleteLastChar() },
            keyStates = gameViewModel.keyStates
          )
          Spacer(modifier = Modifier.height(8.dp))
          ElevatedButton(
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = MaterialTheme.colorScheme.background
            ),
            onClick = {gameViewModel.submitGuess()
          }) {
            Text(
              text = "Submit",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onBackground)
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
            Bar(index, value, maxScore, MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.height(4.dp))
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
      .height(20.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Text(
      text = (index + 1).toString(),
      style = MaterialTheme.typography.bodyMedium,
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onPrimary,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Canvas(
      modifier = Modifier
        .weight(1f)
        .height(14.dp)
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
      style = MaterialTheme.typography.bodyMedium,
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onPrimary,
    )
  }
}

