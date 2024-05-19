package com.popcon.picks.views.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.popcon.picks.dataSource.Repository
import com.popcon.picks.dataSource.localDataBase.OfflineMovieEntity
import com.popcon.picks.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: Repository,
    @ApplicationContext private val applicationContext: Context
) : ViewModel() {
    private val TAG = MovieDetailsViewModel::class.java.simpleName
    private val _uiState = MutableStateFlow<MovieDataUiState>(MovieDataUiState.Empty)
    val uiState: StateFlow<MovieDataUiState> = _uiState

    fun getMovieDetails(movieId: Int) {
        _uiState.value = MovieDataUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getMovieDetails(movieId, Constants.apiKey)
                Log.d(TAG, "Movie details response: ${response.title}")
                _uiState.value = MovieDataUiState.Loaded(response)
            } catch (e: Exception) {
                _uiState.value = MovieDataUiState.Error(e.message ?: "Error fetching movie details")
                Log.e(TAG, "Error fetching movie details", e)
            }
        }
    }
    sealed class MovieDataUiState {
        data object Loading : MovieDataUiState()
        data object Empty : MovieDataUiState()
        data class Loaded(val movies: OfflineMovieEntity) : MovieDataUiState()
        data class Error(val message: String) : MovieDataUiState()
    }

}