package com.example.aniframe.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aniframe.R
import com.example.aniframe.databinding.ItemKitsuBinding
import com.example.aniframe.data.models.Kitsu

class KitsuAdapter(
    private val onSaveAnime: (Kitsu) -> Unit
) : ListAdapter<Kitsu, KitsuAdapter.ViewHolder>(KitsuItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemKitsuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemKitsuBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(kitsu: Kitsu) {
            with(binding) {
                val age = kitsu.attributes.ageRating
                val poster = kitsu.attributes.posterImage.small
                canonTitle.text = kitsu.attributes.canonicalTitle
                startDate.text = "Start Date: " + kitsu.attributes.startDate
                averageRating.text = kitsu.attributes.averageRating
                ageRating.text = age.toString()
                Glide.with(posterImage.context)
                    .load(poster)
                    .into(posterImage)
                favorite.setOnClickListener {
                    onSaveAnime(kitsu)
                }
            }
        }
    }

    private class KitsuItemCallback : DiffUtil.ItemCallback<Kitsu>() {
        override fun areItemsTheSame(oldItem: Kitsu, newItem: Kitsu): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Kitsu, newItem: Kitsu): Boolean {
            return oldItem == newItem
        }
    }
}
