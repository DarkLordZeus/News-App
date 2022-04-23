package com.example.newsapp.RETRO

import com.example.newsapp.RETRO.Article

data class NewsResponse(
    var articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)