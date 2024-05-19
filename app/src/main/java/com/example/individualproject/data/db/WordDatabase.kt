package com.example.individualproject.data.db

import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.individualproject.data.model.Converter
import com.example.individualproject.data.model.Word

@Database(entities = [Word::class], version = 2)
@TypeConverters(Converter::class)
abstract class WordDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        const val NAME = "my_word_database"
    }
}