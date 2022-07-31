package com.example.recipeapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.ItemCommentLayoutBinding

import com.example.recipeapp.databinding.ItemDishShareLayoutBinding
import com.example.recipeapp.models.Comment

import com.example.recipeapp.models.Dish
import com.example.recipeapp.ui.activities.UserProfileShareActivity

import com.example.recipeapp.utils.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.log

class CommentListAdapter(
    private val context: Context

) : ListAdapter<Comment, CommentListAdapter.MyViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return false
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
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
        return MyViewHolder(ItemCommentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
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
        holder.bind(curItem, context)
    }



    /**
     * Gets the number of items in the list
     */


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(private var binding: ItemCommentLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment, context: Context) {
            binding.apply {
                if (comment.userImage != null) {
                    GlideLoader(context).loadDishPicture(comment.userImage, profileImage)
                }
                tvUserName.setText(comment.userFullName)
                tvComment.setText(comment.content)

//                var commentDate = ""
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    val current = LocalDateTime.now()
//                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
//                    commentDate =  current.format(formatter)
//                } else {
//                    val date = Date()
//                    val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
//                    commentDate = formatter.format(date)
//                }

                val formatter = SimpleDateFormat("yyyy.MM.dd  HH:mm:ss", Locale.getDefault())
                val calendar: Calendar = Calendar.getInstance()
                calendar.timeInMillis = comment.date
                val commentDate = formatter.format(calendar.time)
                tvDate.setText(commentDate.toString())

                profileImage.setOnClickListener {
                    val intent: Intent = Intent(context, UserProfileShareActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_ID, comment.userId)
                    context.startActivity(intent)
                }
            }
        }

    }
}