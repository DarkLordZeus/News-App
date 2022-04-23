package com.androiddevs.mvvmnewsapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsapp.RETRO.Article

@Dao
interface NewsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertnews(article: Article)

    @Query("SELECT * FROM articles ORDER BY id ASC")
    fun readalldata():LiveData<List<Article>>

    @Delete
    suspend fun deletenews(article: Article)

    @Query("SELECT COUNT(*) FROM articles WHERE url =:newurl")
    fun checkurl(newurl:String):LiveData<Long>
}