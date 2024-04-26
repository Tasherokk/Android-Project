package com.example.aniframe.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aniframe.network.SorulySearchResult
import com.example.aniframe.databinding.FragmentSorulySearchResultBinding

class SorulySearchResultFragment : Fragment() {

    private var _binding: FragmentSorulySearchResultBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSorulySearchResultBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve search result from arguments
        val result: SorulySearchResult? = arguments?.getParcelable("searchResult")


        // Display search result in respective TextViews or other UI elements
        result?.let {
            with(binding) {
                fileNameTextView.text = it.filename
                similarityTextView.text = "Similarity: ${it.similarity}"
                anilistTextView.text = "AnilistID: ${it.anilist}"
                Glide.with(image)
                    .load(it.image)
                    .into(image)
            }
        }

    }

    companion object {
        fun newInstance(sorulySearchResult: SorulySearchResult): SorulySearchResultFragment {
            val fragment = SorulySearchResultFragment()
            val args = Bundle().apply {
                putParcelable("searchResult", sorulySearchResult)
            }
            fragment.arguments = args
            return fragment
        }
    }

}
