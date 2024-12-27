package org.db.woofle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import org.db.woofle.ui.theme.WoofleTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight


class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      WoofleTheme {
        Scaffold(
          topBar = {
            GameTopBar()
          },
          modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            Surface(
              color = MaterialTheme.colorScheme.background,
              modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
            ) {
              GameScreen()
            }

          }

      }
    }
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameTopBar() {
  TopAppBar(
    title = {
      Box(
        modifier = Modifier
          .fillMaxSize(),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          style = MaterialTheme.typography.headlineMedium,
          fontWeight = FontWeight.Black,
          text = stringResource(id = R.string.app_name)
        )
      }
    },
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
      containerColor = MaterialTheme.colorScheme.background,
      titleContentColor = MaterialTheme.colorScheme.onBackground
    )
  )
}