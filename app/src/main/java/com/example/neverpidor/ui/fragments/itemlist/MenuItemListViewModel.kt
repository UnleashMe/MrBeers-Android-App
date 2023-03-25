package com.example.neverpidor.ui.fragments.itemlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.data.Category
import com.example.neverpidor.util.Event
import com.example.neverpidor.data.repositories.MenuItemsRepository
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.model.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemListViewModel @Inject constructor(
    private val repository: MenuItemsRepository,
    private val appSettings: AppSettings
) : ViewModel() {

    private val _snacks = MutableLiveData<List<DomainSnack>>()
    val snacks: LiveData<List<DomainSnack>> = _snacks

    private val _beers = MutableLiveData<List<DomainBeer>>()
    val beers: LiveData<List<DomainBeer>> = _beers

    private val _beerResponse = MutableLiveData<Event<BeerResponse?>>()
    val beerResponse: LiveData<Event<BeerResponse?>> = _beerResponse

    private val _snackResponse = MutableLiveData<Event<SnackResponse?>>()
    val snackResponse: LiveData<Event<SnackResponse?>> = _snackResponse

    private val _shownEpoxyState = MutableLiveData<Set<String>>()
    val shownEpoxyState: LiveData<Set<String>> = _shownEpoxyState

    fun getSnacks() = viewModelScope.launch(Dispatchers.IO) {
        repository.getDatabaseSnacks().collect {
            _snacks.postValue(it)
        }
    }

    fun getBeers() = viewModelScope.launch(Dispatchers.IO) {
        repository.getDatabaseBeers().collect {
            _beers.postValue(it)
        }
    }

    fun deleteBeer(beerId: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.deleteBeer(beerId)
        response?.let {
            repository.deleteBeerFromDatabase(beerId)
            _beerResponse.postValue(Event(response))
        } ?: _beerResponse.postValue(Event(BeerResponse(msg = "Проверьте подключение к интернету!")))
    }

    fun deleteSnack(snackId: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = repository.deleteSnack(snackId)
        response?.let {
            repository.deleteSnackFromDatabase(snackId)
            _snackResponse.postValue(Event(response))
        } ?: _snackResponse.postValue(Event(SnackResponse(msg = "Проверьте подключение к интернету!")))
    }

    fun getItem(): Category = appSettings.getCurrentItem()

    fun saveEpoxyState(set: Set<String>) {
        _shownEpoxyState.postValue(set)
    }
}