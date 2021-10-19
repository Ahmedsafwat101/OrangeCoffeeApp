package com.orangecoffeeapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.Constants
import com.orangecoffeeapp.data.models.CarModel
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.FragmentLinkingBinding
import com.orangecoffeeapp.databinding.RecyclerItemBinding

class CarRecyclerAdapter(private val onCarItemListener:OnCarItemListener ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var cars = ArrayList<CarModel>()
    private lateinit var  binding: RecyclerItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return  CarViewHolder(binding)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is CarViewHolder -> holder.bind(cars[position])
        }

        holder.itemView.setOnClickListener {
            onCarItemListener.onCarItemClicked(position)
        }
        holder.itemView.setOnLongClickListener {
            onCarItemListener.onCarItemClickedShowDetails(position)
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return cars.size
    }

    //Custom class to describe your view
    class CarViewHolder
    constructor(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(car: CarModel) {

            binding.cardName.text = car.carName

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(Constants.CAR_IMG_URL)
                .into(binding.cardImage)

        }
    }

    fun submitList(carsList: List<CarModel>) {
        cars = carsList as ArrayList<CarModel>
    }

    interface OnCarItemListener{
        fun onCarItemClicked(position: Int)
        fun onCarItemClickedShowDetails(position: Int)
    }

}