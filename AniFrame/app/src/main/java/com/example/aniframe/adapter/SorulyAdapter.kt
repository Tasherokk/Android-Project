package com.example.aniframe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aniframe.databinding.ItemKitsuBinding
import com.example.aniframe.databinding.ItemSorulyBinding
import com.example.aniframe.data.models.Kitsu
import com.example.aniframe.data.models.SorulySearchResult

class SorulyAdapter: ListAdapter<SorulySearchResult, SorulyAdapter.ViewHolder>(SorulyItemCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSorulyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(
        private val binding: ItemSorulyBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(soruly: SorulySearchResult){
            with(binding){
                fileNameTextView.text = soruly.filename
                similarityTextView.text = "Similarity: ${soruly.similarity}"
                anilistTextView.text = "AnilistID: ${soruly.anilist}"
                Glide.with(image)
                    .load(soruly.image)
                    .into(image)
            }
        }
    }

    private class SorulyItemCallback: DiffUtil.ItemCallback<SorulySearchResult>(){
        override fun areItemsTheSame(oldItem: SorulySearchResult, newItem: SorulySearchResult): Boolean {
            return oldItem.anilist == newItem.anilist
        }

        override fun areContentsTheSame(oldItem: SorulySearchResult, newItem: SorulySearchResult): Boolean {
            return oldItem == newItem
        }

    }
}