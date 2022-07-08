package com.example.recipeapp.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivitySettingsBinding
import com.example.recipeapp.databinding.ActivityUserProfileBinding
import com.example.recipeapp.databinding.ActivityUserProfileShareBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.User
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import java.io.IOException

class UserProfileShareActivity : BaseActivity() {
    private lateinit var binding: ActivityUserProfileShareBinding
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        if (intent!!.hasExtra(Constants.EXTRA_USER_ID)) {
            val userId = intent!!.getStringExtra(Constants.EXTRA_USER_ID)!!
            getUserDetailsById(userId)
        }

    }


    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarSettingsActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        binding.toolbarSettingsActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun getUserDetailsById(userId: String) {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetailsById(this@UserProfileShareActivity, userId)
    }
    fun userDetailsSuccess(user: User) {
        mUserDetails = user
        hideProgressDialog()
        GlideLoader(this@UserProfileShareActivity).loadUserPicture(user.image, binding.ivUserPhoto)
        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvMobileNumber.text = user.mobile.toString()
    }


}