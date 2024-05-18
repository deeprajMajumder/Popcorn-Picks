package com.popcon.picks.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.material.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.popcon.picks.model.Movies
import com.popcon.picks.ui.theme.PopcornPicksTheme
import com.popcon.picks.utils.Constants
import com.popcon.picks.views.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PopcornPicksTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation(mainViewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.MovieGrid.route) {
        composable(Screen.MovieGrid.route) {
            MovieGridView(navController, mainViewModel)
        }
        composable(Screen.MovieDetails.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toInt() ?: return@composable
            val movie = mainViewModel.movies.collectAsState().value.find { it.id == movieId }
            movie?.let { MovieDetailView(it, navController) }
        }
    }
}
@Composable
fun MovieGridView(navController: NavController, mainViewModel: MainViewModel) {
    val movies = mainViewModel.movies.collectAsState().value

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp)
    ) {
        items(movies.size) { index ->
            val movie = movies[index]
            MovieThumbnail(movie) {
                navController.navigate(Screen.MovieDetails.createRoute(movie.id!!))
            }
        }
    }
}
@Composable
fun MovieThumbnail(movie: Movies, onClick: () -> Unit) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .size(150.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = Constants.imageUrlLowRes+movie.posterPath),
            contentDescription = movie.title,
            modifier = Modifier.size(150.dp)
        )
    }
}
@Composable
fun MovieDetailView(movie: Movies, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = Constants.imageUrl+movie.posterPath),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.title!!, style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Release Date: ${movie.releaseDate}", style = MaterialTheme.typography.body2)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Vote average: ${movie.voteAverage}", style = MaterialTheme.typography.body2)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = movie.overview!!, style = MaterialTheme.typography.body1)
        Spacer(modifier = Modifier.height(16.dp))
        var isWishlisted by remember { mutableStateOf(false) }
        IconButton(onClick = { isWishlisted = !isWishlisted }) {
            Icon(
                imageVector = if (isWishlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object MovieGrid : Screen("movie_grid")
    data object MovieDetails : Screen("movie_details/{movieId}") {
        fun createRoute(movieId: Int) = "movie_details/$movieId"
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PopcornPicksTheme {
       AppNavigation()
    }
}