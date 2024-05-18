package com.example.aniframe.adapter

import android.R
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.aniframe.data.database.KitsuDB
import com.example.aniframe.data.database.KitsuDao
import com.example.aniframe.databinding.ItemFavoritesBinding
import com.example.aniframe.data.models.Kitsu


class FavoritesAdapter(private val onDeleteAnime: (Kitsu) -> Unit,
                       private val onTagSelected: (Kitsu, String) -> Unit,
                       private val context: Context,
                       private val tagList: List<String>
        ): ListAdapter<Kitsu, FavoritesAdapter.ViewHolder>(KitsuItemCallback()) {

    private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    val dao: KitsuDao
        get() {
            TODO()
        }

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
                val spinner:Spinner = tagTitle
                val poster = kitsu.attributes.posterImage.small
                canonTitle.text = kitsu.attributes.canonicalTitle
                startDate.text = "Start Date: ${kitsu.attributes.startDate}"
                averageRating.text = "Average Rating: ${kitsu.attributes.averageRating}"
                ageRating.text = "Age Rating: ${kitsu.attributes.ageRating}"
                Glide.with(posterImage)
                    .load(poster)
                    .into(posterImage)
                delete.setOnClickListener{
                    onDeleteAnime(kitsu)
                }
                val spinnerAdapter = ArrayAdapter(
                        context,
                        R.layout.simple_spinner_item,
                        tagList
                )

                spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                spinner.adapter = spinnerAdapter

                spinner.setSelection(getSavedTagPosition(kitsu.id))
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val selectedTag = tagList[position]
                        saveSelectedTag(kitsu.id, selectedTag)
                        onTagSelected(kitsu, selectedTag)
                        kitsu.tag = selectedTag
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }

                }
        }
    }
    private fun saveSelectedTag(kitsuId: String, tag: String) {
        sharedPreferences.edit().putString("selected_tag_$kitsuId", tag).apply()
    }

    private fun getSavedTagPosition(kitsuId: String): Int {
        val savedTag = sharedPreferences.getString("selected_tag_$kitsuId", "Planning") ?: "Planning"
        return tagList.indexOf(savedTag)
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