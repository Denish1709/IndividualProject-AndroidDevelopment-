package com.example.individualproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.individualproject.data.model.Word
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao{

    @Query("SELECT * FROM Word WHERE status = 0 ORDER BY title DESC")
    fun getAll(): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE status = 1 ORDER BY title DESC")
    fun getCompletedAll(): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE id = :id")
    fun getWordById(id: Int):Word?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWord(word: Word)

    @Query("UPDATE Word SET status = :status WHERE id = :id")
    fun updateWordStatus(id: Int, status: Boolean)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWord(word: Word)

    @Delete
    fun deleteWord(word: Word)

    @Query("SELECT status FROM Word WHERE id= :id")
    fun getWordStatus(id: Int): Boolean
}
