package com.example.aniframe

import com.example.aniframe.fragments.SorulySearchFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.aniframe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.fragment_container_view, SorulySearchFragment())
            .commit()
    }
}