package com.example.newsapp.database

import androidx.lifecycle.LiveData
import com.example.newsapp.RETRO.Article
import com.androiddevs.mvvmnewsapp.database.NewsDAO

class RoomRepo(private val newsDAO: NewsDAO) {

    val readAllnews: LiveData<List<Article>> = newsDAO.readalldata()


    suspend fun addnews(article: Article){
        newsDAO.insertnews(article)
    }

    suspend fun deleteNews(article: Article){
        newsDAO.deletenews(article)
    }

    fun checkurl(url:String): LiveData<Long> {
        return newsDAO.checkurl(url)
    }
}