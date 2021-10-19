package com.orangecoffeeapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.orangecoffeeapp.R
import com.orangecoffeeapp.constants.Constants.OWNER_IMG_URL
import com.orangecoffeeapp.data.models.UserModel
import com.orangecoffeeapp.databinding.RecyclerItemBinding

class OwnerRecyclerAdapter(private val onOwnerItemListener: OnOwnerItemListener ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var owners = ArrayList<UserModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return  OwnerViewHolder(
                RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is OwnerViewHolder -> holder.bind(owners[position])
        }
        holder.itemView.setOnClickListener {
            onOwnerItemListener.onOwnerItemClicked(position)
        }

        holder.itemView.setOnLongClickListener {
            onOwnerItemListener.onOwnerItemClickedShowDetails(position)
            return@setOnLongClickListener true
        }


    }

    override fun getItemCount(): Int {
        return owners.size
    }

    //Custom class to describe your view
    class OwnerViewHolder
    constructor(private val binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(owner: UserModel) {

            binding.cardName.text = owner.firstName+" "+ owner.lastName

            val requestOption = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            // val bitmap = retriveVideoFrameFromVideo("https://www.youtube.com/watch?v=ND_REzCLg6I")
            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOption)
                .load(OWNER_IMG_URL)
                .into(binding.cardImage)

        }
    }

    fun submitList(ownerList: List<UserModel>) {
        owners = ownerList as ArrayList<UserModel>
    }

    interface OnOwnerItemListener{
        fun onOwnerItemClicked(position: Int)
        fun onOwnerItemClickedShowDetails(position: Int)
    }

}