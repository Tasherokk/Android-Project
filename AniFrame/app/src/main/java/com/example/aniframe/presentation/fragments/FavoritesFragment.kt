package com.example.aniframe.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider

import androidx.room.Room
import com.example.aniframe.R

import com.example.aniframe.adapter.FavoritesAdapter
import com.example.aniframe.data.database.KitsuDatabase
import com.example.aniframe.data.models.Kitsu
import com.example.aniframe.databinding.FragmentFavoritesListBinding
import com.example.aniframe.presentation.viewmodel.FavoritesListState
import com.example.aniframe.presentation.viewmodel.FavoritesViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


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
        adapter = FavoritesAdapter(
                onDeleteAnime = {
                    viewModel.deleteAnime(it)
                    setupUI()
                },
                onTagSelected = { kitsu, newTag ->
                    viewModel.updateAnime(kitsu, newTag)
                },
                requireContext()
        )
        setupUI()
        AllObserve()
        with(binding){
            allButton.setOnClickListener{
                viewModel.fetchKitsuListDB()
            }
            planning.setOnClickListener{
                viewModel.filterItemsByTag("Planning")

            }
            watching.setOnClickListener{
                viewModel.filterItemsByTag("Watching")

            }
            completed.setOnClickListener{
                viewModel.filterItemsByTag("Completed")

            }
        }

    }
    private fun setupUI() {
        with(binding) {
            kitsuList.adapter = adapter
            viewModel.fetchKitsuListDB()
        }
    }
    private fun AllObserve(){
        viewModel.favoritesListState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoritesListState.Success -> adapter?.submitList(state.items)
                is FavoritesListState.Error -> {
                }
                is FavoritesListState.Loading -> {
                }

                else -> {}
            }
        }
    }
}


