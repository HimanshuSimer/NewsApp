package com.app.newsapp.retrofitapi

import com.app.newsapp.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    // Search by user
    @GET("everything")
    fun searchNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = "2266a51fe62842fa9aa040a870d8e2bd"
    ): Call<NewsResponse>
}