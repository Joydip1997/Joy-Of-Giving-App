package com.csr.donor.screens.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csr.donor.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ViewModelHomeFragment @Inject constructor() : ViewModel() {


    private val _bannerImageList: MutableLiveData<List<Int>> = MutableLiveData()
    val bannerImageList: LiveData<List<Int>> = _bannerImageList

    init {
        _bannerImageList.value = listOf(
            R.drawable.banner_1,
            R.drawable.banner_2,
            R.drawable.banner_3,
        )
    }
}