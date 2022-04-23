package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ccom.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.MainActivity
import com.example.newsapp.Models.NewsViewmodel


import com.example.newsapp.util.Resource
import com.example.newsapp.adapter.Adapter
import com.example.newsapp.util.Constants
import com.example.newsapp.util.Constants.Companion.TIME_DELAY

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Search_news : Fragment() {

    private var _binding: FragmentSearchNewsBinding?=null
    private val binding get()=_binding!!
    lateinit var newsViewmodel: NewsViewmodel
    lateinit var searchadapter: Adapter
    val TAG="SearchNews"
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentSearchNewsBinding.inflate(inflater,container,false)
              return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setuprecyclerview()

        searchadapter.setOnItemClickListener {
            val action=Search_newsDirections.actionSearchNewsToArticleNews(it)
            findNavController().navigate(action)
        }

        var job:Job?=null
        binding.etSearch.addTextChangedListener {
            job?.cancel()
            job=MainScope().launch {
                delay(TIME_DELAY)
                it?.let {
                    if(it.toString().isNotEmpty()){
                        //prrevnet new articles to addup at pevious ones and show the new search use snippet
                        newsViewmodel.searchnewspage = 1
                        newsViewmodel.searchnewsresponse = null
                        newsViewmodel.searchNews(it.toString())
                    }
                }
            }
        }

    newsViewmodel= (activity as MainActivity).newsViewModel as NewsViewmodel
    newsViewmodel.searchNews.observe(viewLifecycleOwner, Observer { response->
        when(response){
            is Resource.Success->{
                binding.paginationProgressBar.visibility=View.GONE
                isLoading=false
                response.data?.let {
                    searchadapter.differ.submitList(it.articles.toList())
                    val totalPages = it.totalResults / Constants.QUERY_PAGE_SIZE + 2
                    isLastPage = newsViewmodel.searchnewspage == totalPages
                    if(isLastPage==true) {
                    binding.rvRecycleSearchNews.setPadding(0, 0, 0, 0)
                }
                }
            }

            is Resource.Error->{
                binding.paginationProgressBar.visibility=View.INVISIBLE
                isLoading=false
                response.message?.let{
                    Log.e(TAG,"error $it in recieving")
                }
            }
             is Resource.Loading->{
                 binding.paginationProgressBar.visibility=View.VISIBLE
                 isLoading=true
             }
        }
    })
    }
//both the way either like the BREAKING NEW OR THIS//PAGING WASNOT GOOD AS IT TOOK A LOT REQUESTS AND DEPLETED MY APIS
    //THIS MEETHOD HELPS NOT MAKE 1ST ITEM OF NEXT REQ
    // AND HELPS TO CLIP THE LAST ITEM OF LAST PAGE
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                newsViewmodel.searchNews(binding.etSearch.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setuprecyclerview() {
        searchadapter= Adapter()
        val recyclerView=binding.rvRecycleSearchNews
        recyclerView.adapter=searchadapter
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        recyclerView.layoutManager=LinearLayoutManager(activity)
        recyclerView.addOnScrollListener(this.scrollListener)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}