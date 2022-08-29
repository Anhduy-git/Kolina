package com.example.recipeapp.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemDishLayoutBinding

import com.example.recipeapp.models.Dish
import com.example.recipeapp.ui.activities.DishDetailsActivity
import com.example.recipeapp.ui.activities.ResultDishActivity
import com.example.recipeapp.utils.Constants

import com.example.recipeapp.utils.GlideLoader

class DishAdapter(
    private val activity: Activity
) : ListAdapter<Dish, DishAdapter.MyViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Dish>() {
            override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem.dish_id == newItem.dish_id
            }

            override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return oldItem == newItem
            }
        }
    }

    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, view: Int): MyViewHolder {
        return MyViewHolder(ItemDishLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given  to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given . You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val curItem = getItem(position)
        holder.bind(curItem, activity)
    }



    /**
     * Gets the number of items in the list
     */


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(private var binding: ItemDishLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dish: Dish, activity: Activity) {
            binding.apply {
                if (dish.image != null && dish.image != "") {
                    GlideLoader(activity).loadDishPicture(dish.image, ivDishImage)
                }

                dishItemName.text = dish.title

                itemView.setOnClickListener {
                    val intent = Intent(activity, DishDetailsActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH, dish)
                    if (activity is ResultDishActivity) {
                        intent.putExtra(Constants.EXTRA_VIEW_ONLY, true)
                    }
                    activity.startActivityForResult(intent, Constants.DISH_INFO)
                }
            }
        }
    }
}