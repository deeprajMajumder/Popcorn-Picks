package com.popcon.picks.views

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popcon.picks.dataSource.network.Repository
import com.popcon.picks.model.Movies
import com.popcon.picks.utils.Constants
import com.popcon.picks.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    @ApplicationContext private val applicationContext: Context
    ) : ViewModel() {
        private val TAG = MainViewModel::class.java.simpleName
    private val _movies = MutableStateFlow<List<Movies>>(emptyList())
    val movies: StateFlow<List<Movies>> = _movies

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        viewModelScope.launch {
            try {
                if(NetworkUtils.isOnline(applicationContext)){
                    val response = repository.getMoviesList(Constants.language, Constants.apiKey, 1)
                    if (response.isSuccessful) {
                        val moviesList = response.body()?.results // Add Movie results from the response structure
                        if (moviesList != null) {
                            _movies.value = moviesList
                        } else {
                            Toast.makeText(applicationContext, "Please try again", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e(TAG, "Error response : ${response.errorBody()}")
                        Toast.makeText(applicationContext, "Please try again", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching movies: ${e.message}")
                Toast.makeText(applicationContext, "Unknown Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun getMovieThumbnail(posterPath: String) {
        viewModelScope.launch {
            if(NetworkUtils.isOnline(applicationContext)){
                val response = repository.getMovieThumbnail(posterPath)
            }
        }
    }
}