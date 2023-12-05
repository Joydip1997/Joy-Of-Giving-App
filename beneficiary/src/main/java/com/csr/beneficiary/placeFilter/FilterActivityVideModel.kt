package com.csr.beneficiary.placeFilter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csr.beneficiary.R
import com.csr.common.data.AppPrefs
import com.csr.beneficiary.data.scrapModel.ItemQuantityType
import com.csr.beneficiary.data.scrapModel.ItemType
import com.csr.beneficiary.data.scrapModel.ScrapItemCost
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterActivityVideModel @Inject constructor(
    val appPrefs: AppPrefs
) : ViewModel() {

    private val scrapeItemList = listOf<ScrapItemCost>(
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
            id = "3",
            name = "Books/Copies",
            price = 14,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_book
        ),
        ScrapItemCost(
            id = "4",
            name = "Cardboard",
            price = 7,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_cardboard
        ),
        ScrapItemCost(
            id = "5",
            name = "Plastic",
            price = 10,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_plasticbottle
        ),
        ScrapItemCost(
            id = "6",
            name = "Iron",
            price = 27,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_iron_slab
        ),
        ScrapItemCost(
            id = "7",
            name = "Steel",
            price = 37,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_steel
        ),
        ScrapItemCost(
            id = "8",
            name = "Aluminium",
            price = 105,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_aluminium
        ),
        ScrapItemCost(
            id = "9",
            name = "Brass",
            price = 305,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_brass
        ),
        ScrapItemCost(
            id = "10",
            name = "Copper",
            price = 425,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.NORMAL,
            icon = R.drawable.ic_copper
        ),


        ScrapItemCost(
            id = "11",
            name = "Split AC 1.5 TON",
            price = 4200,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_ac
        ),
        ScrapItemCost(
            id = "12",
            name = "Window AC 1.5 TON",
            price = 4000,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_window_ac
        ),
        ScrapItemCost(
            id = "13",
            name = "AC 1 TON",
            price = 3000,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_ac
        ),
        ScrapItemCost(
            id = "14",
            name = "AC 2 TON",
            price = 4500,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_ac
        ),
        ScrapItemCost(
            id = "15",
            name = "Geyser",
            price = 20,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_geyser
        ),
        ScrapItemCost(
            id = "16",
            name = "Single Door Fridge",
            price = 1000,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_single_door_fridge
        ),
        ScrapItemCost(
            id = "17",
            name = "Double Door Fridge",
            price = 1200,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.LARGE_APPLIANCES,
            icon = R.drawable.ic_double_door_fridge
        ),


        ScrapItemCost(
            id = "18",
            name = "Heavy E-WASTE",
            price = 30,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_e_waste
        ),
        ScrapItemCost(
            id = "19",
            name = "Light E-WASTE",
            price = 15,
            itemQuantityType = ItemQuantityType.KG,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_e_waste
        ),
        ScrapItemCost(
            id = "20",
            name = "Printer/Scanner/Fax Machine",
            price = 15,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_printer
        ),
        ScrapItemCost(
            id = "21",
            name = "CRT TV",
            price = 150,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_crt_tv
        ),
        ScrapItemCost(
            id = "22",
            name = "Ceiling Fan",
            price = 35,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_celling_fan
        ),
        ScrapItemCost(
            id = "23",
            name = "Microwave",
            price = 200,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_microwave
        ),
        ScrapItemCost(
            id = "24",
            name = "Inverter/Stabilizer",
            price = 42,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.SMALL_APPLIANCES,
            icon = R.drawable.ic_inverter
        ),


        ScrapItemCost(
            id = "25",
            name = "Scrap Tablet",
            price = 40,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_tablet
        ),
        ScrapItemCost(
            id = "26",
            name = "Scrap Laptop",
            price = 300,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_laptop
        ),
        ScrapItemCost(
            id = "27",
            name = "CRT Monitor",
            price = 150,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_monitor
        ),
        ScrapItemCost(
            id = "28",
            name = "LCD Monitor",
            price = 20,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_monitor
        ),
        ScrapItemCost(
            id = "29",
            name = "Computer CPU",
            price = 200,
            itemQuantityType = ItemQuantityType.PIECE,
            itemType = ItemType.MOBILE_COMPUTERS,
            icon = R.drawable.ic_cpu
        ),
    )

    private val _scrapItemList: MutableLiveData<List<ScrapItemCost>> = MutableLiveData()
    val scrapItemList: LiveData<List<ScrapItemCost>> = _scrapItemList



    private val allScrapTypes = ArrayList(scrapeItemList)
    private val selectedScrapType = ArrayList<ScrapItemCost>()

    var distance = 5





    fun fetchScrapTypes(){
        viewModelScope.launch {
            val selectedScrapTypes = appPrefs.scrapTypes
            val job = launch {
                allScrapTypes.forEachIndexed { position, item ->
                    if(selectedScrapTypes.contains(item.name)){
                        item.isSelected = !item.isSelected
                    }
                }
            }
            joinAll(job)
            _scrapItemList.postValue(allScrapTypes)
        }
    }


    fun onScrapSelected(chipId: String) {
        viewModelScope.launch {
            val job = launch {
                allScrapTypes.forEachIndexed { position, item ->
                   if(item.id == chipId){
                       item.isSelected = !item.isSelected
                   }
                }
            }
            joinAll(job)
            _scrapItemList.postValue(allScrapTypes)
        }
    }

    fun applyFilters() {
        viewModelScope.launch {
            val selectedFilters = allScrapTypes.filter { it.isSelected }.map { it.name }
            appPrefs.scrapTypes = selectedFilters
            appPrefs.distance = distance
        }
    }

    fun resetFilters() {
        fetchScrapTypes()
    }
}