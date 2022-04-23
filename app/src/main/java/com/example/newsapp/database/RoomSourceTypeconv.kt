package com.example.newsapp.database

import androidx.room.TypeConverter
import com.example.newsapp.RETRO.Source

class RoomSourceTypeconv {

    @TypeConverter
    fun fromsource(source: Source):String{
        return source.name
    }

    @TypeConverter
    fun tosource(name: String): Source {
      return Source(name,name)
    }

}