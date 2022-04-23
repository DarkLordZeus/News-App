package com.example.newsapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ccom.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.Models.NewsROOMmodel
import com.example.newsapp.adapter.Adapter


import com.google.android.material.snackbar.Snackbar


class Saved_news : Fragment() {


    private var _binding: FragmentSavedNewsBinding?=null
    private val binding get()=_binding!!
    val savednewsmodel: NewsROOMmodel by viewModels()
    lateinit var savednewsadapter: Adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentSavedNewsBinding.inflate(inflater,container,false)

                return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setuprecyclerview()
        savednewsmodel.readAllData.observe(viewLifecycleOwner, Observer { list->
            savednewsadapter.differ.submitList(list)
        })
        //ITEM DELETION BY SWIPE
        val ItemTouchhelpercallback= object:ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val article=savednewsadapter.differ.currentList[viewHolder.adapterPosition]
                savednewsmodel.deleteNews(article)
                Snackbar.make(view,"Article removed!",Snackbar.LENGTH_SHORT)
                    .setAction("UNDO",View.OnClickListener { savednewsmodel.addNews(article)
                    Snackbar.make(view,"Article Restored",Snackbar.LENGTH_SHORT).show()
                    }).show()
            }

        }
        ItemTouchHelper(ItemTouchhelpercallback).attachToRecyclerView(binding.rvSavedNews)

        savednewsadapter.setOnItemClickListener {
            val action=Saved_newsDirections.actionSavedNewsToArticleNews(it)
            findNavController().navigate(action)
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
    private fun setuprecyclerview() {
        savednewsadapter= Adapter()
        val recyclerView=binding.rvSavedNews
        recyclerView.adapter=savednewsadapter
        recyclerView.layoutManager= LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL)
        )
        //recyclerView.layoutManager=LinearLayoutManager(activity)

    }

}