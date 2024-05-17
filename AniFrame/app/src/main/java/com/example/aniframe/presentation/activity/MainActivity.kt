package com.example.aniframe.presentation.activity

import com.example.aniframe.presentation.fragments.SorulySearchFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.aniframe.R
import com.example.aniframe.data.database.AuthManager
import com.example.aniframe.databinding.ActivityMainBinding
import com.example.aniframe.presentation.fragments.FavoritesFragment
import com.example.aniframe.presentation.fragments.KitsuListFragment
import com.example.aniframe.presentation.fragments.LoginFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var authManager: AuthManager
    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(KitsuListFragment())
        authManager = AuthManager(this)

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
                    if (isUserLoggedIn()) {
                        if (currentFragment !is FavoritesFragment) {
                            replaceFragment(FavoritesFragment())
                        }
                    } else {
                        if (currentFragment !is LoginFragment) {
                            replaceFragment(LoginFragment())
                        }
                    }
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
    private fun isUserLoggedIn(): Boolean {
        val authToken = authManager.getAuthToken()
        return !authToken.isNullOrEmpty()
    }


}