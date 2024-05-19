package com.example.individualproject.ui.updateWord

import android.text.Spannable.Factory
import android.util.Log
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
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class UpdateViewModel(
    private val repo : WordRepository
): ViewModel() {

    private val _word: MutableLiveData<Word> = MutableLiveData()
    val selectedWord: LiveData<Word> = _word
    val title: MutableLiveData<String> = MutableLiveData()
    val meaning: MutableLiveData<String> = MutableLiveData()
    val synonyms: MutableLiveData<String> = MutableLiveData()
    val details: MutableLiveData<String> = MutableLiveData()
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val snackbar: MutableLiveData<String> = MutableLiveData()
    val word: LiveData<Word> = _word



    fun getWordById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _word.postValue(repo.getWordById(id))
        }
    }

    fun setWord(word: Word) {
        word?.let {
            title.value = it.title
            meaning.value = it.meaning
            synonyms.value = it.synonyms
            details.value = it.details
        }
    }

    fun updateWord() {
        if(title.value.isNullOrEmpty() || meaning.value.isNullOrEmpty() || synonyms.value.isNullOrEmpty() || details.value.isNullOrEmpty()) {
            snackbar.value = "Please fill in all fields"
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val word = selectedWord.value
            if (word != null) {
                repo.updateWord(
                    word.copy(
                        title = title.value!!,
                        meaning = meaning.value!!,
                        synonyms = synonyms.value!!,
                        details = details.value!!,
                        date = LocalDateTime.now()
                    )
                )
                withContext(Dispatchers.Main) {
                    snackbar.value = "Successfully Updated"
                }
            }
            finish.emit(Unit)
        }
        Log.d("update", "${title.value}, ${meaning.value}, ${synonyms.value}, ${details.value}")
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY]) as WordApp
                UpdateViewModel(app.repo)
            }
        }
    }
}