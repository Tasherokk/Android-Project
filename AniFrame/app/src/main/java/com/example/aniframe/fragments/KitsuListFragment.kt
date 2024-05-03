package com.example.aniframe.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.aniframe.adapter.KitsuAdapter
import com.example.aniframe.databinding.FragmentKitsuListBinding
import com.example.aniframe.models.Kitsu
import com.example.aniframe.network.KitsuApiClient
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.aniframe.R
import com.example.aniframe.database.KitsuDatabase
import com.example.aniframe.viewmodel.KitsuListState
import com.example.aniframe.viewmodel.KitsuListViewModel


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

    private var currentSearchQuery: String = ""
    private var original: List<Kitsu> = ArrayList()
    private var _binding: FragmentKitsuListBinding? = null
    private val binding
        get() = _binding!!

    private var adapter: KitsuAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKitsuListBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = KitsuAdapter(
                onSaveAnime = {
                    viewModel.saveAnime(it)
                }
        )
        binding.kitsuList.adapter = adapter

        viewModel.fetchKitsuList()

        viewModel.kitsuListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is KitsuListState.Success -> adapter?.submitList(state.items)

                is KitsuListState.Error -> {
                    AlertDialog.Builder(requireContext())
                            .setTitle(R.string.error_title)
                            .setMessage(state.message ?: getString(R.string.error_message))
                            .show()
                }
                is KitsuListState.SuccessAnimeSave -> Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                else -> {}
            }
        }
        binding.editText.addTextChangedListener {
            val searchQuery = it.toString()
            if (searchQuery != currentSearchQuery) {
                currentSearchQuery = searchQuery
                if (searchQuery.isEmpty()) {
                    adapter?.submitList(original)
                } else {
                    viewModel.fetchKitsuByName(searchQuery)
                }
            }
        }
        binding.allButton.setOnClickListener{
            viewModel.fetchKitsuList()
        }
        binding.trending.setOnClickListener {
            viewModel.fetchKitsuTrending()
        }
        binding.sortRating.setOnClickListener {
            viewModel.sortBy("averageRating")
        }


    }

}


