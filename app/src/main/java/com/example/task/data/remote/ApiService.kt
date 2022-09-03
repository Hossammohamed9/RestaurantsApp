package com.example.task.data.remote

import com.example.task.data.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("MobileMainPage/GetMainSliders")
    suspend fun getSliderAds(
        @Header("lang") lang : String = "en",
        @Body googleId : RequestBody = RequestBody("ChIJ88rv8bI_WBQRkvVBLDeZQUg")) : Response<List<SliderAdsItem>>

    @GET("Categories/index")
    suspend fun getCategories() : Response<List<Category>>

    @POST("MobileMainPage/GetHomePage")
    suspend fun getMostSelling(
        @Header("lang") lang : String = "en",
        @Body googleId : RequestBody = RequestBody("ChIJ88rv8bI_WBQRkvVBLDeZQUg")) : Response<HomePage>



}