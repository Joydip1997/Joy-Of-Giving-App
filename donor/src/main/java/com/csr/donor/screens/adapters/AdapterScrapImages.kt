package com.csr.donor.screens.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.csr.donor.databinding.LayoutScrapImageItemBinding

class AdapterScrapImages(private val context: Context) :
    RecyclerView.Adapter<AdapterScrapImages.AdapterScrapImageItemViewHolder>() {

    private var _scrapeImageList: List<Uri> = emptyList()

    fun setScrapeImageList(scrapeImageList: List<Uri>) {
        _scrapeImageList = scrapeImageList
        notifyDataSetChanged()
    }

    private var _onItemClickListener: ((Int) -> Any?)? = null

    fun setOnRemovePhotoClicked(onItemClickListener : ((Int) -> Any?)){
        _onItemClickListener = onItemClickListener
    }


    inner class AdapterScrapImageItemViewHolder(private val binding: LayoutScrapImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position : Int,image: Uri) {
            binding.apply {
                Glide.with(context).load(image).centerCrop().into(ivScrap)
                ivClose.setOnClickListener {
                    _onItemClickListener?.invoke(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AdapterScrapImageItemViewHolder(
            LayoutScrapImageItemBinding.inflate(LayoutInflater.from(context), parent, false)
        )

    override fun getItemCount() = _scrapeImageList.size

    override fun onBindViewHolder(holder: AdapterScrapImageItemViewHolder, position: Int) {
        val item = _scrapeImageList[position]
        holder.bind(position,item)
    }
}