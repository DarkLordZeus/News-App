package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ccom.example.newsapp.databinding.ItemArticlePreviewBinding
import com.example.newsapp.RETRO.Article

import com.bumptech.glide.Glide




class Adapter(): RecyclerView.Adapter<Adapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemArticlePreviewBinding)
        :RecyclerView.ViewHolder(binding.root){

        }

    companion object DiffCallback: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
           return oldItem==newItem
        }

    }

    val differ=AsyncListDiffer(this, DiffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemArticlePreviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article=differ.currentList[position]
        holder.binding.apply {
            Glide.with(holder.itemView.context).load(article.urlToImage).into(ivArticleImage)
            tvTitle.text=article.title
            tvDescription.text=article.description
            tvSource.text=article.source?.name
            tvPublishedAt.text=article.publishedAt}

            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(article)
                }
            }


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
//here  it help to get article when we click the item......
     private var onItemClickListener:((Article)->Unit)?=null

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener = listener
    }
    }

