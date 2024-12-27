package org.db.woofle

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameHeader(
  level: Int,
  message: String
) {
  Text(
    text = "Level: $level",
    style = MaterialTheme.typography.headlineSmall,
    fontSize = 18.sp,
    fontWeight = FontWeight.Black,
    color = MaterialTheme.colorScheme.onPrimary
  )
  Spacer(modifier = Modifier.height(16.dp))
  Text(
    text = message,
    style = MaterialTheme.typography.headlineSmall,
    fontSize = 18.sp,
    fontWeight = FontWeight.Black,
    color = MaterialTheme.colorScheme.onPrimary
  )
  Spacer(modifier = Modifier.height(16.dp))
}

