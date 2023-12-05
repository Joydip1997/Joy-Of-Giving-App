package com.csr.donor.screens.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csr.app.MyApplication
import com.csr.donor.data.Order.PickupOrderRepository
import com.csr.donor.data.PickupLocation
import com.csr.donor.data.PickupOrderDTO
import com.csr.donor.data.PickupOrderStatus
import com.csr.common.data.AppPrefs
import com.csr.donor.data.scrapModel.PickupTime
import com.csr.donor.data.scrapModel.ScrapItemWeight
import com.csr.donor.data.scrapModel.itemPickupTimeList
import com.csr.donor.data.scrapModel.itemWeightList
import com.csr.donor.data.user.UserRepository
import com.csr.donor.data.user_address_model.UserAddress
import com.csr.donor.data.user_address_model.toDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelSchedulePickupFragment @Inject constructor(
    private val userRepository: UserRepository,
    private val pickupOrderRepository: PickupOrderRepository,
    private val appPrefs: AppPrefs
) : ViewModel() {


    private var userPickupAddress: List<UserAddress> = emptyList()
    private val _userPickupAddressList: MutableLiveData<List<UserAddress>> = MutableLiveData()
    val userPickupAddressList: LiveData<List<UserAddress>> = _userPickupAddressList

    private val _scrapItemWeightList: MutableLiveData<List<ScrapItemWeight>> = MutableLiveData()
    val scrapItemWeightList: LiveData<List<ScrapItemWeight>> = _scrapItemWeightList

    private val itemWeights = ArrayList(itemWeightList)


    private val _pickupTimeList: MutableLiveData<List<PickupTime>> = MutableLiveData()
    val pickupTimeList: LiveData<List<PickupTime>> = _pickupTimeList

    private val _selectedPickupAddress: MutableLiveData<UserAddress> = MutableLiveData()
    val selectedPickupAddress: LiveData<UserAddress> = _selectedPickupAddress


    private val itemPickupTimes = ArrayList(itemPickupTimeList)

    private var expandedUi: UiToExpand = UiToExpand.NONE

    private val _expandedUiState: MutableSharedFlow<UiToExpand> = MutableSharedFlow()
    val expandedUiState: SharedFlow<UiToExpand> = _expandedUiState

    private val _onOrderPlaced: MutableSharedFlow<PickupOrderDTO> = MutableSharedFlow()
    val onOrderPlaced: SharedFlow<PickupOrderDTO> = _onOrderPlaced

    private val _errorMessage: MutableSharedFlow<String> = MutableSharedFlow()
    val errorMessage: SharedFlow<String> = _errorMessage

    var estimatedWeight: String? = null
    var pickupDate: String? = null
    var pickupTime: String? = null
    var pickupAddress: UserAddress? = null


    init {
        _scrapItemWeightList.value = itemWeights
        _pickupTimeList.value = itemPickupTimes
        viewModelScope.launch {
            _expandedUiState.emit(UiToExpand.NONE)
        }
    }

    fun onWeightSelected(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            itemWeights.forEach {
                if(it.id == id){
                    estimatedWeight = it.name
                }
                it.isSelected = it.id == id
            }
            _scrapItemWeightList.postValue(itemWeights)
        }
    }

    fun onPickupTimeSelected(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            itemPickupTimes.forEach {
                if(it.id == id){
                    pickupTime = it.name
                }
                it.isSelected = it.id == id
            }
            _pickupTimeList.postValue(itemPickupTimes)
        }
    }


    fun openWeightChart() {
        viewModelScope.launch {
            _expandedUiState.emit(UiToExpand.WEIGHT_CHART)
        }
    }

    fun openDatePicker() {
        viewModelScope.launch {
            _expandedUiState.emit(UiToExpand.DATE_PICKER)
        }
    }

    fun openTimePicker() {
        viewModelScope.launch {
            _expandedUiState.emit(UiToExpand.TIME_PICKER)
        }
    }

    fun openAddressPicker() {
        viewModelScope.launch {
            _expandedUiState.emit(UiToExpand.ADDRESS_PICKER)
        }
    }

    fun openCloseAll() {
        viewModelScope.launch {
            _expandedUiState.emit(UiToExpand.NONE)
        }
    }

    fun onUserAddressIsSelected(id: Int) {
        viewModelScope.launch {
            userPickupAddress.forEach {
                if(it.id == id){
                    _selectedPickupAddress.postValue(it)
                    pickupAddress = it
                }
                it.isSelected = it.id == id
            }
            _userPickupAddressList.postValue(userPickupAddress)
        }
    }

    fun getUserAddress() {
        viewModelScope.launch {
            userPickupAddress = userRepository.getAllAddress()
            _userPickupAddressList.postValue(userPickupAddress)
        }
    }

    fun schedulePickup() {
        viewModelScope.launch {
            when {
                estimatedWeight == null -> {
                    _errorMessage.emit("Please select the scrap weight")
                    return@launch
                }

                pickupDate == null -> {
                    _errorMessage.emit("Please select the pickup date")
                    return@launch
                }

                pickupTime == null -> {
                    _errorMessage.emit("Please select the pickup time")
                    return@launch
                }

                pickupAddress == null -> {
                    _errorMessage.emit("Please select the pickup address")
                    return@launch
                }
            }
            val pickupOrderDTO = PickupOrderDTO(
                userId = appPrefs.userId,
                location = PickupLocation(
                    coordinates = listOf(
                        pickupAddress!!.latitude ?: 0.0,
                        pickupAddress!!.longitude ?: 0.0
                    )
                ),
                orderPickDate = pickupDate,
                orderPickTime = pickupTime,
                pickupLocation = pickupAddress?.toDTO(),
                orderStatus = PickupOrderStatus.SCHEDULED.ordinal
            )
            com.csr.app.MyApplication.pickupOrderDTO = pickupOrderDTO
            _onOrderPlaced.emit(pickupOrderDTO)
        }
    }

    enum class UiToExpand {
        WEIGHT_CHART, DATE_PICKER, TIME_PICKER, ADDRESS_PICKER, NONE
    }
}