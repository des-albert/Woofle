package org.db.woofle

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameHeader(
  level: Int,
  message: String
) {
  Text(
    text = stringResource(id = R.string.app_name),
    style = MaterialTheme.typography.headlineLarge,
    fontWeight = FontWeight.Black,
    fontFamily = FontFamily.Serif,
  )
  Text(
    text = "Level: $level",
    style = MaterialTheme.typography.headlineSmall,
    fontSize = 18.sp,
    fontWeight = FontWeight.Black,
    fontFamily = FontFamily.Serif,
    color = MaterialTheme.colorScheme.onPrimary
  )
  Spacer(modifier = Modifier.height(16.dp))
  Text(
    text = message,
    fontSize = 18.sp,
    fontWeight = FontWeight.Black,
    fontFamily = FontFamily.Serif,
    color = MaterialTheme.colorScheme.onPrimary
  )
  Spacer(modifier = Modifier.height(16.dp))
}

