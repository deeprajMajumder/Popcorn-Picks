package com.popcon.picks.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromGenreIdsList(genreIds: ArrayList<Int>): String {
        return Gson().toJson(genreIds)
    }

    @TypeConverter
    fun toGenreIdsList(genreIdsString: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(genreIdsString, listType)
    }
}