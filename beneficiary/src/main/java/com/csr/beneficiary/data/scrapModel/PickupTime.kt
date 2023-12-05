package com.csr.beneficiary.data.scrapModel

data class PickupTime(
    val id: String,
    val name: String,
    var isSelected : Boolean = false
)

val itemPickupTimeList = listOf<PickupTime>(
    PickupTime("1","10AM-11AM"),
    PickupTime("2","12PM-1PM"),
    PickupTime("3","3PM-4PM"),
    PickupTime("4","4PM-5PM")
)



