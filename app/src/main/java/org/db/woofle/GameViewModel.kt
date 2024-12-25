package org.db.woofle

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(private val repository: GameRepository) : ViewModel() {
  var guesses = mutableStateListOf<String>()
    private set
  var currentGuess by mutableStateOf("")
    private set
  var message by mutableStateOf("Guess the word")
    private set
  var colors = mutableStateListOf<List<Color>>()
    private set
  var level by mutableIntStateOf(1)
    private set
  var keyStates = mutableStateMapOf<Char, Color>()
    private set
  var isGuessCorrect by mutableStateOf(false)
    private set
  var guessCount by mutableIntStateOf(0)
    private set
  var history = mutableStateListOf<Int>(0, 0, 0, 0, 0, 0)

  private var words = listOf<String>()
  private var validWords = setOf<String>()
  private var targetWord = ""
  private var currentIndex = 0
  private var correctColor: Color = Color(0x7F000000)
  private var closeColor: Color = Color(0x7F000000)
  private var incorrectColor: Color = Color(0x7F000000)


  fun loadWords(context: Context) {
    viewModelScope.launch {

      words = withContext(Dispatchers.IO) {
        readWordsFromAssets(context)
      }
      validWords = withContext(Dispatchers.IO) {
        readValidWordsFromAssets(context)
      }
      selectTargetWord()
    }
  }


  private fun selectTargetWord() {
    if (words.isNotEmpty()) {
      targetWord = words[currentIndex]
      currentIndex = (currentIndex + 1) % words.size
    }
  }

  private fun readWordsFromAssets(context: Context): List<String> {
    return context.assets.open("top.txt").bufferedReader().useLines { lines ->
      lines.map { it.uppercase() }.toList()
    }
  }

  private fun readValidWordsFromAssets(context: Context): Set<String> {
    return context.assets.open("words.txt").bufferedReader().useLines { lines ->
      lines.toSet()
    }
  }

  fun updateGuess(char: Char) {
    if (currentGuess.length < 5) {
      currentGuess += char
    }
  }

  fun deleteLastChar() {
    if (currentGuess.isNotEmpty()) {
      currentGuess = currentGuess.dropLast(1)
    }
  }

  fun setColors(guessColors: List<Color>) {
    correctColor = guessColors[0]
    closeColor = guessColors[1]
    incorrectColor = guessColors[2]

  }


  fun submitGuess() {
    if (currentGuess.length == 5) {
      if (validWords.contains(currentGuess)) {
        guesses.add(currentGuess)
        colors.add(checkGuess(currentGuess, targetWord))
        updateKeyStates(currentGuess, targetWord)
        guessCount = guessCount + 1
        currentGuess = ""
        if (guesses.last() == targetWord) {
          message = "Correct in $guessCount attempts"
          history[guessCount - 1] = history[guessCount - 1] + 1
          isGuessCorrect = true
        } else if (guesses.size >= 6) {
          message = "Game over! The word was $targetWord."
          level = 1
          selectTargetWord()
          guesses.clear()
          colors.clear()
          keyStates.clear()
          guessCount = 0
        } else {
          message = "Try again!"
        }
      } else {
        message = "Invalid word! Try again."
      }
    }
  }

  fun proceedToNextLevel() {
    saveCurrentLevel(level)
    saveHistory(history)
    level++
    selectTargetWord()
    guesses.clear()
    colors.clear()
    keyStates.clear()
    guessCount = 0
    isGuessCorrect = false
    message = "Guess the word"
  }


  private fun checkGuess(guess: String, targetWord: String): List<Color> {
    val result = MutableList(guess.length) { incorrectColor }
    val targetWordChars = targetWord.toMutableList()

    // First pass: Check for correct letters in the correct position
    guess.forEachIndexed { index, c ->
      if (c == targetWord[index]) {
        result[index] = correctColor
        targetWordChars[index] = ' ' // Mark this character as used
      }
    }

    // Second pass: Check for correct letters in the wrong position
    guess.forEachIndexed { index, c ->
      if (result[index] != correctColor && targetWordChars.contains(c)) {
        result[index] = closeColor
        targetWordChars[targetWordChars.indexOf(c)] = ' ' // Mark this character as used
      } else if (result[index] != correctColor) {
        result[index] = incorrectColor // Incorrect letter
      }
    }

    return result
  }

  private fun updateKeyStates(guess: String, targetWord: String) {
    guess.forEachIndexed { index, c ->
      if (c == targetWord[index]) {
        keyStates[c] = correctColor
      } else if (targetWord.contains(c) && keyStates[c] != correctColor) {
        keyStates[c] = closeColor
      } else if (keyStates[c] != correctColor && keyStates[c] != closeColor) {
        keyStates[c] = incorrectColor
      }
    }
  }

  fun saveCurrentLevel(level: Int) {
    repository.saveLevel(level)
  }

  fun loadCurrentLevel() {
    level = repository.getLevel() + 1
    currentIndex = level - 1
  }

  fun saveHistory(history: List<Int>) {
    val historyString = history.joinToString(separator = ",")
    repository.saveHistory(historyString)
  }

  fun loadHistory() {
    var historyString = repository.getHistory()
    if (!historyString.isNullOrEmpty()) {
      val historyList = historyString.split(",").map { it.trim().toInt() }
      history.clear()
      history.addAll(historyList)

    }
  }

}