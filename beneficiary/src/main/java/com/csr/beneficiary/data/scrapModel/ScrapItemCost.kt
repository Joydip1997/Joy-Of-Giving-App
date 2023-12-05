package com.csr.beneficiary.data.scrapModel

import com.csr.beneficiary.R

data class ScrapItemCost(
    val id: String,
    val name: String,
    val price: Int,
    val itemType: ItemType,
    val itemQuantityType: ItemQuantityType,
    val icon: Int = R.drawable.ic_newpaper,
    var isSelected: Boolean = false
)

enum class ItemType(itemTypeName : String){
    NORMAL("Normal"),LARGE_APPLIANCES("LARGE APPLIANCES"),SMALL_APPLIANCES("SMALL APPLIANCES"),MOBILE_COMPUTERS("MOBILE COMPUTERS"),OTHERS("OTHERS")
}

enum class ItemQuantityType{
    PIECE,KG
}

