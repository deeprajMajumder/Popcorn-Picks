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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.popcon.picks.dataSource.localDataBase.OfflineEntity
import com.popcon.picks.ui.theme.PopcornPicksTheme
import com.popcon.picks.utils.Constants
import com.popcon.picks.views.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
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
            val isDarkTheme by mainViewModel.isDarkTheme.collectAsState()
            val scaffoldState = rememberScaffoldState()
            val scope = rememberCoroutineScope()
            PopcornPicksTheme(
                darkTheme = isDarkTheme
            ) {
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(topBar = {
                        TopAppBar(
                            title = { Text("Popcorn Picks") },
                            navigationIcon = {
                                IconButton(onClick = { scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Menu drawer")
                                }
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = Color.Yellow
                            )
                        )
                    },
                        scaffoldState = scaffoldState,
                        drawerContent = {
                            DrawerAppComponent(isDarkTheme)
                        },
                        drawerBackgroundColor = MaterialTheme.colors.surface,
                        drawerContentColor = MaterialTheme.colors.onSurface,
                        drawerScrimColor = Color.Transparent,
                        drawerGesturesEnabled = true
                        ) { innerPadding ->
                        AppNavigation(innerPadding, mainViewModel)
                    }
                }
            }
        }
    }

    @Composable
    fun DrawerAppComponent(isDarkTheme: Boolean) {

        ModalDrawerSheet{
            LazyColumn(modifier = Modifier.padding(16.dp)) {
                item {
                    Text(text = "Settings", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row( modifier = Modifier
                        .fillMaxWidth()
                        .clickable {  },
                        verticalAlignment = Alignment.CenterVertically){
                        Text(text = "Dark Mode", modifier = Modifier.weight(1f))
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { mainViewModel.toggleTheme() }
                        )

                    }
                }
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
                MovieGridView(movieList = mainViewModel.pagedMovies)
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
            CircularProgressIndicator(color = Color.Yellow)
        }
    }

    @Composable
    fun MovieGridView(movieList: Flow<PagingData<OfflineEntity>>) {
        val lazyMovieItems = movieList.collectAsLazyPagingItems()

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(16.dp)
        ) {
            items(lazyMovieItems.itemCount) { index ->
                val movie = lazyMovieItems[index]
                if (movie != null) {
                    MovieThumbnail(movie) {
                        val intent = Intent(this@MainActivity, MovieDetailsActivity::class.java).apply {
                            putExtra("movieId", movie.movieId)
                        }
                        Log.d(TAG, "Starting MovieDetailsActivity with movieId: ${movie.movieId}")
                        startActivity(intent)
                    }
                }
            }

            lazyMovieItems.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        item {
                            LoadingView()
                        }
                    }
                    loadState.append is LoadState.Loading -> {
                        item {
                            LoadingView()
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        val e = lazyMovieItems.loadState.append as LoadState.Error
                        item {
                            Text("Error: ${e.error.localizedMessage}", color = Color.Red)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MovieThumbnail(movie: OfflineEntity?, onClick: () -> Unit) {
        Card(
            shape = RoundedCornerShape(6.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier
                .padding(10.dp)
                .clickable { onClick() }
        ) {
            Column(verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(10.dp)) {
                Image(
                    painter = rememberAsyncImagePainter(model = Constants.imageUrlLowRes + movie?.posterPath),
                    contentDescription = movie?.title,
                    modifier = Modifier.size(150.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
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
    enum class DrawerAppScreen {
        Screen1,
        Screen2,
        Screen3
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        PopcornPicksTheme {
            AppNavigation(PaddingValues(0.dp),mainViewModel)
        }
    }
}