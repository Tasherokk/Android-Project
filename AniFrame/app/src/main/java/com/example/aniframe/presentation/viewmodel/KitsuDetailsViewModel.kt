package com.example.aniframe.presentation.viewmodel

import KitsuDetailsApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.aniframe.data.network.KitsuService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class KitsuDetailsViewModel(
    private val service: KitsuService
) : ViewModel() {
    class Provider(
        val service: KitsuService
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return KitsuDetailsViewModel(service) as T
        }
    }
    private val _kitsuDetailsState = MutableLiveData<KitsuDetailsState>()
    val kitsuDetailsState: LiveData<KitsuDetailsState> get() = _kitsuDetailsState

    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            _kitsuDetailsState.value = KitsuDetailsState.Error(throwable.message)
        }

    fun fetchKitsuDetails(kitsuId: Int) {
        _kitsuDetailsState.value = KitsuDetailsState.Loading(true)

        viewModelScope.launch(coroutineExceptionHandler) {
            val response = async { service.fetchKitsuDetails(kitsuId) }
            val kitsuList = response.await().getKitsu()
            withContext(Dispatchers.Main) {
                _kitsuDetailsState.value = KitsuDetailsState.Success(kitsuList)
                _kitsuDetailsState.postValue(KitsuDetailsState.Loading(false))
            }
        }
    }
}
sealed class KitsuDetailsState {
    data class Loading(val isLoading: Boolean) : KitsuDetailsState()
    data class Success(val item: KitsuDetailsApi) : KitsuDetailsState()
    data class Error(val message: String? = null) : KitsuDetailsState()
}