package com.csr.donor.screens.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csr.donor.data.Order.PickupOrderRepository
import com.csr.donor.data.PickupOrderDTO
import com.csr.common.data.AppPrefs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ViewModelMyPickupFragment @Inject constructor(
    private val pickupOrderRepository: PickupOrderRepository,
    private val appPrefs: AppPrefs
) : ViewModel() {

    var orderStatus : Int = 0

    private val _myPickupOrders: MutableLiveData<List<PickupOrderDTO>> = MutableLiveData()
    val myPickupOrders: LiveData<List<PickupOrderDTO>> = _myPickupOrders

    fun fetchPickupOrders() {
        viewModelScope.launch {
            val myOrders = pickupOrderRepository.fetchOrders(appPrefs.userId ?: return@launch,orderStatus)
            _myPickupOrders.postValue(myOrders)
        }
    }

}