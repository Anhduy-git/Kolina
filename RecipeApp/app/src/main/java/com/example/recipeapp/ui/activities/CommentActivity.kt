package com.example.recipeapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.adapters.CommentListAdapter
import com.example.recipeapp.databinding.ActivityCommentBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.Comment
import com.example.recipeapp.models.User
import com.example.recipeapp.ui.fragments.ShareDishFragment
import com.example.recipeapp.utils.Constants
import java.util.*


class CommentActivity : BaseActivity() {
    private lateinit var binding: ActivityCommentBinding
    private var mUser: User? = null
    private var dishId: String? = null
    private var addNewComment = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvComment.layoutManager = LinearLayoutManager(this@CommentActivity)
        binding.rvComment.setHasFixedSize(true)


        if (intent!!.hasExtra(Constants.EXTRA_DISH_ID)) {
            dishId = intent!!.getStringExtra(Constants.EXTRA_DISH_ID)!!
            if (dishId != null) {
                getCommentListFromFireStore()
                binding.rvComment.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)

                        try {
                            val firstPos: Int = (binding.rvComment.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                            if (firstPos > 0) {
                                binding.swipeRefreshLayout.setEnabled(false)
                            } else {
                                binding.swipeRefreshLayout.setEnabled(true)
                                if (binding.rvComment.getScrollState() == 1) if (binding.swipeRefreshLayout.isRefreshing()) binding.rvComment.stopScroll()
                            }
                        } catch (e: Exception) {
//                    Log.e(TAG, "Scroll Error : " + e.localizedMessage)
                        }
                    }
                })
                binding.swipeRefreshLayout.setOnRefreshListener {
                    getCommentListFromFireStore()
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }

        }

        //get user detail
        getUserDetails()
        binding.commentBtn.setOnClickListener {

            val content = binding.etComment.text.toString()
            if (!TextUtils.isEmpty(content.trim { it <= ' ' })) {

                if (mUser != null && dishId != null) {
                    //clear text
                    binding.etComment.text?.clear()
//                    var commentDate = ""
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                        val current = LocalDateTime.now()
//                        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss")
//                        commentDate =  current.format(formatter)
//                    } else {
//                        val date = Date()
//                        val formatter = SimpleDateFormat("MMM dd yyyy HH:mma")
//                        commentDate = formatter.format(date)
//                    }
                    val comment = Comment(mUser!!.id, mUser!!.image, "${mUser!!.firstName} ${mUser!!.lastName}", content, System.currentTimeMillis())
                    addComment(comment)
                }
            }
        }
    }

    private fun addComment(comment: Comment) {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().updateComment(this@CommentActivity, dishId!!, comment)
    }
    fun successAddComment() {
        hideProgressDialog()
        getCommentListFromFireStore()
        addNewComment = true

    }
    private fun getCommentListFromFireStore() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getCommentList(this@CommentActivity, dishId!!)
    }
    fun successCommentListFromFireStore(commentList: ArrayList<Comment>) {
        //sort data
        Collections.sort(commentList,
            Comparator<Comment> { lhs: Comment, rhs: Comment -> rhs.date.compareTo(lhs.date) })

        hideProgressDialog()
        val adapterCommentList =
            CommentListAdapter(this@CommentActivity)
        // END
        binding.rvComment.adapter = adapterCommentList
        adapterCommentList.submitList(commentList)

    }

    private fun getUserDetails() {

        FirestoreClass().getUserDetails(this@CommentActivity)
    }
    fun userDetailsSuccess(user: User) {
        mUser = user
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        if (MotionEvent.ACTION_OUTSIDE == event?.action) {
//            Log.d("testhaha", "cone4")
//            setResult(RESULT_OK)
//            finish()
//        }
//        return super.onTouchEvent(event)
//    }

    override fun finish() {
        if (addNewComment) {
            val intentIdx: Intent = Intent()
            intentIdx.putExtra(Constants.EXTRA_UPDATE_INDEX, intent.getIntExtra(Constants.EXTRA_UPDATE_INDEX, -1))
            setResult(RESULT_OK, intentIdx)
        }
        super.finish()
    }




}