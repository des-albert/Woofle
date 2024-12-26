package org.db.woofle

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Keyboard(
  modifier: Modifier = Modifier,
  onKeyPress: (Char) -> Unit,
  onBackspace: () -> Unit,
  keyStates: Map<Char, Color>
) {
  val rows = listOf(
    "QWERTYUIOP",
    "ASDFGHJKL",
    "ZXCVBNM"
  )
  Column(
    horizontalAlignment = CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp)
  ) {
    rows.forEachIndexed { rowIndex, row ->
      Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
      ) {
        row.forEach { char ->
          Box(
            modifier
              .height(40.dp)
              .weight(1f)
              .background(keyStates[char] ?: Color.LightGray, RoundedCornerShape(4.dp))
              .clickable(
                onClick = {
                  onKeyPress(char)
                }
              ),
            Alignment.Center
          ) {
            Text(
              text = char.toString(),
              color = MaterialTheme.colorScheme.tertiary,
              fontSize = 18.sp
            )
          }
        }

        if (rowIndex == 2) {
          Box(
            modifier
              .height(40.dp)
              .weight(1f)
              .clip(RoundedCornerShape(2.dp))
              .background(Color.LightGray,RoundedCornerShape(4.dp))
              .clickable(
                onClick = {
                  onBackspace()
                }
              ),
            Alignment.Center
          ) {
            Text(
              text = "⌫",
              color = MaterialTheme.colorScheme.tertiary,
              fontSize = 18.sp
            )
          }
        }
      }
    }
  }
}


