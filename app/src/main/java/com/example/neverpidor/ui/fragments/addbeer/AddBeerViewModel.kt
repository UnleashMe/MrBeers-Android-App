package com.example.neverpidor.ui.fragments.addbeer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neverpidor.util.Event
import com.example.neverpidor.R
import com.example.neverpidor.data.MenuItemsRepository
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.SnackResponse
import com.example.neverpidor.model.network.snack.SnackRequest
import com.example.neverpidor.model.settings.AppSettings
import com.example.neverpidor.util.InvalidFields
import com.example.neverpidor.util.TextFieldsValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddBeerViewModel @Inject constructor(
    private val repository: MenuItemsRepository,
    private val appSettings: AppSettings,
    private val textFieldsValidator: TextFieldsValidator
) : ViewModel() {

    private val _beerLiveData = MutableLiveData<DomainBeer>()
    val beerLiveData: LiveData<DomainBeer> = _beerLiveData

    private val _snackLiveData = MutableLiveData<DomainSnack>()
    val snackLiveData: LiveData<DomainSnack> = _snackLiveData

    private val _beerResponse = MutableLiveData<Event<BeerResponse?>>()
    val beerResponse: LiveData<Event<BeerResponse?>> = _beerResponse

    private val _snackResponse = MutableLiveData<Event<SnackResponse?>>()
    val snackResponse: LiveData<Event<SnackResponse?>> = _snackResponse

    private val _currentLiveState = MutableLiveData<InvalidFields>()
    val currentLiveState: LiveData<InvalidFields>
        get() = _currentLiveState

    private fun addBeer(beerRequest: BeerRequest) = viewModelScope.launch(Dispatchers.IO) {
        _beerResponse.postValue(Event(repository.addBeer(beerRequest)))
    }

    private fun addSnack(snackRequest: SnackRequest) = viewModelScope.launch(Dispatchers.IO) {
        _snackResponse.postValue(
            Event(repository.addSnack(snackRequest))
        )
    }

    fun getBeerById(beerId: String) = viewModelScope.launch(Dispatchers.IO) {
        _beerLiveData.postValue(repository.getBeerById(beerId))
    }

    fun getSnackById(snackId: String) = viewModelScope.launch(Dispatchers.IO) {
        _snackLiveData.postValue(repository.getSnackById(snackId))
    }

    private fun updateBeer(beerId: String, beerRequest: BeerRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            _beerResponse.postValue(Event(repository.updateBeer(beerId, beerRequest)))
        }

    private fun updateSnack(snackId: String, snackRequest: SnackRequest) =
        viewModelScope.launch(Dispatchers.IO) {
            _snackResponse.postValue(Event(repository.updateSnack(snackId, snackRequest)))
        }

    fun getItem(): Int = appSettings.getCurrentItem()

    fun handleInput(
        title: String,
        description: String,
        type: String,
        price: String,
        alc: String? = null,
        volume: String? = null,
        itemId: String? = null
    ): Boolean {
        _currentLiveState.value =
            textFieldsValidator.validateFields(title, description, type, price, alc, volume, itemId)
        if (_currentLiveState.value!!.fields.isNotEmpty()) return false
        alc?.let {
            val beerRequest = BeerRequest(
                alc.toDouble(),
                description,
                title,
                price.toDouble(),
                type,
                volume!!.toDouble()
            )
            if (itemId != null) {
                updateBeer(itemId, beerRequest)
            } else {
                addBeer(beerRequest)
            }
            return true
        }
        val snackRequest = SnackRequest(description, title, price.toDouble(), type)
        if (itemId != null) {
            updateSnack(itemId, snackRequest)
        } else {
            addSnack(snackRequest)
        }
        return true
    }
}