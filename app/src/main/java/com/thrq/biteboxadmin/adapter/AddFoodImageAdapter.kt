package com.thrq.biteboxadmin.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thrq.biteboxadmin.databinding.ImageItemBinding

class AddFoodImageAdapter (val list: ArrayList<Uri>)
    : RecyclerView.Adapter<AddFoodImageAdapter.AddFoodImageViewHolder>() {

    inner class AddFoodImageViewHolder(val binding: ImageItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFoodImageViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddFoodImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AddFoodImageViewHolder, position: Int) {
        holder.binding.itemImg.setImageURI(list[position])
    }
}