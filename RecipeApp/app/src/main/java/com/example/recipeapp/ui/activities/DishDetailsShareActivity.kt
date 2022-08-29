package com.example.recipeapp.ui.activities

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.example.recipeapp.R
import com.example.recipeapp.adapters.ViewPagerAdapterShare
import com.example.recipeapp.databinding.ActivityDishDetailsShareBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.Dish
import com.example.recipeapp.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator

class DishDetailsShareActivity : BaseActivity() {
    private lateinit var binding: ActivityDishDetailsShareBinding
    var mDishDetailsShare: Dish? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishDetailsShareBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = ViewPagerAdapterShare(supportFragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) {tab, position ->
            when(position) {
                0 -> {
                    tab.text = "Summary"
                }
                1 -> {
                    tab.text = "Ingredient"
                }
                2 -> {
                    tab.text = "Recipe"
                }
            }
        }.attach()
        setupActionBar()

        if (intent!!.hasExtra(Constants.EXTRA_DISH)) {
            mDishDetailsShare = intent!!.getParcelableExtra(Constants.EXTRA_DISH)!!
        }

        binding.cloneDishBtn.setOnClickListener {

            showAlertDialogToCloneDish()
        }


    }
    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarDishDetailsActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarDishDetailsActivity.setNavigationOnClickListener {
            onBackPressed()
        }

    }



    /**
     * A function to show the alert dialog for the confirmation of delete product from cloud firestore.
     */
    private fun showAlertDialogToCloneDish() {

        MaterialAlertDialogBuilder(this@DishDetailsShareActivity)
            .setTitle(resources.getString(R.string.clone_dialog_title))
            .setMessage(resources.getString(R.string.clone_dialog_message))
            .setCancelable(false)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

                // TODO Step 7: Call the function to delete the product from cloud firestore.
                // START
                // Show the progress dialog.
                showProgressDialog(resources.getString(R.string.please_wait))
                cloneDishDetails()
                dialogInterface.dismiss()
            }
            .show()

    }
//    private fun getDishListFromFireStore() {
//        // Show the progress dialog.
//        showProgressDialog(resources.getString(R.string.please_wait))
//
//        // Call the function of Firestore class.
//        FirestoreClass().getDishList(this@DishDetailsShareActivity, mDishDetailsShare?.dish_type!!)
//    }

    fun dishCloneSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@DishDetailsShareActivity,
            resources.getString(R.string.dish_cloned_success_message),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
    private fun cloneDishDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constants.MYRECIPEAPP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        //remove null element in list


        // Here we get the text from editText and trim the space
        val dish = Dish(
            FirestoreClass().getCurrentUserID(),
            username,
            mDishDetailsShare?.title!!,
            mDishDetailsShare?.description!!,
            mDishDetailsShare?.serving_size!!,
            mDishDetailsShare?.image,
            mDishDetailsShare?.user_image,
            mDishDetailsShare?.dish_type!!,
            mDishDetailsShare?.main_material,
            mDishDetailsShare?.material,
            mDishDetailsShare?.recipe
        )

        FirestoreClass().uploadDishDetails(this@DishDetailsShareActivity, dish)
    }
    //
//    /**
//     * A function to upload the selected product image to firebase cloud storage.
//     */


    //
//    /**
//     * A function to get the successful result of product image upload.
//     */


}