package com.popcon.picks.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.popcon.picks.model.movieDetails.BelongsToCollection
import com.popcon.picks.model.movieDetails.Genres
import com.popcon.picks.model.movieDetails.ProductionCompanies
import com.popcon.picks.model.movieDetails.ProductionCountries
import com.popcon.picks.model.movieDetails.SpokenLanguages

class Converters {
    private val gson = Gson()
    @TypeConverter
    fun fromGenreIdsList(genreIds: ArrayList<Int>): String {
        return Gson().toJson(genreIds)
    }

    @TypeConverter
    fun toGenreIdsList(genreIdsString: String): ArrayList<Int> {
        val listType = object : TypeToken<ArrayList<Int>>() {}.type
        return Gson().fromJson(genreIdsString, listType)
    }

    @TypeConverter
    fun fromString(value: String?): BelongsToCollection? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<BelongsToCollection>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toString(belongsToCollection: BelongsToCollection?): String? {
        return if (belongsToCollection == null) {
            null
        } else {
            gson.toJson(belongsToCollection)
        }
    }

    @TypeConverter
    fun fromGenresString(value: String?): List<Genres>? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<List<Genres>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toGenresString(genres: List<Genres>?): String? {
        return if (genres == null) {
            null
        } else {
            gson.toJson(genres)
        }
    }

    @TypeConverter
    fun fromOriginCountryString(value: String?): List<String>? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toOriginCountryString(list: List<String>?): String? {
        return if (list == null) {
            null
        } else {
            gson.toJson(list)
        }
    }
    @TypeConverter
    fun fromProductionCompaniesString(value: String?): List<ProductionCompanies>? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<List<ProductionCompanies>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toProductionCompaniesString(list: List<ProductionCompanies>?): String? {
        return if (list == null) {
            null
        } else {
            gson.toJson(list)
        }
    }
    @TypeConverter
    fun fromProductionCountriesString(value: String?): List<ProductionCountries>? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<List<ProductionCountries>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toProductionCountriesString(list: List<ProductionCountries>?): String? {
        return if (list == null) {
            null
        } else {
            gson.toJson(list)
        }
    }

    @TypeConverter
    fun fromSpokenLanguagesString(value: String?): List<SpokenLanguages>? {
        if (value == null) {
            return null
        }
        val type = object : TypeToken<List<SpokenLanguages>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun toSpokenLanguagesString(list: List<SpokenLanguages>?): String? {
        return if (list == null) {
            null
        } else {
            gson.toJson(list)
        }
    }
}