package com.example.newsapp.repository

import com.example.newsapp.RETRO.RetrofitInstance
import com.example.newsapp.database.NewsDatabase

class NewsRepository(database: NewsDatabase) {
        suspend fun getBreakingNews(countryCode:String,page:Int)=
                RetrofitInstance.Companion.NEWSAPI.retrofitservice.getBreakingNews(countryCode,page)
        suspend fun searchNews(category:String,page: Int)=
                RetrofitInstance.Companion.NEWSAPI.retrofitservice.searchNews(category,page)
}