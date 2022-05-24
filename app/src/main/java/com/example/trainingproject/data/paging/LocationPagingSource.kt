package com.example.trainingproject.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.trainingproject.data.api.LocationService
import com.example.trainingproject.model.Location

class LocationPagingSource(
    private val api: LocationService
) : PagingSource<Int, Location>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Location> {
        return try {
            val currentPage = params.key ?: 1
            val response = api.getLocations(currentPage)
            LoadResult.Page(
                data = response.data.data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (currentPage == response.data.last_page) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Location>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}