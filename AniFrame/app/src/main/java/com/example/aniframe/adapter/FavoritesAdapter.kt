package com.example.aniframe.adapter

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aniframe.databinding.ItemFavoritesBinding
import com.example.aniframe.data.models.Kitsu


class FavoritesAdapter(private val onDeleteAnime: (Kitsu) -> Unit,
                       private val context: Context,
        ): ListAdapter<Kitsu, FavoritesAdapter.ViewHolder>(KitsuItemCallback()) {

    private var tagList: List<String> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                ItemFavoritesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(
        private val binding: ItemFavoritesBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind(kitsu: Kitsu){
            with(binding){
                val age = kitsu.attributes.ageRating
                val poster = kitsu.attributes.posterImage.small
                val spinner:Spinner = tagTitle
                canonTitle.text = kitsu.attributes.canonicalTitle
                startDate.text = "Start Date: " + kitsu.attributes.startDate
                averageRating.text = kitsu.attributes.averageRating
                ageRating.text = age.toString()
                Glide.with(posterImage)
                    .load(poster)
                    .into(posterImage)
                delete.setOnClickListener{
                    onDeleteAnime(kitsu)
                }

                tagTitle.adapter = ArrayAdapter(context, R.layout.simple_spinner_item, tagList)
            }
        }
    }
    fun setTags(tags: List<String>){
        tagList = tags
        notifyDataSetChanged()
    }

    private class KitsuItemCallback: DiffUtil.ItemCallback<Kitsu>(){
        override fun areItemsTheSame(oldItem: Kitsu, newItem: Kitsu): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Kitsu, newItem: Kitsu): Boolean {
            return oldItem == newItem
        }

    }
}