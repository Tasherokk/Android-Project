package com.example.aniframe.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aniframe.data.database.*
import com.example.aniframe.database.*
import com.example.aniframe.data.models.Kitsu
import kotlinx.coroutines.*

class FavoritesViewModel(
        private val kitsuDao: KitsuDao
) : ViewModel() {

    class Provider(
            val dao: KitsuDao
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavoritesViewModel(dao) as T
        }
    }

    private val _favoritesListState = MutableLiveData<FavoritesListState>()
    val favoritesListState: LiveData<FavoritesListState> get() = _favoritesListState

    private val coroutineExceptionHandler =
            CoroutineExceptionHandler { coroutineContext, throwable ->
                _favoritesListState.postValue(FavoritesListState.Error(throwable.message))
            }

    fun fetchKitsuListDB() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = async { kitsuDao.getAll() }
            val kitsuList = response.await()
            withContext(Dispatchers.Main) {
                _favoritesListState.setValue(
                        FavoritesListState.Success(
                                kitsuList.map {
                                    KitsuDbMapper(it)
                                }
                        )
                )
            }
        }
    }
    fun deleteAnime(kitsu: Kitsu){
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val posterImage = kitsu.attributes.posterImage
            val attributes = AttributesDB(
                    createdAt = kitsu.attributes.createdAt,
                    canonicalTitle = kitsu.attributes.canonicalTitle,
                    averageRating = kitsu.attributes.averageRating,
                    ageRating = kitsu.attributes.ageRating.name,
                    posterImage = PosterImageDB(
                            tiny = posterImage.tiny,
                            small = posterImage.small,
                            large = posterImage.large,
                            original = posterImage.original
                    )
            )
            val kitsuDB = KitsuDB(
                    id = kitsu.id,
                    type = kitsu.type,
                    attributes = attributes
            )
            kitsuDao.delete(kitsuDB)
            _favoritesListState.postValue(FavoritesListState.SuccessAnimeSave)
        }
    }
}
sealed class FavoritesListState {
    data class Success(val items: List<Kitsu>) : FavoritesListState()
    data object SuccessAnimeSave: FavoritesListState()
    data class Error(val message: String? = null) : FavoritesListState()
}