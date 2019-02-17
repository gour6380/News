package com.gourav.news.network

import com.gourav.news.models.ArticleResponseWrapper
import com.gourav.news.models.SourceResponseWrapper

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {
    //TODO add Your api key at the end of  X-Api-Key:
    @Headers("X-Api-Key:")
    @GET("/v2/top-headlines")
    fun getHeadlines(
            @Query("category") category: String,
            @Query("country") country: String
    ): Call<ArticleResponseWrapper>
//TODO add Your api key at the end of  X-Api-Key:
    @Headers("X-Api-Key:")
    @GET("/v2/sources")
    fun getSources(
            @Query("category") category: String,
            @Query("country") country: String,
            @Query("language") language: String
    ): Call<SourceResponseWrapper>

    enum class Category(val title: String) {
        Business("Business"),
        Entertainment("Entertainment"),
        General("General"),
        Health("Health"),
        Science("Science"),
        Sports("Sports"),
        Technology("Technology")
    }
}