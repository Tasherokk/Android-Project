package com.example.aniframe.presentation.viewmodel

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

    private val _kitsuListState = MutableLiveData<KitsuListState>()
    val kitsuListState: LiveData<KitsuListState> get() = _kitsuListState

    private val coroutineExceptionHandler =
            CoroutineExceptionHandler { coroutineContext, throwable ->
                _kitsuListState.postValue(KitsuListState.Error(throwable.message))
            }

    fun fetchKitsuList() {
        _kitsuListState.value = KitsuListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = async {service.fetchKitsuList(10, 0)}
            val kitsuList = response.await().data
            withContext(Dispatchers.Main) {
                _kitsuListState.setValue(
                        KitsuListState.Success(
                                kitsuList.map {
                                    KitsuApi.toKitsu(it)
                                }
                        )
                )
            }

            _kitsuListState.postValue(KitsuListState.Loading(false))
        }
    }
    fun sortBy(attr: String) {
        _kitsuListState.value = KitsuListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = async {service.sortBy("-"+ attr)}
            val kitsuList = response.await().data
            withContext(Dispatchers.Main) {
                _kitsuListState.setValue(
                        KitsuListState.Success(
                                kitsuList.map {
                                    KitsuApi.toKitsu(it)
                                }
                        )
                )
            }

            _kitsuListState.postValue(KitsuListState.Loading(false))
        }
    }
    fun fetchKitsuTrending() {
        _kitsuListState.value = KitsuListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = async {service.fetchKitsuTrendingList()}
            val kitsuList = response.await().data
            withContext(Dispatchers.Main) {
                _kitsuListState.setValue(
                        KitsuListState.Success(
                                kitsuList.map {
                                    KitsuApi.toKitsu(it)
                                }
                        )
                )
            }

            _kitsuListState.postValue(KitsuListState.Loading(false))
        }
    }
    fun fetchKitsuByName(name: String) {
        _kitsuListState.value = KitsuListState.Loading(true)

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val response = async {service.fetchKitsuListByName(name)}
            val kitsuList = response.await().data
            withContext(Dispatchers.Main) {
                _kitsuListState.setValue(
                        KitsuListState.Success(
                                kitsuList.map {
                                    KitsuApi.toKitsu(it)
                                }
                        )
                )
            }

            _kitsuListState.postValue(KitsuListState.Loading(false))
        }
    }

    fun saveAnime(kitsu: Kitsu){
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