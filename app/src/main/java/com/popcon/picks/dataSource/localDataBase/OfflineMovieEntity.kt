package com.popcon.picks.dataSource.localDataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.popcon.picks.model.movieDetails.BelongsToCollection
import com.popcon.picks.model.movieDetails.Genres
import com.popcon.picks.model.movieDetails.ProductionCompanies
import com.popcon.picks.model.movieDetails.ProductionCountries
import com.popcon.picks.model.movieDetails.SpokenLanguages

@Entity(tableName = "movies")
data class OfflineMovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "adult")
    val adult: Boolean?,

    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String?,

    @ColumnInfo(name = "belongs_to_collection")
    val belongsToCollection: BelongsToCollection?,

    @ColumnInfo(name = "budget")
    val budget: Int?,

    @ColumnInfo(name = "genres")
    val genres: List<Genres>,

    @ColumnInfo(name = "homepage")
    val homepage: String?,

    @ColumnInfo(name = "movieId")
    val movieId: Int?,

    @ColumnInfo(name = "imdb_id")
    val imdbId: String?,

    @ColumnInfo(name = "origin_country")
    val originCountry: List<String>,

    @ColumnInfo(name = "original_language")
    val originalLanguage: String?,

    @ColumnInfo(name = "original_title")
    val originalTitle: String?,

    @ColumnInfo(name = "overview")
    val overview: String?,

    @ColumnInfo(name = "popularity")
    val popularity: Double?,

    @ColumnInfo(name = "poster_path")
    val posterPath: String?,

    @ColumnInfo(name = "production_companies")
    val productionCompanies: List<ProductionCompanies>,

    @ColumnInfo(name = "production_countries")
    val productionCountries: List<ProductionCountries>,

    @ColumnInfo(name = "release_date")
    val releaseDate: String?,

    @ColumnInfo(name = "revenue")
    val revenue: Int?,

    @ColumnInfo(name = "runtime")
    val runtime: Int?,

    @ColumnInfo(name = "spoken_languages")
    val spokenLanguages: List<SpokenLanguages>,

    @ColumnInfo(name = "status")
    val status: String?,

    @ColumnInfo(name = "tagline")
    val tagline: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "video")
    val video: Boolean?,

    @ColumnInfo(name = "vote_average")
    val voteAverage: Double?,

    @ColumnInfo(name = "vote_count")
    val voteCount: Int?
)
