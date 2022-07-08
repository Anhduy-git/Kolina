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
import com.example.recipeapp.ui.activities.DishDetailsActivity


class CustomViewListMaterialAdapter(
    private val context: Context
) : RecyclerView.Adapter<CustomViewListMaterialAdapter.MyViewHolder>() {

    private var materialContentList = ArrayList<String>()

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemMaterialAddLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
        val curItem = materialContentList[position]
        holder.bind(curItem, position, context)
        holder.binding.etMaterialDetail.doAfterTextChanged { text ->
            if (!TextUtils.isEmpty(text.toString().trim { it <= ' ' })) {
                materialContentList[holder.adapterPosition] = text.toString().trim { it <= ' ' }
            }


        }

        holder.binding.delButton.setOnClickListener {
            if (position != RecyclerView.NO_POSITION) {
                materialContentList.remove(materialContentList[position])
                notifyDataSetChanged()
            }
        }



    }

    override fun getItemCount(): Int {
        return materialContentList.size
    }



    /**
     * Gets the number of items in the list
     */


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(var binding: ItemMaterialAddLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(materialContent: String, position: Int, context: Context) {
            binding.apply {
                tvNum.setText(context.resources.getString(R.string.material_num, position + 1))
                etMaterialDetail.setText(materialContent)
            }
        }
    }
    fun setData(materialContentList: ArrayList<String>) {
        this.materialContentList = materialContentList
        notifyDataSetChanged()
    }

}