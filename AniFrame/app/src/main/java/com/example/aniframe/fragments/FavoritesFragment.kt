package com.example.aniframe.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider

import androidx.room.Room

import com.example.aniframe.adapter.FavoritesAdapter
import com.example.aniframe.database.KitsuDatabase
import com.example.aniframe.databinding.FragmentFavoritesListBinding
import com.example.aniframe.viewmodel.FavoritesListState
import com.example.aniframe.viewmodel.FavoritesViewModel


class FavoritesFragment : Fragment() {
    private val viewModel: FavoritesViewModel by lazy {
        ViewModelProvider(
                this,
                FavoritesViewModel.Provider(
                        dao = Room.databaseBuilder(
                                requireContext().applicationContext,
                                KitsuDatabase::class.java, "kitsudb"
                        ).build().kitsuDao()
                )
        ).get<FavoritesViewModel>(FavoritesViewModel::class.java)
    }

    private var _binding: FragmentFavoritesListBinding? = null
    private val binding
        get() = _binding!!

    private var adapter: FavoritesAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesListBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FavoritesAdapter()
        binding.kitsuList.adapter = adapter

        viewModel.fetchKitsuListDB()

        viewModel.favoritesListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesListState.Success -> adapter?.submitList(state.items)

                else -> {}
            }
        }

    }

//    private fun sortBy(attr: String){
//        val client = KitsuApiClient.instance
//        val response = client.sortBy( "-"+ attr)
//        response.enqueue(object : Callback<KitsuApiResponse> {
//            override fun onResponse(call: Call<KitsuApiResponse>, response: Response<KitsuApiResponse>) {
//
//                response.body()?.let {
//                    adapter?.submitList(it.data)
//                }
//            }
//            override fun onFailure(call: Call<KitsuApiResponse>, t: Throwable) {
//                println("${t.message}")
//            }
//        })
//    }
//    private fun fetchKitsuTrending(){
//        val client = KitsuApiClient.instance
//        val response = client.fetchKitsuTrendingList()
//        response.enqueue(object : Callback<KitsuApiResponse> {
//            override fun onResponse(call: Call<KitsuApiResponse>, response: Response<KitsuApiResponse>) {
//
//                response.body()?.let {
//                    adapter?.submitList(it.data)
//                }
//            }
//            override fun onFailure(call: Call<KitsuApiResponse>, t: Throwable) {
//                println("${t.message}")
//            }
//        })
//    }
//    private fun fetchKitsuByName(name: String) {
//        val client = KitsuApiClient.instance
//        val response = client.fetchKitsuListByName(name)
//        response.enqueue(object : Callback<KitsuApiResponse> {
//            override fun onResponse(call: Call<KitsuApiResponse>, response: Response<KitsuApiResponse>) {
//                response.body()?.let {
//                    adapter?.submitList(it.data)
//                }
//
//            }
//
//            override fun onFailure(call: Call<KitsuApiResponse>, t: Throwable) {
//                println("${t.message}")
//            }
//        })
//    }
//    private fun fetchKitsuList() {
//        val client = KitsuApiClient.instance
//        val response = client.fetchKitsuList(10, 0)
//        response.enqueue(object : Callback<KitsuApiResponse> {
//            override fun onResponse(call: Call<KitsuApiResponse>, response: Response<KitsuApiResponse>) {
//                response.body()?.let {
//                    adapter?.submitList(it.data)
//                    original = it.data
//                }
//
//            }
//
//            override fun onFailure(call: Call<KitsuApiResponse>, t: Throwable) {
//                println("${t.message}")
//            }
//        })
//    }

}


