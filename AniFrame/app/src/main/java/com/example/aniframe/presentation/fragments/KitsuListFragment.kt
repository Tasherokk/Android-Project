package com.example.aniframe.presentation.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.aniframe.R
import com.example.aniframe.adapter.KitsuAdapter
import com.example.aniframe.data.database.KitsuDatabase
import com.example.aniframe.data.models.Kitsu
import com.example.aniframe.data.network.KitsuApiClient
import com.example.aniframe.databinding.FragmentKitsuListBinding
import com.example.aniframe.presentation.viewmodel.KitsuListState
import com.example.aniframe.presentation.viewmodel.KitsuListViewModel


class KitsuListFragment : Fragment() {
    private val viewModel: KitsuListViewModel by lazy {
        ViewModelProvider(
            this,
            KitsuListViewModel.Provider(
                service = KitsuApiClient.instance,
                dao = Room.databaseBuilder(
                    requireContext().applicationContext,
                    KitsuDatabase::class.java, "kitsudb"
                ).build().kitsuDao()
            )
        ).get<KitsuListViewModel>(KitsuListViewModel::class.java)
    }
    private lateinit var navController: NavController

    private var _binding: FragmentKitsuListBinding? = null
    private val binding
        get() = _binding!!

    private var adapter: KitsuAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKitsuListBinding.inflate(layoutInflater, container, false)
//        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        return _binding?.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = KitsuAdapter(
            onSaveAnime = {
                viewModel.saveAnime(it)
            },
            onDetailsClick = {
                handleOnDetailClick(it)
            }
//
        )
        binding.kitsuList.adapter = adapter

        observeViewModel()
        viewModel.fetchKitsuList()

        binding.searchButton.setOnClickListener {
            val searchQuery = binding.searchEditText.text.toString()
            if (searchQuery != viewModel.currentSearchQuery) {
                viewModel.currentSearchQuery = searchQuery
                if (searchQuery.isEmpty()) {
                    viewModel.fetchKitsuList()
                } else {
                    viewModel.fetchKitsuByName(searchQuery)
                }
            }
        }
        binding.allButton.setOnClickListener {
            viewModel.fetchKitsuList()
        }
        binding.trending.setOnClickListener {
            viewModel.fetchKitsuTrending()
        }
        binding.sortRating.setOnClickListener {
            viewModel.sortBy("averageRating")
        }
        viewModel.isLoadingMoreItems.observe(viewLifecycleOwner) { isLoading ->
            adapter?.setLoading(isLoading)
        }
        binding.kitsuList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    viewModel.loadMoreItems()
                }
            }

        }

        )

    }

    private fun handleOnDetailClick(kitsu: Kitsu) {

        val detailsFragment = KitsuDetailsFragment()

        // Создание Bundle и передача данных
        val bundle = Bundle().apply {
            putInt("kitsuId", kitsu.id.toInt())  // Предполагается, что у объекта Kitsu есть поле id
        }
        detailsFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, detailsFragment)
            .addToBackStack(null)
            .commit()


    }

    private fun observeViewModel() {
        viewModel.kitsuListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is KitsuListState.Loading -> {
                    // Show loading state if needed
                }

                is KitsuListState.Success -> {
                    adapter?.submitList(state.items)
                }

                is KitsuListState.Error -> {
                    AlertDialog.Builder(requireContext())
                        .setTitle(R.string.error_title)
                        .setMessage(state.message ?: getString(R.string.error_message))
                        .show()
                }

                is KitsuListState.SuccessAnimeSave -> Toast.makeText(
                    requireContext(),
                    "Success",
                    Toast.LENGTH_SHORT
                ).show()

                else -> {}
            }
        }
    }
}

