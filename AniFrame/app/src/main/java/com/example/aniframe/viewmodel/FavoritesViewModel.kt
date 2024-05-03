package com.example.aniframe.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aniframe.database.KitsuDao
import com.example.aniframe.database.KitsuDbMapper
import com.example.aniframe.models.Kitsu
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
}
sealed class FavoritesListState {
    data class Success(val items: List<Kitsu>) : FavoritesListState()
    data class Error(val message: String? = null) : FavoritesListState()
}