package com.example.task.data.repository

import com.example.task.data.remote.ApiService
import javax.inject.Inject

class Repository @Inject constructor(
    private val api : ApiService
) {

    suspend fun getAds() = api.getSliderAds()

    suspend fun getCategory() = api.getCategories()

    suspend fun getSellingRestaurants() = api.getMostSelling()

    suspend fun getSellingDishes() = api.getMostSelling()

}