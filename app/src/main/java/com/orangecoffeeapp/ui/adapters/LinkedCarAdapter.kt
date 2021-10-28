package com.orangecoffeeapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.Constants
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.LinkedCarsWithOwners
import com.orangecoffeeapp.databinding.LinkedRecyclerItemBinding
import com.orangecoffeeapp.databinding.RecyclerItemBinding

class LinkedCarAdapter(var onClick: (Int)->Unit ={}, var onLongClick:(Int)-> Unit ={}) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var linkedCarsWithOwners = ArrayList<LinkedCarsWithOwners>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LinkedViewHolder(
            LinkedRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is LinkedViewHolder -> holder.bind(linkedCarsWithOwners[position])
        }

        holder.itemView.setOnClickListener {
            onClick(position)
        }
        holder.itemView.setOnLongClickListener {
            onLongClick(position)
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return linkedCarsWithOwners.size
    }

    //Custom class to describe your view
    class LinkedViewHolder
    constructor(private val binding: LinkedRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(linkedCar: LinkedCarsWithOwners) {

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(Constants.LINKED_IMG_URL)
                .into(binding.cardImage)

        }
    }

    fun submitList(linkedList: List<LinkedCarsWithOwners>) {
        linkedCarsWithOwners = linkedList as ArrayList<LinkedCarsWithOwners>
    }

}