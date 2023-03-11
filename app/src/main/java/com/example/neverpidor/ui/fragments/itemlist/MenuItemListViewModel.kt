package com.example.neverpidor.ui.fragments.itemlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.Event
import com.example.neverpidor.Repositories
import com.example.neverpidor.data.MenuItemsRepository
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.network.beer.BeerList
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.model.network.snack.SnackList
import com.example.neverpidor.model.settings.AppSettings
import kotlinx.coroutines.launch
import retrofit2.Response

class MenuItemListViewModel: ViewModel() {

    private val appSettings: AppSettings by lazy {
        Repositories.appSettings
    }

    private val repository = MenuItemsRepository()

    private val _snacks = MutableLiveData<SnackList>()
    val snacks: LiveData<SnackList> = _snacks

    private val _beers = MutableLiveData<List<DomainBeer>>()
    val beers: LiveData<List<DomainBeer>> = _beers

    private val _beerResponse = MutableLiveData<Event<BeerResponse?>>()
    val beerResponse: LiveData<Event<BeerResponse?>> = _beerResponse

    private val _snackResponse = MutableLiveData<Event<SnackResponse?>>()
    val snackResponse: LiveData<Event<SnackResponse?>> = _snackResponse

    fun getSnacks() = viewModelScope.launch {
        _snacks.postValue(repository.getSnacks())
    }
    fun getBeers() = viewModelScope.launch {
         _beers.postValue(repository.getBeers())
    }
    fun deleteBeer(beerId: String) = viewModelScope.launch {
        _beerResponse.postValue(Event(repository.deleteBeer(beerId)))

    }
    fun deleteSnack(snackId: String) = viewModelScope.launch {
        _snackResponse.postValue(Event(repository.deleteSnack(snackId)))

    }

    fun getItem(): Int = appSettings.getCurrentItem()
}