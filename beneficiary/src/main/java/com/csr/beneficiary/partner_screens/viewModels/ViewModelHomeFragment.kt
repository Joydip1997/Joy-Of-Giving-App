package com.csr.beneficiary.partner_screens.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csr.beneficiary.data.Order.PickupOrderRepository
import com.csr.beneficiary.data.PickupOrderDTO
import com.csr.common.data.AppPrefs
import com.csr.beneficiary.location.LocationClient
import com.google.android.gms.maps.model.LatLng
import com.csr.beneficiary.placeFilter.model.PickupOrderFilter
import com.csr.beneficiary.placeFilter.model.pickupOrderFiltersList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelHomeFragment @Inject constructor(
    private val locationClient: LocationClient,
    private val pickupOrderRepository: PickupOrderRepository,
    private val appPrefs: AppPrefs
) : ViewModel() {


    var numberToCall = ""
    private val _pickupOrdersList: MutableLiveData<List<PickupOrderDTO>> = MutableLiveData()
    val pickupOrdersList: LiveData<List<PickupOrderDTO>> = _pickupOrdersList

    private val _myLatLong: MutableLiveData<LatLng> = MutableLiveData()
    val myLatLong: LiveData<LatLng> = _myLatLong

    private val _selectedFilters: MutableLiveData<ArrayList<PickupOrderFilter>> = MutableLiveData()
    val selectedFilters: LiveData<ArrayList<PickupOrderFilter>> get() = _selectedFilters


    init {
        _selectedFilters.value = pickupOrderFiltersList
    }

    fun fetchNearByPickupOrders() {
        viewModelScope.launch {
            val currentLocation = locationClient.getCurrentLocation()
            currentLocation?.let { latLng ->
                val nearByOrders =
                    pickupOrderRepository.getNearByOrders(latLng.latitude, latLng.longitude, appPrefs.distance,appPrefs.scrapTypes)
                _myLatLong.postValue(latLng)
                _pickupOrdersList.postValue(nearByOrders)
            }

        }
    }

}