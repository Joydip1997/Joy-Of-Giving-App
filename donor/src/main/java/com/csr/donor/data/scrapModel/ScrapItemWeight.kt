package com.csr.donor.data.scrapModel

import androidx.annotation.Keep


@Keep
data class ScrapItemWeight(
    val id: String,
    val name: String,
    var isSelected : Boolean = false
)
@Keep
val itemWeightList = listOf<ScrapItemWeight>(
    ScrapItemWeight("1","Less than 20kg"),
    ScrapItemWeight("2","20-50kg"),
    ScrapItemWeight("3","50-150kg"),
    ScrapItemWeight("4","150-500kg"),
    ScrapItemWeight("5","More than 500kg"),
)



