package com.csr.donor.screens.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.csr.donor.R
import com.csr.donor.data.scrapModel.ScrapItemCost
import com.csr.donor.views.toggleEnableAndAlpha

class ScrapTypeSpinnerAdapter(private val appContext: Context) : BaseAdapter() {


    private var dataSource: List<ScrapItemCost> = emptyList()

    fun setPickupAddressList(_dataSource: List<ScrapItemCost>) {
        dataSource = _dataSource
        notifyDataSetChanged()
    }

    private var onScrapTypeSelected: ((id: String) -> Any?)? = null

    fun setOnScrapTypeSelected(onScrapTypeSelected: ((id: String) -> Any?)) {
        this.onScrapTypeSelected = onScrapTypeSelected
    }


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View =
            LayoutInflater.from(appContext).inflate(R.layout.layout_scrap_type_title, parent, false)
        (view.findViewById(R.id.tv_scrap_type) as TextView).text = dataSource[position].name
        Glide.with(appContext).load(dataSource[position].icon)
            .into((view.findViewById(R.id.iv_scrap) as ImageView))
        view.setOnClickListener {
            onScrapTypeSelected?.invoke(dataSource[position].id)
        }
        view.toggleEnableAndAlpha(!dataSource[position].isSelected)

        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    private class ItemHolder(row: View) {
        val label: TextView
        val img: ImageView

        init {
            label = row.findViewById(R.id.tv_scrap_type) as TextView
            img = row.findViewById(R.id.iv_scrap) as ImageView
        }
    }

}
