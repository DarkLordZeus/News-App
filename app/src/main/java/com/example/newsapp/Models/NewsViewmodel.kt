package com.example.newsapp.Models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.RETRO.NewsResponse
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewmodel(private val NewsRepository: NewsRepository):ViewModel() {


    val breakingNews = MutableLiveData<Resource<NewsResponse>>()
    var breakingnewspage = 1


    val searchNews = MutableLiveData<Resource<NewsResponse>>()
    var searchnewspage = 1


    var breakingnewsresponse: NewsResponse? = null
    var searchnewsresponse: NewsResponse? = null


    init {
        getBreakingNews("in")
    }

    init {
        searchNews("latest")
    }


    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = NewsRepository.getBreakingNews(countryCode, breakingnewspage)
        breakingNews.postValue(handlebreakingresponse(response))
    }

    fun searchNews(category: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = NewsRepository.searchNews(category, searchnewspage)
        searchNews.postValue(handlesearchresponse(response))
    }

    private fun handlebreakingresponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingnewspage++
                if (breakingnewsresponse == null) {
                    breakingnewsresponse = resultResponse
                } else {
                    val oldArticles = breakingnewsresponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingnewsresponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    //RETROFIT SERVICES RESPONSE
    private fun handlesearchresponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchnewspage++
                if (searchnewsresponse == null) {
                    searchnewsresponse = resultResponse
                } else {
                    val oldArticles = searchnewsresponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchnewsresponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}
