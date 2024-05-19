package com.popcon.picks.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.popcon.picks.dataSource.localDataBase.OfflineEntity
import com.popcon.picks.ui.theme.PopcornPicksTheme
import com.popcon.picks.utils.Constants
import com.popcon.picks.views.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = MainActivity::class.java.simpleName
    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            PopcornPicksTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(topBar = {
                        TopAppBar(
                            title = { Text("Popcorn Picks") },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "menu drawer")
                                }
                            }
                        )
                    },
                        drawerContent = {
                            DrawerContent(isDarkTheme) { isDark ->
                                isDarkTheme = isDark
                            }
                        }) { innerPadding ->
                        AppNavigation(innerPadding, mainViewModel)
                    }
                }
            }
        }
    }


    @Composable
    fun DrawerContent(isDarkTheme: Boolean, onThemeChange: (Boolean) -> Unit) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Settings",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeChange(!isDarkTheme) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isDarkTheme) "Switch to Light Theme" else "Switch to Dark Theme",
                    fontSize = 18.sp
                )
            }
        }
    }

    @Composable
    fun AppNavigation(
        innerPadding: PaddingValues,
        mainViewModel: MainViewModel
    ) {
        when (val state = mainViewModel.uiState.collectAsState().value) {
            is MainViewModel.MovieDataUiState.Empty -> {}
            is MainViewModel.MovieDataUiState.Loading -> {
                LoadingView()
            }
            is MainViewModel.MovieDataUiState.Loaded -> {
                MovieGridView(movieList = state.movies)
            }
            is MainViewModel.MovieDataUiState.Error -> {
                Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_LONG).show()
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
            CircularProgressIndicator()
        }
    }

    @Composable
    fun MovieGridView(movieList: List<OfflineEntity?>) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(16.dp)
        ) {
            items(movieList.size) { index ->
                val movie = movieList[index]
                MovieThumbnail(movie!!) {
                    val intent = Intent(this@MainActivity, MovieDetailsActivity::class.java).apply {
                        putExtra("movieId", movie.movieId)
                    }
                    Log.d(TAG, "Starting MovieDetailsActivity with movieId: ${movie.movieId}")
                    startActivity(intent)
                }
            }
        }
    }

    @Composable
    fun MovieThumbnail(movie: OfflineEntity?, onClick: () -> Unit) {
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .padding(8.dp)
                .clickable { onClick() }
                .size(150.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = Constants.imageUrlLowRes + movie?.posterPath),
                    contentDescription = movie?.title,
                    modifier = Modifier.size(150.dp)
                )
                Column(verticalArrangement = Arrangement.Top) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = movie?.title!!,
                        style = MaterialTheme.typography.body1,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Release Date: ${movie.releaseDate}",
                        style = MaterialTheme.typography.body2,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        PopcornPicksTheme {
            AppNavigation(PaddingValues(0.dp),mainViewModel)
        }
    }
}