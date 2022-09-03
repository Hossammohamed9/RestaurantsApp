package com.example.task.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.task.R
import com.example.task.databinding.CategoryItemBinding
import com.example.task.data.models.Category

class CategoryAdapter (private val onClickListener : OnClickListener): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

     class ViewHolder(val binding:CategoryItemBinding) : RecyclerView.ViewHolder(binding.root){
         fun bind(category : Category, click : OnClickListener)
         {
             binding.category = category
             binding.onClick = click
             binding.executePendingBindings()
         }
     }

    private val diffCallback = object : DiffUtil.ItemCallback<Category>(){
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var categories : List<Category>
        get() = differ.currentList
        set(value) { differ.submitList(value)}

    class OnClickListener(val clickListener: (asteroid: Category)-> Unit)
    {
        fun onClick(asteroid : Category) = clickListener(asteroid)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CategoryItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val category = categories[position]
            Glide.with(categoryImageId.context)
                .load("https://satatechnologygroup.net:3301/${category.photo}")
                .fitCenter()
                .placeholder(R.drawable.ic_loading)
                .apply(object : RequestOptions(){}
                    .error(R.drawable.ic_baseline_no_photography_24))
                .into(categoryImageId)

            categoryNameId.text = category.name
            holder.bind(category,onClickListener)
        }


    }

    override fun getItemCount() = categories.size

}