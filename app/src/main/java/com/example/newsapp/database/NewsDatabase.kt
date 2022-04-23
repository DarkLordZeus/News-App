package com.example.newsapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsapp.RETRO.Article
import com.androiddevs.mvvmnewsapp.database.NewsDAO

@Database(entities=[Article::class],version=1,exportSchema=false)

@TypeConverters(RoomSourceTypeconv::class)
abstract class NewsDatabase: RoomDatabase(){

    abstract fun newsDao(): NewsDAO

    companion object{
        @Volatile
         var INSTANCE: NewsDatabase?=null

        fun getDatabase(context: Context): NewsDatabase {
            val tempInstance= INSTANCE
                    if(tempInstance!=null){
                        return tempInstance
                    }
            synchronized(this){
                val instance= Room.databaseBuilder(
                        context.applicationContext,
                NewsDatabase::class.java,
                "user_database"
                ).build()
                INSTANCE =instance
                return instance

            }
        }
    }

}
