package com.example.recipeapp.adapters

import android.content.Context
import android.opengl.Visibility
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ItemMaterialAddLayoutBinding
import com.example.recipeapp.databinding.MaterialSelectedItemLayoutBinding
import com.example.recipeapp.models.Material
import com.example.recipeapp.ui.activities.DishDetailsActivity
import com.example.recipeapp.utils.GlideLoader


class MaterialListSelectedAdapter(
    private val context: Context
) : RecyclerView.Adapter<MaterialListSelectedAdapter.MyViewHolder>() {

    private var materialList = ArrayList<Material>()

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MaterialSelectedItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val curItem = materialList[position]
        holder.bind(curItem, position, context)

        holder.binding.delButton.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                materialList.remove(materialList[position])
                notifyDataSetChanged()
            }
        }



    }

    override fun getItemCount(): Int {
        return materialList.size
    }



    /**
     * Gets the number of items in the list
     */


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(var binding: MaterialSelectedItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(material: Material, position: Int, context: Context) {
            binding.apply {
                GlideLoader(context).loadDishPicture(material.image, ivMaterialImage)
                tvMaterialName.text = material.name
            }
        }
    }

    fun setData(materials: ArrayList<Material>) {
        this.materialList = materials
        notifyDataSetChanged()
    }

}