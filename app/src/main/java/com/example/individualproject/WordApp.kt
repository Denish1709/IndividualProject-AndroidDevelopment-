package com.example.individualproject

import android.app.Application
import androidx.room.Room
import com.example.individualproject.data.db.WordDatabase
import com.example.individualproject.data.repository.WordRepository

class WordApp: Application() {
    lateinit var repo: WordRepository

    override fun onCreate() {
        super.onCreate()
        val db = Room.databaseBuilder(
            this,
            WordDatabase::class.java,
            WordDatabase.NAME
        ).fallbackToDestructiveMigration().build()

        repo = WordRepository(db.wordDao())
    }
}