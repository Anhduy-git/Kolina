package com.example.recipeapp.ui.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityDishDetailsBinding
import com.example.recipeapp.adapters.ViewPagerAdapter
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.Dish
import com.example.recipeapp.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator

class DishDetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityDishDetailsBinding
    var mDishDetails: Dish? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) {tab, position ->
            when(position) {
                0 -> {
                    tab.text = "Summary"
                }
                1 -> {
                    tab.text = "Material"
                }
                2 -> {
                    tab.text = "Recipe"
                }
            }
        }.attach()
        setupActionBar()

        if (intent!!.hasExtra(Constants.EXTRA_DISH)) {
            mDishDetails = intent!!.getParcelableExtra(Constants.EXTRA_DISH)!!
        }
        if (intent!!.hasExtra(Constants.EXTRA_VIEW_ONLY)) {
            binding.editDishBtn.visibility = View.GONE
            binding.delDishBtn.visibility = View.GONE
            binding.shareDishBtn.visibility = View.GONE

        }
        else {
            binding.editDishBtn.setOnClickListener {
                val intent = Intent(applicationContext, EditDishActivity::class.java)
                intent.putExtra(Constants.EXTRA_DISH, mDishDetails)
                startActivity(intent)
                finish()
            }
            binding.delDishBtn.setOnClickListener {
                if (mDishDetails != null) {
                    showAlertDialogToDeleteProduct(mDishDetails?.dish_id!!)
                }
            }
            binding.shareDishBtn.setOnClickListener {
                if (mDishDetails != null) {
                    showAlertDialogToUploadDish()
                }
            }
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
     * A function to notify the success result of product deleted from cloud firestore.
     */
    fun dishDeleteSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        setResult(Activity.RESULT_OK)

        finish()
    }

    /**
     * A function to show the alert dialog for the confirmation of delete product from cloud firestore.
     */
    private fun showAlertDialogToDeleteProduct(dishID: String) {

        MaterialAlertDialogBuilder(this@DishDetailsActivity)
            .setTitle(resources.getString(R.string.delete_dialog_title))
            .setMessage(resources.getString(R.string.delete_dialog_message))
            .setCancelable(false)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setNegativeButton(resources.getString(R.string.no)) {  dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

                // TODO Step 7: Call the function to delete the product from cloud firestore.
                // START
                // Show the progress dialog.
                showProgressDialog(resources.getString(R.string.please_wait))

                // Call the function of Firestore class.
                FirestoreClass().deleteDish(this@DishDetailsActivity, dishID, mDishDetails?.dish_type!!)
                // END

                dialogInterface.dismiss()
            }
            .show()

    }
    private fun showAlertDialogToUploadDish() {

        MaterialAlertDialogBuilder(this@DishDetailsActivity)
            .setTitle(resources.getString(R.string.share_dialog_title))
            .setMessage(resources.getString(R.string.share_dialog_message))
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
                shareDishDetails()
                dialogInterface.dismiss()
            }
            .show()

    }
    fun dishUploadSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@DishDetailsActivity,
            resources.getString(R.string.dish_shared_success_message),
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
    private fun shareDishDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constants.MYRECIPEAPP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        //remove null element in list


        // Here we get the text from editText and trim the space
        val dish = Dish(
            FirestoreClass().getCurrentUserID(),
            username,
            mDishDetails?.title!!,
            mDishDetails?.description!!,
            mDishDetails?.serving_size!!,
            mDishDetails?.image,
            mDishDetails?.user_image,
            mDishDetails?.dish_type!!,
            mDishDetails?.main_material,
            mDishDetails?.material,
            mDishDetails?.recipe
        )

        FirestoreClass().uploadShareDishDetails(this@DishDetailsActivity, dish)
    }
//    private fun getDishListFromFireStore() {
//        // Show the progress dialog.
//        showProgressDialog(resources.getString(R.string.please_wait))
//
//        // Call the function of Firestore class.
//        FirestoreClass().getDishList(this@DishDetailsShareActivity, mDishDetailsShare?.dish_type!!)
//    }




}