package com.example.task.ui.restaurarnts

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.task.R
import com.example.task.data.models.DataX
import com.example.task.databinding.RestaurantItemBinding

class RestaurantsAdapter(private val context: Context) : RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RestaurantItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<DataX>(){
        override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem.RestauranthId == newItem.RestauranthId
        }

        override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var restaurants : List<DataX>
        get() = differ.currentList
        set(value) { differ.submitList(value)}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RestaurantItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val restaurant = restaurants[position]
            Glide.with(resrestaurantImageId.context)
                .load("https://satatechnologygroup.net:3301/${restaurant.cover}")
                .fitCenter()
                .placeholder(R.drawable.ic_loading)
                .apply(object : RequestOptions(){}
                    .error(R.drawable.ic_baseline_no_photography_24))
                .into(resrestaurantImageId)

            Glide.with(cousineImageId.context)
                .load("https://satatechnologygroup.net:3301/${restaurant.logo}")
                .fitCenter()
                .placeholder(R.drawable.ic_loading)
                .apply(object : RequestOptions(){}
                    .error(R.drawable.ic_baseline_no_photography_24))
                .into(cousineImageId)

            restaurantNameId.text = restaurant.name
            if (restaurant.rate != null){
                rateId.text = restaurant.rate.toString()
            }else{
                rateId.text = "0.0"
            }

            if (restaurant.IsOpen == "false"){
                statusId.text = context.getString(R.string.closed)
                statusId.setTextColor(Color.RED)
            }else{
                statusId.text = context.getString(R.string.open)
                statusId.setTextColor(Color.parseColor("#2FB50A"))
            }
        }
    }

    override fun getItemCount() = restaurants.size

}