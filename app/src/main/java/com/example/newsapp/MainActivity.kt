package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import ccom.example.newsapp.R
import ccom.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.Models.NewsViewmodel
import com.example.newsapp.Models.NewsViewmodelProviderfactory
import com.example.newsapp.database.NewsDatabase


import com.example.newsapp.repository.NewsRepository


class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController
    private lateinit var AppBarConfiguration : AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    lateinit var newsViewModel: ViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        setContentView(binding.root)
        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController=navHostFragment.navController

        val repository= NewsRepository(NewsDatabase.getDatabase(this))
        val viewmodelProviderfactory= NewsViewmodelProviderfactory(repository)
        newsViewModel=ViewModelProvider(this,viewmodelProviderfactory).get(NewsViewmodel::class.java)



        binding.bottomNavigationView.setupWithNavController(navController)


    }


    override fun onSupportNavigateUp() : Boolean
    {
        return navController.navigateUp()||super.onSupportNavigateUp()
    }


    /*override fun onCreateOptionsMenu(menu: Menu?):Boolean
    {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }*/

    override fun onOptionsItemSelected(item: MenuItem):Boolean
    {

        return item.onNavDestinationSelected(navController)||return super.onOptionsItemSelected(item)
    }




}
