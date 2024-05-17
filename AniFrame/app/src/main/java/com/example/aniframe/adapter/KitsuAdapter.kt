package com.example.aniframe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aniframe.R
import com.example.aniframe.databinding.ItemKitsuBinding
import com.example.aniframe.data.models.Kitsu

class KitsuAdapter(
    private val onSaveAnime: (Kitsu) -> Unit,
    private val onDetailsClick: (Kitsu) -> Unit
) : ListAdapter<Kitsu, RecyclerView.ViewHolder>(KitsuItemCallback()) {

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    private var isLoading = false

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isLoading) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            ViewHolder(
                ItemKitsuBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent, false
                )
            )
        } else {
            LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (isLoading) 1 else 0
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        if (isLoading) {
            notifyItemInserted(itemCount - 1)
        } else {
            notifyItemRemoved(itemCount)
        }
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
                details.setOnClickListener {
                    onDetailsClick(kitsu)
                }

            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

private class KitsuItemCallback : DiffUtil.ItemCallback<Kitsu>() {
    override fun areItemsTheSame(oldItem: Kitsu, newItem: Kitsu): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Kitsu, newItem: Kitsu): Boolean {
        return oldItem == newItem
    }
}
