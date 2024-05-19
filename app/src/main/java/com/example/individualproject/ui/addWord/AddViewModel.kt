package com.example.individualproject.ui.addWord

import android.text.Spannable.Factory
import android.util.Log
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

class AddViewModel(
    private val repo : WordRepository
): ViewModel() {

    val title: MutableLiveData<String> = MutableLiveData()
    val meaning: MutableLiveData<String> = MutableLiveData()
    val synonym: MutableLiveData<String> = MutableLiveData()
    val detail: MutableLiveData<String> = MutableLiveData()
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val snackbar: MutableLiveData<String> = MutableLiveData()

    fun add() {
        if (title.value.isNullOrEmpty() || meaning.value.isNullOrEmpty() || synonym.value.isNullOrEmpty() || detail.value.isNullOrEmpty()) {
            snackbar.value = "All fields cannot be empty"
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            if (title.value != null && meaning.value != null && synonym.value != null) {
                repo.addWord(
                    Word(
                        title = title.value!!,
                        meaning = meaning.value!!,
                        synonyms = synonym.value!!,
                        details = detail.value!!,
                        status = false
                    )
                )
                withContext(Dispatchers.Main) {
                    snackbar.value = "Add New Word Successfully"
                }
                finish.emit(Unit)
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo = (this[APPLICATION_KEY] as WordApp).repo
                AddViewModel(repo)
            }
        }
    }
}