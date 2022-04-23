package com.example.newsapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import ccom.example.newsapp.databinding.FragmentArticleBinding
import com.example.newsapp.Models.NewsROOMmodel


import com.google.android.material.snackbar.Snackbar


class Article_news : Fragment() {

    private var _binding: FragmentArticleBinding?=null
    private val binding get()=_binding!!
    val args:Article_newsArgs by navArgs()
    val savednewsmodel: NewsROOMmodel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentArticleBinding.inflate(inflater,container,false)

        val article=args.article
        binding.webView.apply {
            webViewClient= WebViewClient()
            article.url?.let { loadUrl(it) }
        }


        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.article.url?.let {
            savednewsmodel.checkurl(it).observe(viewLifecycleOwner, Observer {
                if(it>0L){
                binding.fab.visibility=View.INVISIBLE
            }
            else{
                binding.fab.visibility=View.VISIBLE
            } })
        }


        savednewsmodel.readAllData.observe(viewLifecycleOwner, Observer {
            binding.fab.setOnClickListener {
                savednewsmodel.addNews(args.article)
                Snackbar.make(view,"Article saved successfully",Snackbar.LENGTH_SHORT).show()
            }

        })

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}