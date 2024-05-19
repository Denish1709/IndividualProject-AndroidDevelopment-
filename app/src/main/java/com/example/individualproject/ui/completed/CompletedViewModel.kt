package com.example.individualproject.ui.completed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.individualproject.WordApp
import com.example.individualproject.data.model.Word
import com.example.individualproject.data.repository.WordRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CompletedViewModel(
    private val repo : WordRepository
): ViewModel() {
    private val _words = MutableLiveData<List<Word>>()
    val words: LiveData<List<Word>> get() = _words

    fun getCompletedWords() {
        viewModelScope.launch {
            val filteredWords = repo.getCompletedAll().map { words ->
                words.filter { it.status == true }
            }.first()
            _words.value = filteredWords
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                CompletedViewModel (
                    (this[APPLICATION_KEY] as WordApp).repo
                )
            }
        }
    }
}
