package com.example.aniframe

import com.example.aniframe.fragments.SorulySearchFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.aniframe.databinding.ActivityMainBinding
import com.example.aniframe.fragments.FavoritesFragment
import com.example.aniframe.fragments.KitsuListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(KitsuListFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.kitsu -> {
                    replaceFragment(KitsuListFragment())
                    true
                }
                R.id.soruly -> {
                    replaceFragment(SorulySearchFragment())
                    true
                }
                R.id.favorite -> {
                    replaceFragment(FavoritesFragment())
                    true
                }
                else -> true
            }
        }

    }


    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }


}