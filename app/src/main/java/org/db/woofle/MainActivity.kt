package org.db.woofle

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.db.woofle.ui.theme.WoofleTheme

class MainActivity : ComponentActivity() {
  private lateinit var viewModel: GameViewModel
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      WoofleTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          val repository = GameRepository(applicationContext)
          viewModel = GameViewModel(repository)

          GameScreen(viewModel)
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
  GameScreen()
}