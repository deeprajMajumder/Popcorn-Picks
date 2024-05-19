package com.popcon.picks.views.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popcon.picks.dataSource.Repository
import com.popcon.picks.dataSource.localDataBase.OfflineEntity
import com.popcon.picks.utils.Constants
import com.popcon.picks.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
    private val _uiState = MutableStateFlow<MovieDataUiState>(MovieDataUiState.Empty)
    val uiState: StateFlow<MovieDataUiState> = _uiState

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        _uiState.value = MovieDataUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(NetworkUtils.isOnline(applicationContext)){
                    val response = repository.getMovieList(Constants.language, Constants.apiKey, 1)
                    val moviesList = response// Add Movie results from the response structure
//                    _movies.value = moviesList
                    _uiState.value = MovieDataUiState.Loaded(moviesList)
                } else {
                    _uiState.value = MovieDataUiState.Error("No Internet Connection")
                    Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching movies: ${e.message}")
                Toast.makeText(applicationContext, "Unknown Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    sealed class MovieDataUiState {
        data object Loading : MovieDataUiState()
        data object Empty : MovieDataUiState()
        data class Loaded(val movies: List<OfflineEntity?>) : MovieDataUiState()
        data class Error(val message: String) : MovieDataUiState()
    }
}