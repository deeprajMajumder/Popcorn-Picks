package com.popcon.picks.model

import com.google.gson.annotations.SerializedName

data class MoviesParent(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("results") var results: ArrayList<Movies> = arrayListOf(),
    @SerializedName("total_pages") var totalPages: Int? = null,
    @SerializedName("total_results") var totalResults: Int? = null
)
