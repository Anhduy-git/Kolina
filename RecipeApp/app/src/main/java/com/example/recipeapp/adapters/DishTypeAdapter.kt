package com.example.recipeapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemDishTypeLayoutBinding
import com.example.recipeapp.models.DishType
import com.example.recipeapp.ui.activities.DishMenuActivity
import com.example.recipeapp.ui.fragments.MenuFragment
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.GlideLoader

class DishTypeAdapter(
    private val context: Context
) : RecyclerView.Adapter<DishTypeAdapter.MyViewHolder>() {

    private var dishTypeList = emptyList<DishType>()

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemDishTypeLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
        val curItem = dishTypeList[position]
        holder.bind(curItem, context)
    }

    override fun getItemCount(): Int {
        return dishTypeList.size
    }

    /**
     * Gets the number of items in the list
     */


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(private var binding: ItemDishTypeLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dishType: DishType, context: Context) {
            binding.apply {
                GlideLoader(context).loadDishPicture(dishType.image, ivDishTypeImage)

                dishTypeItemName.text = dishType.name


                itemView.setOnClickListener {
                    val intent = Intent(context, DishMenuActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_TYPE, dishType.name)
                    context.startActivity(intent)

                }
            }
        }
    }
    fun setData(dishTypes: List<DishType>) {
        this.dishTypeList = dishTypes
        notifyDataSetChanged()
    }
}