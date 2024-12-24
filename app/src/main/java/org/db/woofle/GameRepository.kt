package org.db.woofle

import android.content.Context

class GameRepository(context: Context) {
  private val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

  fun saveLevel(value: Int) {
    sharedPreferences.edit().putInt("lastLevel", value).apply()
  }

  fun getLevel(): Int {
    return sharedPreferences.getInt("lastLevel", 0)
  }
}