package com.example.aniframe.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aniframe.data.database.AttributesDB
import com.example.aniframe.data.database.KitsuDB
import com.example.aniframe.data.database.KitsuDao
import com.example.aniframe.data.database.PosterImageDB
import com.example.aniframe.data.models.Kitsu
import com.example.aniframe.data.network.KitsuApiResponse
import com.example.aniframe.data.network.KitsuService
import kotlinx.coroutines.*
class KitsuListViewModel(
    private val service: KitsuService,
    private val kitsuDao: KitsuDao
) : ViewModel() {

    class Provider(
        val service: KitsuService,
        val dao: KitsuDao
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return KitsuListViewModel(service, dao) as T
        }
    }

    var currentSearchQuery: String? = null
    private var currentOffset = 0
    private val itemsPerPage = 10
    private val _loadedItems = mutableListOf<Kitsu>()
    private var isLoading = false
    private val _kitsuListState = MutableLiveData<KitsuListState>()
    val kitsuListState: LiveData<KitsuListState> get() = _kitsuListState

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _kitsuListState.postValue(KitsuListState.Error(exception.message ?: "Unknown error"))

    }

    fun fetchKitsuList() {
        _kitsuListState.value = KitsuListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = service.fetchKitsuList(10, 0)
            withContext(Dispatchers.Main) {
                val kitsuList = response.data.map { KitsuApi.toKitsu(it) }
                _loadedItems.clear()
                _loadedItems.addAll(kitsuList)
                _kitsuListState.value = KitsuListState.Success(_loadedItems)
                _kitsuListState.value = KitsuListState.Loading(false)
            }
        }
    }

    fun loadMoreItems() {

        if (isLoading) return

        isLoading = true
        _kitsuListState.value = KitsuListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            try {
                val response = service.fetchKitsuList(itemsPerPage, currentOffset).data
                val kitsuList = response.map { KitsuApi.toKitsu(it) }
                currentOffset += itemsPerPage

                _loadedItems.addAll(kitsuList)

                withContext(Dispatchers.Main) {
                    _kitsuListState.value = KitsuListState.Success(_loadedItems)
                    _kitsuListState.postValue(KitsuListState.Loading(false))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _kitsuListState.value = KitsuListState.Error(e.message)
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun sortBy(attr: String) {
        _kitsuListState.value = KitsuListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = async { service.sortBy("-" + attr) }
            val kitsuList = response.await().data
            withContext(Dispatchers.Main) {
                _kitsuListState.value = KitsuListState.Success(kitsuList.map { KitsuApi.toKitsu(it) })
                _kitsuListState.postValue(KitsuListState.Loading(false))
            }
        }
    }

    fun fetchKitsuTrending() {
        _kitsuListState.value = KitsuListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = async { service.fetchKitsuTrendingList() }
            val kitsuList = response.await().data
            withContext(Dispatchers.Main) {
                _kitsuListState.value = KitsuListState.Success(kitsuList.map { KitsuApi.toKitsu(it) })
                _kitsuListState.postValue(KitsuListState.Loading(false))
            }
        }
    }

    fun fetchKitsuByName(name: String) {
        _kitsuListState.value = KitsuListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = async { service.fetchKitsuListByName(name) }
            withContext(Dispatchers.Main) {
                _loadedItems.clear()
                val kitsuList = response.await().data.map { KitsuApi.toKitsu(it) }
                _loadedItems.addAll(kitsuList)
                _kitsuListState.value = KitsuListState.Success(_loadedItems)
                _kitsuListState.value = KitsuListState.Loading(false)
            }
        }
    }

    fun saveAnime(kitsu: Kitsu){
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val posterImage = kitsu.attributes.posterImage
            val attributes = AttributesDB(
                startDate = kitsu.attributes.startDate,
                canonicalTitle = kitsu.attributes.canonicalTitle,
                averageRating = kitsu.attributes.averageRating,
                ageRating = kitsu.attributes.ageRating,
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
            kitsuDao.insert(kitsuDB)
            _kitsuListState.postValue(KitsuListState.SuccessAnimeSave)
        }
    }
}

sealed class KitsuListState {
    data class Loading(val isLoading: Boolean) : KitsuListState()
    data class Success(val items: List<Kitsu>) : KitsuListState()
    data class Error(val message: String? = null) : KitsuListState()
    data object SuccessAnimeSave: KitsuListState()
}