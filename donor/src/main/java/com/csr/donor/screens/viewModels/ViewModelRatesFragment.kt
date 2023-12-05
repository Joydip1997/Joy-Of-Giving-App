package com.csr.donor.screens.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.csr.donor.R
import com.csr.donor.data.scrapModel.ItemQuantityType
import com.csr.donor.data.scrapModel.ItemType
import com.csr.donor.data.scrapModel.ScrapItemCost
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ViewModelRatesFragment @Inject constructor() : ViewModel() {


    private val scrapeItemList = listOf(
        ScrapItemCost(
            id = "1",
            name = "Newspaper",
            price = 16,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_newpaper
        ),
        ScrapItemCost(
            id = "2",
            name = "office paper",
            price = 16,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_officepaper
        ),
        ScrapItemCost(
            id = "1",
            name = "Books/Copies",
            price = 14,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_book
        ),
        ScrapItemCost(
            id = "1",
            name = "Cardboard",
            price = 7,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_cardboard
        ),
        ScrapItemCost(
            id = "1",
            name = "Plastic",
            price = 10,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_plasticbottle
        ),
        ScrapItemCost(
            id = "1",
            name = "Iron",
            price = 27,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_iron_slab
        ),
        ScrapItemCost(
            id = "1",
            name = "Steel",
            price = 37,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_steel
        ),
        ScrapItemCost(
            id = "1",
            name = "Aluminium",
            price = 105,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_aluminium
        ),
        ScrapItemCost(
            id = "1",
            name = "Brass",
            price = 305,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_brass
        ),
        ScrapItemCost(
            id = "1",
            name = "Copper",
            price = 425,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_copper
        ),


        ScrapItemCost(
            id = "1",
            name = "Split AC 1.5 TON",
            price = 4200,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_ac
        ),
        ScrapItemCost(
            id = "2",
            name = "Window AC 1.5 TON",
            price = 4000,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_window_ac
        ),
        ScrapItemCost(
            id = "1",
            name = "AC 1 TON",
            price = 3000,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_ac
        ),
        ScrapItemCost(
            id = "1",
            name = "AC 2 TON",
            price = 4500,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_ac
        ),
        ScrapItemCost(
            id = "1",
            name = "Geyser",
            price = 20,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_geyser
        ),
        ScrapItemCost(
            id = "1",
            name = "Single Door Fridge",
            price = 1000,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_single_door_fridge
        ),
        ScrapItemCost(
            id = "1",
            name = "Double Door Fridge",
            price = 1200,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_double_door_fridge
        ),


        ScrapItemCost(
            id = "1",
            name = "Heavy E-WASTE",
            price = 30,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_e_waste
        ),
        ScrapItemCost(
            id = "2",
            name = "Light E-WASTE",
            price = 15,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_e_waste
        ),
        ScrapItemCost(
            id = "1",
            name = "Printer/Scanner/Fax Machine",
            price = 15,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_printer
        ),
        ScrapItemCost(
            id = "1",
            name = "CRT TV",
            price = 150,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_crt_tv
        ),
        ScrapItemCost(
            id = "1",
            name = "Ceiling Fan",
            price = 35,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_celling_fan
        ),
        ScrapItemCost(
            id = "1",
            name = "Microwave",
            price = 200,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_microwave
        ),
        ScrapItemCost(
            id = "1",
            name = "Inverter/Stabilizer",
            price = 42,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_inverter
        ),


        ScrapItemCost(
            id = "1",
            name = "Scrap Tablet",
            price = 40,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_tablet
        ),
        ScrapItemCost(
            id = "1",
            name = "Scrap Laptop",
            price = 300,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_laptop
        ),
        ScrapItemCost(
            id = "1",
            name = "CRT Monitor",
            price = 150,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_monitor
        ),
        ScrapItemCost(
            id = "1",
            name = "LCD Monitor",
            price = 20,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_monitor
        ),
        ScrapItemCost(
            id = "1",
            name = "Computer CPU",
            price = 200,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_cpu
        ),
    )

    private val _scrapItemList: MutableLiveData<List<ScrapItemCost>> = MutableLiveData()
    val scrapItemList: LiveData<List<ScrapItemCost>> = _scrapItemList

    init {
        _scrapItemList.value = scrapeItemList
    }

    fun onUserSearch(query: String) {
        if (query.isEmpty()) {
            _scrapItemList.value = scrapeItemList
        } else {
            _scrapItemList.value = scrapeItemList.filter { it.name.startsWith(query,true) }
        }
    }

}