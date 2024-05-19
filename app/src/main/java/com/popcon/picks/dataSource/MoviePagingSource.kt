package com.popcon.picks.dataSource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.popcon.picks.dataSource.localDataBase.OfflineEntity
import java.io.IOException
import javax.inject.Inject


class MoviePagingSource @Inject constructor(
    private val repository: Repository,
    private val language: String,
    private val apiKey: String
) : PagingSource<Int, OfflineEntity>() {
    private val TAG = MoviePagingSource::class.java.simpleName
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OfflineEntity> {
        val page = params.key ?: 1
        Log.d(TAG, "page value: $page")
        return try {
            val movies = repository.getMovieList(language, apiKey, page)
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (movies.isEmpty()) null else page + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, OfflineEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}