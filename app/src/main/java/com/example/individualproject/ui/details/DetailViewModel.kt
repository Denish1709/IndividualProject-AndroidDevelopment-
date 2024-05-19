package com.example.individualproject.ui.details

import android.text.Editable.Factory
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repo: WordRepository
) : ViewModel() {

    private val _word: MutableLiveData<Word> = MutableLiveData()
    val selectedWord: LiveData<Word> = _word
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val title: MutableLiveData<String> = MutableLiveData()
    val meaning: MutableLiveData<String> = MutableLiveData()
    val synonyms: MutableLiveData<String> = MutableLiveData()
    val details: MutableLiveData<String> = MutableLiveData()

    fun getWordById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _word.postValue(repo.getWordById(id))
        }
    }

    fun moveWord() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentWord = selectedWord.value
            if (currentWord != null) {
                val updatedStatus = !currentWord.status!!
                val updatedWord = currentWord.copy(status = updatedStatus)
                repo.updateWord(updatedWord)
                finish.emit(Unit)
            }
        }
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteWord(selectedWord.value!!)
            finish.emit(Unit)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                DetailViewModel(
                    (this[APPLICATION_KEY] as WordApp).repo
                )
            }
        }
    }
}