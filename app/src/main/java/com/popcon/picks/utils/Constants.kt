package com.popcon.picks.utils

import com.popcon.picks.BuildConfig

object Constants {
    const val NETWORK_REQUEST_TIMEOUT_SECONDS = 30L
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val LAST_FETCHED_PAGE_KEY = "last_fetched_page"
    const val language = "en-US"
    const val apiKey = BuildConfig.API_KEY
    const val imageUrl = "https://image.tmdb.org/t/p/w500/"
    const val imageUrlLowRes = "https://image.tmdb.org/t/p/w200/"
}