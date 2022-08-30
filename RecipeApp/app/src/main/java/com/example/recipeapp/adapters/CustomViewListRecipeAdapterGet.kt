package com.example.recipeapp.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ItemRecipeAddLayoutGetBinding

class CustomViewListRecipeAdapterGet(
    private val context: Context
) : RecyclerView.Adapter<CustomViewListRecipeAdapterGet.MyViewHolder>() {

    private var recipeContentList = ArrayList<String>()

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemRecipeAddLayoutGetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
        val curItem = recipeContentList[position]
        holder.bind(curItem, position, context)


    }

    override fun getItemCount(): Int {
        return recipeContentList.size
    }

    /**
     * Gets the number of items in the list
     */


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(var binding: ItemRecipeAddLayoutGetBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recipeContent: String, position: Int, context: Context) {

            binding.apply {
                tvNum.setText(context.resources.getString(R.string.recipe_num, position + 1))
                etRecipeDetail.setMaxLines(Integer.MAX_VALUE); // Or specify a lower value if you want
                etRecipeDetail.setHorizontallyScrolling(false);
                etRecipeDetail.setText(recipeContent)
            }
        }
    }
    fun setData(recipeContentList: ArrayList<String>) {
        this.recipeContentList = recipeContentList
        notifyDataSetChanged()
    }
}