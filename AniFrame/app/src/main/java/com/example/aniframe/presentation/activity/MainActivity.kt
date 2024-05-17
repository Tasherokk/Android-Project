package com.example.aniframe.presentation.activity

import com.example.aniframe.presentation.fragments.SorulySearchFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.aniframe.R
import com.example.aniframe.databinding.ActivityMainBinding
import com.example.aniframe.presentation.fragments.FavoritesFragment
import com.example.aniframe.presentation.fragments.KitsuListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
        replaceFragment(KitsuListFragment())

        Log.d("MyApp", "This is a debug message");

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.kitsu -> {

                    replaceFragment(KitsuListFragment())
//                    navController.navigate(R.id.kitsuListFragment)
                    true
                }
                R.id.soruly -> {
                    replaceFragment(SorulySearchFragment())
//                    navController.navigate(R.id.sorulySearchFragment)
                    true
                }
                R.id.favorite -> {
                    replaceFragment(FavoritesFragment())
//                    navController.navigate(R.id.favoritesFragment)
                    true
                }
                else -> true
            }
        }

    }


    private fun replaceFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }



}