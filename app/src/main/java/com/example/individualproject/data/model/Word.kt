package com.example.individualproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Word(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val meaning: String,
    val synonyms: String,
    val details: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val status: Boolean? = false,
    val priority: Byte = 0
)
