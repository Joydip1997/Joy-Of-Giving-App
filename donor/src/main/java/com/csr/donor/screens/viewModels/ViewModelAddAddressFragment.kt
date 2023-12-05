package com.csr.donor.screens.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csr.donor.data.location_repository.LocationRepository
import com.csr.donor.data.geoCoding.FullPlaceData
import com.csr.donor.data.user.UserRepository
import com.csr.donor.data.user_address_model.UserAddress
import com.csr.donor.location.LocationClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelAddAddressFragment @Inject constructor(
    private val locationClient: LocationClient,
    private val locationRepository: LocationRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private var globalPlaceStreetAddress: String? = null
    private var globalPlaceCityName: String? = null
    private var globalPlaceLandmarkName: String? = null
    private var globalPlacePinCode: String? = null
    private var globalPlaceStateName: String? = null
    private var globalPlaceCountryName: String? = null
    private var globalPlaceLatitude: Double? = null
    private var globalPlaceLongitude: Double? = null

    private val _moveToLocation: MutableLiveData<Pair<LatLng, Boolean>> = MutableLiveData()
    val moveToLocation: LiveData<Pair<LatLng, Boolean>> get() = _moveToLocation

    private val _isLoading: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val isLoading: SharedFlow<Boolean> = _isLoading.asSharedFlow()

    private val _userAddressAdded: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val userAddressAdded: SharedFlow<Boolean> = _userAddressAdded.asSharedFlow()

    private val _currentToLocation: MutableLiveData<LatLng> = MutableLiveData()
    val currentToLocation: LiveData<LatLng> get() = _currentToLocation

    private val _pickupAddress: MutableLiveData<String> = MutableLiveData()
    val pickupAddress: LiveData<String> = _pickupAddress

    private val _fullAddress: MutableLiveData<FullPlaceData?> = MutableLiveData()
    val fullAddress: LiveData<FullPlaceData?> = _fullAddress

    private var fullAddressData: FullPlaceData? = null

    fun fetchCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            val currentLocation = locationClient.getCurrentLocation()
            currentLocation?.let { latLng ->
                _moveToLocation.postValue(Pair(latLng, true))
                fetchCurrentAddressFromGeoCoding(latLng.latitude, latLng.longitude)
            }
        }
    }

    fun onGoogleMapMoving() {
        viewModelScope.launch {
            _isLoading.emit(true)
        }
    }

    fun fetchCurrentAddressFromGeoCoding(
        latitude: Double,
        longitude: Double,
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.emit(true)
            val job = async {
                locationRepository.fetchAddressFromLatLng(
                    latitude, longitude
                )
            }
            val fullPlaceData = job.await()
            globalPlaceLatitude = latitude
            globalPlaceLongitude = longitude
            globalPlaceStreetAddress = fullPlaceData?.address.toString()
            globalPlaceCityName = fullPlaceData?.cityOrVillage.toString()
            globalPlaceStateName = fullPlaceData?.state.toString()
            globalPlaceCountryName = fullPlaceData?.country.toString()
            _fullAddress.postValue(fullPlaceData)
            fullAddressData = fullPlaceData
            _pickupAddress.postValue(fullPlaceData?.address.toString())
            _isLoading.emit(false)
        }
    }

    fun setNewUserAddress(streetAddress: String) {
        globalPlaceStreetAddress = streetAddress
    }

    fun setCityName(cityName: String) {
        globalPlaceCityName = cityName
    }

    fun setLandMarkName(landMarkName: String) {
        globalPlaceLandmarkName = landMarkName
    }

    fun setPinCode(pincode: String) {
        globalPlacePinCode = pincode
    }

    fun addUserAddress() {
        viewModelScope.launch(Dispatchers.IO) {
            if (globalPlaceLatitude == null || globalPlaceLongitude == null) return@launch
            val userAddress = UserAddress(
                address = globalPlaceStreetAddress.toString(),
                city = globalPlaceCityName.toString(),
                landmarkName = globalPlaceLandmarkName.toString(),
                pincode = globalPlacePinCode.toString(),
                state = globalPlaceStateName.toString(),
                country = globalPlaceCountryName.toString(),
                latitude = globalPlaceLatitude!!,
                longitude = globalPlaceLongitude!!
            )
            userRepository.addNewAddress(userAddress)
            _userAddressAdded.emit(true)
        }
    }

    fun placePickupOrder() {

    }

}