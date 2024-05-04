package com.example.aniframe.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.aniframe.adapter.KitsuAdapter
import com.example.aniframe.adapter.SorulyAdapter
import com.example.aniframe.data.models.SorulySearchResult
import com.example.aniframe.databinding.FragmentSorulySearchResultBinding

class SorulySearchResultFragment : Fragment() {

    private var _binding: FragmentSorulySearchResultBinding? = null
    private val binding
        get() = _binding!!

    private val adapter: SorulyAdapter by lazy {
        SorulyAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSorulySearchResultBinding.inflate(layoutInflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sorulySearchResultList.adapter = adapter

        val results: List<SorulySearchResult>? = arguments?.getParcelableArrayList("searchResult")

        results?.let {
            with(binding) {
                adapter.submitList(results)
            }
        }

    }

    companion object {
        fun newInstance(sorulySearchResult: List<SorulySearchResult>): SorulySearchResultFragment {
            val fragment = SorulySearchResultFragment()
            val args = Bundle().apply {
                putParcelableArrayList("searchResult", ArrayList(sorulySearchResult))
            }
            fragment.arguments = args
            return fragment
        }
    }

}
