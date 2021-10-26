package com.orangecoffeeapp.ui.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.Constants
import com.orangecoffeeapp.data.models.MenuItemModel
import com.orangecoffeeapp.databinding.RecyclerMenuOrderBinding

class OrderRecyclerAdapter(
    var getValue: (Float) -> Unit = {},
    var getUpdatedMap: (MenuItemModel, Int) -> Unit,
    var incrementAndDecrement: (Float) -> Unit = {}

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var binding:RecyclerMenuOrderBinding
    private var menuMap = HashMap<MenuItemModel,Int>()
    private var menuItems:List<MenuItemModel> = menuMap.keys.toList()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = RecyclerMenuOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("TAG","hereee")

        return  OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {

            is OrderViewHolder -> {
                    holder.bind(
                        menuItems[position],
                        menuMap[menuItems[position]],
                        getValue,getUpdatedMap,incrementAndDecrement)
            }
        }

            getValue(
                binding.itemCountTxt.text.toString().toInt() * menuItems[position].price.toString()
                    .toFloat()
            )
    }


    override fun getItemCount(): Int {
        return menuItems.size
    }

    //Custom class to describe your view
    class OrderViewHolder
    constructor(private val binding: RecyclerMenuOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            menuItem: MenuItemModel,
            count: Int?,
            getValue: (Float) -> Unit,
            getUpdatedMap: (MenuItemModel, Int) -> Unit,
            incrementAndDecrement:  (Float) -> Unit
        ) {

            binding.coffeeName.text = menuItem.coffeeName
            binding.coffeePrice.text = menuItem.price.toString()+" EG"
            binding.itemCountTxt.text = count.toString()


            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(Constants.COFFEE_MENU_ITEM_IMG_URL)
                .into(binding.cardImage)

            binding.addBut.setOnClickListener{
                binding.itemCountTxt.text = (binding.itemCountTxt.text.toString().toInt()+1).toString()

                val value:Float = menuItem.price.toString().toFloat()
                incrementAndDecrement(value)
                getUpdatedMap(menuItem, binding.itemCountTxt.text.toString().toInt())

            }

            binding.deducteBut.setOnClickListener{

                if(binding.itemCountTxt.text.toString().toInt() > 0) {
                    binding.itemCountTxt.text = (binding.itemCountTxt.text.toString().toInt()-1).toString()
                    val value:Float = menuItem.price.toString().toFloat()
                    incrementAndDecrement(value*-1)
                    getUpdatedMap(menuItem, binding.itemCountTxt.text.toString().toInt())

                }else{
                    incrementAndDecrement(0f)
                    getUpdatedMap(menuItem, 0)

                }
            }
        }
    }

    fun submitList(menuMap: HashMap<MenuItemModel,Int>) {
        this.menuMap = menuMap
        menuItems = menuMap.keys.toList()
        notifyDataSetChanged()


    }

    fun updateMap(menuItem: MenuItemModel,count:Int){
        this.menuMap[menuItem] = count
        notifyDataSetChanged()
    }

    fun getMap() =  menuMap

}

