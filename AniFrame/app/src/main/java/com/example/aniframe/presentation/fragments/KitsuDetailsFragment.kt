package com.example.aniframe.presentation.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.aniframe.R
import com.example.aniframe.adapter.KitsuAdapter
import com.example.aniframe.data.database.KitsuDatabase
import com.example.aniframe.data.network.KitsuApiClient
import com.example.aniframe.databinding.FragmentKitsuDetailsBinding
import com.example.aniframe.databinding.FragmentKitsuListBinding
import com.example.aniframe.presentation.viewmodel.KitsuDetailsState
import com.example.aniframe.presentation.viewmodel.KitsuDetailsViewModel
import com.example.aniframe.presentation.viewmodel.KitsuListViewModel

class KitsuDetailsFragment : Fragment() {
    private val viewModel: KitsuDetailsViewModel by lazy {
        ViewModelProvider(
            this,
            KitsuDetailsViewModel.Provider(KitsuApiClient.instance)
        )[KitsuDetailsViewModel::class.java]

    }

    private var _binding: FragmentKitsuDetailsBinding? = null
    private val binding
        get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKitsuDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animeId = getAnimeIdFromArgs()

        // Использование animeId, например, для получения деталей или отображения информации
        if (animeId != null) {
            configureObservers()
            viewModel.fetchKitsuDetails(animeId)
        } else {
            Log.d("Didn't get", "Looser")
        }

    }

    private fun getAnimeIdFromArgs(): Int? {
        val arguments = arguments
        return arguments?.getInt("kitsuId")
    }


    private fun configureObservers() {
        viewModel.kitsuDetailsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is KitsuDetailsState.Error -> {
                    Log.d("MyApp", "no results")
                }

                is KitsuDetailsState.Loading -> {
                    with(binding) {
                        if (state.isLoading) {
                            contentLayout.isVisible = false

                        } else {
                            contentLayout.isVisible = true
                        }
                    }
                }

                is KitsuDetailsState.Success -> {

                    val kitsu = state.item
                    Log.d("MyApp", "${kitsu.attributes}")
                    with(binding) {
                        val poster = kitsu.attributes.posterImage.small
                        canonicalTitle.text = kitsu.attributes.canonicalTitle
                        startDate.text = "Start Date: ${kitsu.attributes.startDate}"
                        averageRating.text = "Rating: ${kitsu.attributes.averageRating}"
                        ageRating.text = "Age Rating: ${kitsu.attributes.ageRating}"
                        description.text = kitsu.attributes.description
                        Glide.with(posterImage.context)
                            .load(poster)
                            .into(posterImage)

                    }
                }
            }
        }
    }
}