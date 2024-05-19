package com.example.individualproject.data.repository

import com.example.individualproject.data.db.WordDao
import com.example.individualproject.data.model.Word
import kotlinx.coroutines.flow.Flow

class WordRepository(
    private val dao:WordDao
) {
    fun getAllWords(): Flow<List<Word>> {
        return dao.getAll()
    }

    fun getWordById(id: Int): Word? {
        return dao.getWordById(id)
    }

    fun getCompletedAll(): Flow<List<Word>> {
        return dao.getCompletedAll()
    }

    fun addWord(word: Word) {
         dao.addWord(word)
    }

    fun updateWord(word: Word) {
        dao.updateWord(word)
    }

    fun deleteWord(word: Word) {
        dao.deleteWord(word)
    }


}