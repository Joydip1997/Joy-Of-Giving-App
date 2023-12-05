package com.csr.beneficiary.placeFilter.model

import com.csr.beneficiary.R


data class PickupOrderFilter(
    val leftIcon: Int? = null,
    val title: String? = null,
    val rightIcon: Int? = null,
    val filterType: PickupOrderFilterType,
    var isSelected : Boolean = false
)

enum class PickupOrderFilterType {
    FULL_FILTER, UNLOCK_FILTERS, SORT, OPEN_NOW, MORE_FILTERS
}


val pickupOrderFiltersList = arrayListOf(
    PickupOrderFilter(leftIcon = R.drawable.ic_filter, title = "Filter", filterType = PickupOrderFilterType.FULL_FILTER),
//    PickupOrderFilter(
//        title = "Sort",
//        rightIcon = R.drawable.ic_sort,
//        filterType = PickupOrderFilterType.SORT
//    ),

)
