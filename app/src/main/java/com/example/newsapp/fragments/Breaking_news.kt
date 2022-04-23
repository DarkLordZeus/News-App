package com.example.newsapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ccom.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.MainActivity
import com.example.newsapp.Models.NewsViewmodel
import com.example.newsapp.adapter.Adapter

import com.example.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE

import com.example.newsapp.util.Resource



class  Breaking_news : Fragment() {
    private var _binding: FragmentBreakingNewsBinding? = null
    private val binding get() = _binding!!
    lateinit var newsViewmodel: NewsViewmodel
    lateinit var newsadapter: Adapter
    val TAG = "BREAKINGNEWSFRAGMENT"
    var isLoading = false
    var isLastPage = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewmodel= (activity as MainActivity).newsViewModel as NewsViewmodel
        setuprecyclerview()

        newsadapter.setOnItemClickListener {
            val action=Breaking_newsDirections.actionBreakingNewsToArticleNews(it)
            findNavController().navigate(action)
        }

        //RESOURCE generation succesfull or not
        newsViewmodel.breakingNews.observe(viewLifecycleOwner, Observer { response->
            when(response){
                is Resource.Success -> {

                    binding.paginationProgressBar.visibility=View.INVISIBLE
                    isLoading=false
                    response.data?.let { newsResponse ->
                        newsadapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = newsViewmodel.breakingnewspage == totalPages
                        if(isLastPage==true) {
                            binding.rvBreakingNews.setPadding(0, 0, 0, 0)
                        }

                    }
                }
                is Resource.Error ->{

                    binding.paginationProgressBar.visibility=View.INVISIBLE
                    isLoading=false
                    response.message?.let { message->
                        Log.e(TAG,"ERROR OCCURED $message")
                    }
                }

                is Resource.Loading ->{

                    binding.paginationProgressBar.visibility=View.VISIBLE
                    isLoading=true
                }

            }

        })

    }
    val scrollListener = object : RecyclerView.OnScrollListener(){

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (recyclerView.canScrollVertically(-1) &&
                // - 1 means scroll up
                !recyclerView.canScrollVertically(1) &&
                // 1 means scroll down
                newState == RecyclerView.SCROLL_STATE_IDLE
            ) {
                onBottomReached()
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun setuprecyclerview() {
        newsadapter= Adapter()
        val recyclerView=binding.rvBreakingNews
        recyclerView.adapter=newsadapter
        recyclerView.layoutManager=LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL)
        )
        recyclerView.layoutManager=LinearLayoutManager(activity)
        recyclerView.addOnScrollListener(scrollListener)
    }

    private fun onBottomReached(){
        if (!isLoading && !isLastPage) {
            newsViewmodel.getBreakingNews("in")

        }
    }

}