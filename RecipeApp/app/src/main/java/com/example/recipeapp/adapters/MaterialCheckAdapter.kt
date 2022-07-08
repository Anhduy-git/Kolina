package com.example.recipeapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import com.example.recipeapp.databinding.MaterialCheckLayoutBinding

import com.example.recipeapp.models.Material

import com.example.recipeapp.utils.GlideLoader

class MaterialCheckAdapter(
    private val context: Context,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<MaterialCheckAdapter.MyViewHolder>() {

    private var materialList = emptyList<Material>()

    companion object {
        var mClickListener: ClickListener? = null
    }

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MaterialCheckLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
        mClickListener = clickListener
        val curItem = materialList[position]
        holder.bind(curItem, context)
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
    class MyViewHolder(private var binding: MaterialCheckLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(material: Material, context: Context) {
            binding.apply {
                GlideLoader(context).loadDishPicture(material.image, ivMaterialImage)

                tvMaterialName.text = material.name

                if (material.isSelected) {
                    materialBtn.isEnabled = false
                    materialBtn.text = "Choosed"
                }
                else {
                    materialBtn.isEnabled = true
                    materialBtn.text = "Choose"
                    if (mClickListener != null) {
                        materialBtn.setOnClickListener {
                            mClickListener!!.onClickListener(material, ivMaterialImage)
                            material.isSelected = true
                            materialBtn.isEnabled = false
                            materialBtn.text = "Choosed"
                        }
                    }
                }





            }
        }
    }
    fun setData(materials: List<Material>) {
        this.materialList = materials
        notifyDataSetChanged()
    }
    interface ClickListener {
        fun onClickListener(item: Material, imageAddToCart: ImageView)
    }
}