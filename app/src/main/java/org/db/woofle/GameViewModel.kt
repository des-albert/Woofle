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

class GameViewModel(private val repository: GameRepository): ViewModel() {
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
  var guessCount by mutableStateOf(0)
    private set


  private var words = listOf<String>()
  private var validWords = setOf<String>()
  private var targetWord = ""
  private var currentIndex = 0


  fun loadWords(context: Context) {
    viewModelScope.launch {

      words = withContext(Dispatchers.IO)     {
        readWordsFromAssets(context)
      }
      validWords = withContext(Dispatchers.IO)     {
        readValidWordsFromAssets(context)
      }
      selectTargetWord()
    }
  }

  private fun selectRandomTargetWord() {
    targetWord = words.random()
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


  fun submitGuess() {
    if (currentGuess.length == 5) {
      if (validWords.contains(currentGuess)) {
        guesses.add(currentGuess)
        colors.add(checkGuess(currentGuess, targetWord))
        updateKeyStates(currentGuess, targetWord)
        guessCount++
        currentGuess = ""
        if (guesses.last() == targetWord) {
          message = "Correct in $guessCount attempts"
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
    level++
    selectTargetWord()
    guesses.clear()
    colors.clear()
    keyStates.clear()
    guessCount = 0
    isGuessCorrect = false
    message = "Guess the word!"
  }


  private fun checkGuess(guess: String, targetWord: String): List<Color> {
    val result = MutableList(guess.length) { Color(0xFF5B5B5B) }
    val targetWordChars = targetWord.toMutableList()

    // First pass: Check for correct letters in the correct position
    guess.forEachIndexed { index, c ->
      if (c == targetWord[index]) {
        result[index] = Color(0xFF4CAF50)
        targetWordChars[index] = ' ' // Mark this character as used
      }
    }

    // Second pass: Check for correct letters in the wrong position
    guess.forEachIndexed { index, c ->
      if (result[index] != Color(0xFF4CAF50) && targetWordChars.contains(c)) {
        result[index] = Color(0xFFFFA000)
        targetWordChars[targetWordChars.indexOf(c)] = ' ' // Mark this character as used
      } else if (result[index] != Color(0xFF4CAF50)) {
        result[index] = Color(0xFF5B5B5B) // Incorrect letter
      }
    }

    return result
  }

  private fun updateKeyStates(guess: String, targetWord: String) {
    guess.forEachIndexed { index, c ->
      if (c == targetWord[index]) {
        keyStates[c] = Color(0xFF4CAF50)
      } else if (targetWord.contains(c) && keyStates[c] != Color(0xFF4CAF50)) {
        keyStates[c] = Color(0xFFFFA000)
      } else if (keyStates[c] != Color(0xFF4CAF50) && keyStates[c] != Color(0xFFFFA000)) {
        keyStates[c] = Color(0xFF5B5B5B)
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
}