package com.example.aniframe.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.aniframe.R
import com.example.aniframe.adapter.KitsuAdapter
import com.example.aniframe.databinding.FragmentKitsuListBinding
import com.example.aniframe.models.Kitsu
import com.example.aniframe.network.KitsuApiClient
import com.example.aniframe.network.KitsuApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class KitsuListFragment : Fragment() {

    private var currentSearchQuery: String = ""
    private var original: List<Kitsu> = ArrayList()
    private var _binding: FragmentKitsuListBinding? = null
    private val binding
        get() = _binding!!

    private val adapter: KitsuAdapter by lazy {
        KitsuAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKitsuListBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchKitsuList()
        binding.editText.addTextChangedListener {
            val searchQuery = it.toString()
            if (searchQuery != currentSearchQuery) {
                currentSearchQuery = searchQuery
                if (searchQuery.isEmpty()) {
                    adapter.submitList(original)
                } else {
                    fetchKitsuByName(searchQuery)
                }
            }
        }
        binding.allButton.setOnClickListener{
            fetchKitsuList()
        }
        binding.trending.setOnClickListener {
            fetchKitsuTrending()
        }
        binding.sortRating.setOnClickListener {
            sortBy("averageRating")
        }

        setupUI()

    }

    private fun sortBy(attr: String){
        val client = KitsuApiClient.instance
        val response = client.sortBy( "-"+ attr)
        response.enqueue(object : Callback<KitsuApiResponse> {
            override fun onResponse(call: Call<KitsuApiResponse>, response: Response<KitsuApiResponse>) {

                response.body()?.let {
                    adapter.submitList(it.data)
                }
            }
            override fun onFailure(call: Call<KitsuApiResponse>, t: Throwable) {
                println("${t.message}")
            }
        })
    }
    private fun fetchKitsuTrending(){
        val client = KitsuApiClient.instance
        val response = client.fetchKitsuTrendingList()
        response.enqueue(object : Callback<KitsuApiResponse> {
            override fun onResponse(call: Call<KitsuApiResponse>, response: Response<KitsuApiResponse>) {

                response.body()?.let {
                    adapter.submitList(it.data)
                }
            }
            override fun onFailure(call: Call<KitsuApiResponse>, t: Throwable) {
                println("${t.message}")
            }
        })
    }
    private fun fetchKitsuByName(name: String) {
        val client = KitsuApiClient.instance
        val response = client.fetchKitsuListByName(name)
        response.enqueue(object : Callback<KitsuApiResponse> {
            override fun onResponse(call: Call<KitsuApiResponse>, response: Response<KitsuApiResponse>) {
                response.body()?.let {
                    adapter.submitList(it.data)
                }

            }

            override fun onFailure(call: Call<KitsuApiResponse>, t: Throwable) {
                println("${t.message}")
            }
        })
    }
    private fun fetchKitsuList() {
        val client = KitsuApiClient.instance
        val response = client.fetchKitsuList(10, 0)
        response.enqueue(object : Callback<KitsuApiResponse> {
            override fun onResponse(call: Call<KitsuApiResponse>, response: Response<KitsuApiResponse>) {
                response.body()?.let {
                    adapter.submitList(it.data)
                    original = it.data

                }

            }

            override fun onFailure(call: Call<KitsuApiResponse>, t: Throwable) {
                println("${t.message}")
            }
        })
    }

    private fun setupUI() {
        with(binding) {
            kitsuList.adapter = adapter

        }
    }
}


