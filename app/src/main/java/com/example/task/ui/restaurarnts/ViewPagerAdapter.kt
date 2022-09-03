package com.example.task.ui.restaurarnts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.task.R
import com.example.task.databinding.ViewPagerItemBinding
import com.example.task.data.models.Sliders

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.ImageViewHolder>(){

    inner class ImageViewHolder(val binding : ViewPagerItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Sliders>(){
        override fun areItemsTheSame(oldItem: Sliders, newItem: Sliders): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Sliders, newItem: Sliders): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var sliders : List<Sliders>
        get() = differ.currentList
        set(value) { differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ViewPagerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
            val slider = sliders[position]
        Glide.with(holder.binding.imageViewId.context)
            .load("https://satatechnologygroup.net:3301/${slider.photo}")
            .fitCenter()
            .placeholder(R.drawable.ic_loading)
            .apply(object : RequestOptions(){}
                .error(R.drawable.ic_baseline_no_photography_24))
            .into(holder.binding.imageViewId)

    }

    override fun getItemCount() = sliders.size

}