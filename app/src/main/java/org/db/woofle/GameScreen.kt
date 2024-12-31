package org.db.woofle

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
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
  ) {
    Image(
      painter = painterResource(id = R.drawable.izzy),
      contentDescription = null,
      modifier = Modifier.fillMaxSize(),
      alpha = 0.4f,
      contentScale = ContentScale.Crop
    )
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(12.dp),
      horizontalAlignment = CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

      GameHeader(level, message)

      Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = CenterHorizontally
      ) {
        gameViewModel.guesses.forEachIndexed { index, guess ->
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {
            for (i in 0 until 5) {
              val boxColor = gameViewModel.colors[index][i]
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .background(animateColorAsState(targetValue = boxColor.copy(alpha = 0.4f), label = "").value, RoundedCornerShape(4.dp)),
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
              color = MaterialTheme.colorScheme.onBackground
            )
          }

          HorizontalBarChart(data = scores)

        } else {
          Keyboard(
            onKeyPress = { char -> gameViewModel.updateGuess(char) },
            onBackspace = { gameViewModel.deleteLastChar() },
            keyStates = gameViewModel.keyStates
          )
          Spacer(modifier = Modifier.height(4.dp))
          ElevatedButton(
            colors = ButtonDefaults.elevatedButtonColors(
              containerColor = MaterialTheme.colorScheme.background
            ),
            onClick = {
              gameViewModel.submitGuess()
            }) {
            Text(
              text = "Submit",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onBackground
            )
          }
        }

      }
    }
  }
}


@Composable
fun HorizontalBarChart(data: List<Int>) {

  val barHeight = 14.dp
  val barSpacing = 4.dp
  val maxBarWidth = 300.dp
  val cornerRadius = 4.dp
  val maxValue = data.maxOrNull() ?: 1

  Column(
    horizontalAlignment = CenterHorizontally,
    modifier = Modifier
      .fillMaxWidth()
      .border(2.dp, Color.Gray, RoundedCornerShape(6.dp))
      .padding(8.dp)
  ) {
    Text(
      text = "Statistics",
      fontWeight = FontWeight.Bold,
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onPrimary
    )
    data.forEachIndexed { index, value ->

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = barSpacing)
      ) {

        val barWidth = (value.toFloat() / maxValue) * maxBarWidth.value - 5

        Text(
          text = (index + 1).toString(),
          modifier = Modifier
            .align(Alignment.CenterVertically),
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.Bold
        )

        Box(
          modifier = Modifier
            .height(barHeight)
            .padding(start = 8.dp)
            .width(barWidth.dp) //
            .background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(cornerRadius))
        )
        if (value > 0) {
          Text(
            text = value.toString(),
            modifier = Modifier
              .padding(start = 8.dp)
              .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}