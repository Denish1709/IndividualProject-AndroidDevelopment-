package com.example.individualproject.data.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converter {

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? = value?.let {
        LocalDateTime.parse(it)
    }

    @TypeConverter
    fun dateTimestamp(date: LocalDateTime?): String? = date?.toString()
}