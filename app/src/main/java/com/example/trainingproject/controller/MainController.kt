package com.example.trainingproject.controller

import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.trainingproject.data.api.LocationApi
import com.example.trainingproject.data.paging.LocationPagingSource
import com.example.trainingproject.view.MainActivity

class MainController(private val view: MainActivity) {

    val getLocations = Pager(
        config = PagingConfig(pageSize = 15)
    ) {
        LocationPagingSource(LocationApi.apiService)
    }.flow.cachedIn(view.lifecycleScope)
}