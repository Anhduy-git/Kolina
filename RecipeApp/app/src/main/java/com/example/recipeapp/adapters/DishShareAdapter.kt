package com.example.recipeapp.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.persistableBundleOf


import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ItemDishShareLayoutBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.Dish
import com.example.recipeapp.ui.activities.CommentActivity
import com.example.recipeapp.ui.activities.DishDetailsActivity
import com.example.recipeapp.ui.activities.DishDetailsShareActivity
import com.example.recipeapp.ui.activities.UserProfileShareActivity
import com.example.recipeapp.ui.fragments.ShareDishFragment
import com.example.recipeapp.utils.*

class DishShareAdapter(
    private val activity: Activity,
    private val fragment: ShareDishFragment
) : ListAdapter<Dish, DishShareAdapter.MyViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Dish>() {
            override fun areItemsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Dish, newItem: Dish): Boolean {
                return false
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
        return MyViewHolder(ItemDishShareLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
        holder.bind(curItem, activity, fragment)

    }


    /**
     * Gets the number of items in the list
     */


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(private var binding: ItemDishShareLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(dish: Dish, activity: Activity, fragment: ShareDishFragment) {
            binding.apply {
                if (dish.image != null) {
                    GlideLoader(activity).loadDishPicture(dish.image, ivDishImage)
                }

                if (dish.user_image != null) {
                    GlideLoader(activity).loadUserPicture(dish.user_image, profileImage)
                }

                dishItemName.text = dish.title
                tvDishUserName.text = dish.user_name

                llUserInfo.setOnClickListener {
                    val intent: Intent = Intent(activity, UserProfileShareActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_ID, dish.user_id)
                    activity.startActivity(intent)
                }

                itemView.setOnClickListener {
                    val intent = Intent(activity, DishDetailsShareActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH, dish)
                    activity.startActivity(intent)
                }
                commentBtn.setOnClickListener {
                    val intent = Intent(activity, CommentActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_ID, dish.dish_id)
                    intent.putExtra(Constants.EXTRA_UPDATE_INDEX, position)
                    fragment.startActivityForResult(intent, Constants.ADD_COMMENT)

                    
                }
                var numUserComment = 0
                FirestoreClass().getNumComment(dish.dish_id, object: FunctionCallBackNumComment {
                    override fun onCallbackNumComment(numComment: Int) {
                        numUserComment = numComment
                        tvNumComment.setText(numUserComment.toString())
                    }
                })
                //set up for like
                var liked = false
                FirestoreClass().isLiked(dish.dish_id, object: FunctionCallBackIsLiked {
                    override fun onCallbackIsLiked(isLiked: Boolean) {
                        if (isLiked) {
                            liked = isLiked
                            likeDishBtn.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_liked_icon))
                        }
                    }
                })
                var numUserLiked = 0
                FirestoreClass().getNumUsersLike(dish.dish_id, object: FunctionCallBackNumLiked {
                    override fun onCallbackNumLiked(numLiked: Int) {
                        numUserLiked = numLiked
                        tvNumLiked.setText(numLiked.toString())
                    }
                })


                likeDishBtn.setOnClickListener {

                    if (liked) {
                        liked = false
                        likeDishBtn.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_like_icon))
                        numUserLiked--
                        tvNumLiked.setText((numUserLiked).toString())
                        FirestoreClass().updateNotLike(dish.dish_id)
                    } else {
                        liked = true
                        likeDishBtn.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_liked_icon))
                        numUserLiked++
                        tvNumLiked.setText((numUserLiked).toString())
                        FirestoreClass().updateLike(dish.dish_id)
                    }
                }

                //set up for dislike
                var disliked = false
                FirestoreClass().isDisliked(dish.dish_id, object: FunctionCallBackIsDisliked {
                    override fun onCallbackIsDisliked(isDisliked: Boolean) {
                        if (isDisliked) {
                            disliked = isDisliked
                            dislikeDishBtn.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_disliked))
                        }
                    }
                })
                var numUserDisliked = 0
                FirestoreClass().getNumUsersDislike(dish.dish_id, object: FunctionCallBackNumDisliked {
                    override fun onCallbackNumDisliked(numDisliked: Int) {
                        numUserDisliked = numDisliked
                        tvNumDisliked.setText(numUserDisliked.toString())
                    }
                })


                dislikeDishBtn.setOnClickListener {

                    if (disliked) {
                        disliked = false
                        dislikeDishBtn.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_dislike))
                        numUserDisliked--
                        tvNumDisliked.setText((numUserDisliked).toString())
                        FirestoreClass().updateNotDislike(dish.dish_id)
                    } else {
                        disliked = true
                        dislikeDishBtn.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_disliked))
                        numUserDisliked++
                        tvNumDisliked.setText((numUserDisliked).toString())
                        FirestoreClass().updateDislike(dish.dish_id)
                    }
                }
            }
        }
    }
}