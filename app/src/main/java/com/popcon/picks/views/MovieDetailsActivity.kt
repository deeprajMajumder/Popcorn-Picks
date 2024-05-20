package com.popcon.picks.views

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.popcon.picks.dataSource.localDataBase.OfflineMovieEntity
import com.popcon.picks.ui.theme.PopcornPicksTheme
import com.popcon.picks.utils.Constants
import com.popcon.picks.views.viewModel.MovieDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MovieDetailsActivity : ComponentActivity() {
    private val TAG = MovieDetailsActivity::class.java.simpleName
    private val movieDetailsViewModel : MovieDetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieId = intent.getIntExtra("movieId",0)
        Log.d(TAG, "MovieId: $movieId")
        movieDetailsViewModel.getMovieDetails(movieId)
        enableEdgeToEdge()
        setContent {
            PopcornPicksTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MovieDetailScreen()
                }
            }
        }
    }
    @Composable
    fun MovieDetailScreen() {

        when (val state = movieDetailsViewModel.uiState.collectAsState().value) {
            is MovieDetailsViewModel.MovieDataUiState.Empty -> {}
            is MovieDetailsViewModel.MovieDataUiState.Loading -> {
                LoadingView()
            }
            is MovieDetailsViewModel.MovieDataUiState.Loaded -> {
                MovieDetailView(state.movies)
            }
            is MovieDetailsViewModel.MovieDataUiState.Error -> {
                Toast.makeText(this@MovieDetailsActivity, state.message, Toast.LENGTH_LONG).show()
            }
            else -> {}
        }
    }
    @Composable
    fun LoadingView() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator(color = Color.Yellow)
        }
    }

    @Composable
    fun MovieDetailView(movie: OfflineMovieEntity) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = movie.title!!) },
                    navigationIcon = {
                        IconButton(onClick = { finish() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Yellow
                    )
                )
            },
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    item {
                        Image(
                            painter = rememberAsyncImagePainter(model = Constants.imageUrl + movie.posterPath),
                            contentDescription = movie.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = movie.title!!,
                            style = MaterialTheme.typography.h5
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Release Date: ${movie.releaseDate}", style = MaterialTheme.typography.body2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Genre: ${movie.genres[0].name}", style = MaterialTheme.typography.body2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Runtime: ${movie.runtime} minutes", style = MaterialTheme.typography.body2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Original Language: ${movie.spokenLanguages[0].englishName}", style = MaterialTheme.typography.body2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Vote average: ${movie.voteAverage}", style = MaterialTheme.typography.body2)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = movie.overview!!, style = MaterialTheme.typography.body1)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        )
    }
}