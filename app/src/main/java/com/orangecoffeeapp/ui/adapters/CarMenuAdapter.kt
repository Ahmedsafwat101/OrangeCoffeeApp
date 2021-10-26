package com.orangecoffeeapp.ui.adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.Constants
import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.databinding.RecyclerMenuItemBinding

class CarMenuAdapter (var onClick: (Int)->Unit ={} ,var onLongClick:(Int)-> Unit ={}): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var menu = ArrayList<MenuItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MenuViewHolder(
            RecyclerMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is MenuViewHolder -> holder.bind(menu[position])
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
        return menu.size
    }

    //Custom class to describe your view
    class MenuViewHolder
    constructor(private val binding: RecyclerMenuItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: MenuItemModel) {

            binding.coffeeName.text = menu.coffeeName
            binding.coffeePrice.text = menu.price.toString()+" EG"

            /*if(menu.available){
                binding.logInBtn.setBackgroundColor(Color.GREEN)
                binding.logInBtn.text ="Available"
            }else{
                binding.logInBtn.setBackgroundColor(Color.YELLOW)
                binding.logInBtn.text ="Not Available"
            }*/


            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(Constants.COFFEE_MENU_ITEM_IMG_URL)
                .into(binding.cardImage)

        }
    }

    fun submitList(menuList: List<MenuItemModel>) {
        menu = menuList as ArrayList<MenuItemModel>
        notifyDataSetChanged()

    }



}