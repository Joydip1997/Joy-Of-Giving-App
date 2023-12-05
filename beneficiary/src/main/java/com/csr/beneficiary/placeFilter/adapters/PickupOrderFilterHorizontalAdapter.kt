package com.csr.beneficiary.placeFilter.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.csr.beneficiary.R
import com.csr.beneficiary.databinding.LayoutPlaceFilterViewBinding
import com.csr.beneficiary.placeFilter.model.PickupOrderFilter

class PickupOrderFilterHorizontalAdapter(private val appContext: Context) :
    RecyclerView.Adapter<PickupOrderFilterHorizontalAdapter.PlaceFilterHorizontalAdapterViewHolder>() {

    private var _places: List<PickupOrderFilter> = emptyList()

    fun setFilters(filters: ArrayList<PickupOrderFilter>) {
        _places = filters
        notifyDataSetChanged()
    }


    private var onFilterItemClicked: ((PickupOrderFilter) -> Any?)? = null

    fun setOnFilterItemClicked(onFilterItemClicked: ((PickupOrderFilter) -> Any?)) {
        this.onFilterItemClicked = onFilterItemClicked
    }

    inner class PlaceFilterHorizontalAdapterViewHolder(private val binding: LayoutPlaceFilterViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(placeFilter: PickupOrderFilter) {
            binding.apply {
                val colorStateList = if (placeFilter.isSelected) {
                    ContextCompat.getColorStateList(appContext, R.color.color_primary)
                } else {
                    ContextCompat.getColorStateList(appContext, R.color.color_light_grey)
                }

                val iconTintColor = if (placeFilter.isSelected) {
                    ContextCompat.getColor(appContext, R.color.white)
                } else {
                    ContextCompat.getColor(appContext, R.color.color_primary)
                }

                val textColor = if (placeFilter.isSelected) {
                    ContextCompat.getColor(appContext, R.color.white)
                } else {
                    ContextCompat.getColor(appContext, R.color.color_primary)
                }

                parentLayout.backgroundTintList = colorStateList
                tvFilter.setTextColor(textColor)
                ivLeftIcon.setColorFilter(iconTintColor, PorterDuff.Mode.SRC_IN)
                ivRightIcon.setColorFilter(iconTintColor, PorterDuff.Mode.SRC_IN)
                ivLeftIcon.isVisible = placeFilter.leftIcon != null
                ivRightIcon.isVisible = placeFilter.rightIcon != null
                tvFilter.isVisible = placeFilter.title != null
                placeFilter.leftIcon?.let {
                    ivLeftIcon.setImageResource(it)
                }

                placeFilter.title?.let {
                    tvFilter.text = it
                }

                placeFilter.rightIcon?.let {
                    ivRightIcon.setImageResource(it)
                }
                parentLayout.setOnClickListener {
                    onFilterItemClicked?.invoke(placeFilter)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaceFilterHorizontalAdapterViewHolder(
            LayoutPlaceFilterViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = _places.size

    override fun onBindViewHolder(holder: PlaceFilterHorizontalAdapterViewHolder, position: Int) {
        holder.bindItem(_places[position])
    }
}