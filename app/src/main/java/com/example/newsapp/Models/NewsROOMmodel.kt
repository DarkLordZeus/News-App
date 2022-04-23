package com.example.newsapp.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.RETRO.Article
import com.example.newsapp.database.NewsDatabase
import com.example.newsapp.database.RoomRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsROOMmodel(application: Application):AndroidViewModel(application) {

    val readAllData: LiveData<List<Article>>
    private val repository: RoomRepo
    //lateinit var countcheck:MutableLiveData<Long>



    init{
        val newsDao= NewsDatabase.getDatabase(application).newsDao()
        repository= RoomRepo(newsDao)
        readAllData=repository.readAllnews

    }

    fun addNews(article: Article){
        viewModelScope.launch(Dispatchers.IO){
            repository.addnews(article)
        }
    }

    fun deleteNews(article: Article)
    {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteNews(article)
        }
    }

    fun checkurl(url:String):LiveData<Long>{
       return repository.checkurl(url)
    }

}