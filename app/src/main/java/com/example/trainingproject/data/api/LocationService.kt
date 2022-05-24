package com.example.trainingproject.data.api

import com.example.trainingproject.model.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {

    @GET("locations")
    suspend fun getLocations(
        @Query("page") page: Int
    ): LocationResponse
}