package com.example.newsapp.RETRO

import com.example.newsapp.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String,
        @Query("page")
        pagenumber:Int=1,
        @Query("apikey")
        APIKEY:String=API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchNews(
        @Query("q")
        category:String,
        @Query("page")
        pagenumber:Int=1,
        @Query("apikey")
        APIKEY:String=API_KEY
    ):Response<NewsResponse>


}