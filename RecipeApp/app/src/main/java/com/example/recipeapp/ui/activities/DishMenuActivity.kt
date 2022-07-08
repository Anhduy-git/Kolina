package com.example.recipeapp.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.DishAdapter
import com.example.recipeapp.databinding.ActivityDishMenuBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.Dish
import com.example.recipeapp.utils.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class DishMenuActivity : BaseActivity() {
    private lateinit var binding: ActivityDishMenuBinding
    private lateinit var dishType:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDishMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set up reycler view
        binding.rvDish.layoutManager = GridLayoutManager(this@DishMenuActivity, 2)
        binding.rvDish.setHasFixedSize(true)


        if (intent.hasExtra(Constants.EXTRA_DISH_TYPE)) {
            dishType = intent.getStringExtra(Constants.EXTRA_DISH_TYPE)!!
            binding.tvTitle.setText(dishType)
        }

        setupActionBar()
        //get data
        getDishListFromFireStore()

        binding.addDishBtn.setOnClickListener {
            val intent = Intent(this@DishMenuActivity, AddDishActivity::class.java)
            intent.putExtra(Constants.EXTRA_DISH_TYPE, dishType)
            startActivityForResult(intent, Constants.ADD_DISH)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //add or delete dish, then refresh
            if (requestCode == Constants.ADD_DISH || requestCode == Constants.DISH_INFO) {
                getDishListFromFireStore()
            }
        }
//        else if (resultCode == Activity.RESULT_CANCELED) {
//
//        }
    }



    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarDishMenuActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarDishMenuActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun successDishListFromFireStore(dishList: ArrayList<Dish>) {

        // Hide Progress dialog.
        hideProgressDialog()



        // TODO Step 7: Pass the third parameter value.
        // START
        val adapterDish =
            DishAdapter(this@DishMenuActivity)
        // END
        binding.rvDish.adapter = adapterDish
        adapterDish.submitList(dishList)


    }

    private fun getDishListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class.
        val dishDocument = when(dishType) {
            "Man" -> Constants.DISHES_MAN
            "Ngot" -> Constants.DISHES_NGOT
            "Chay" -> Constants.DISHES_CHAY
            "Trai Cay" -> Constants.DISHES_TRAICAY
            "Nuong" -> Constants.DISHES_NUONG
            "Lau" -> Constants.DISHES_LAU
            else -> ""
        }
        FirestoreClass().getDishList(this@DishMenuActivity, dishDocument)
    }

    /**
     * A function that will call the delete function of FirestoreClass that will delete the product added by the user.
     *
     * @param productID To specify which product need to be deleted.
     */
//    fun deleteProduct(productID: String) {
//
//        // Here we will call the delete function of the FirestoreClass. But, for now lets display the Toast message and call this function from adapter class.
//        showAlertDialogToDeleteProduct(productID)
//    }
//
//    /**
//     * A function to notify the success result of product deleted from cloud firestore.
//     */
//    fun productDeleteSuccess() {
//
//        // Hide the progress dialog
//        hideProgressDialog()
//
//        Toast.makeText(
//            this@DishMenuActivity,
//            resources.getString(R.string.dish_delete_success_message),
//            Toast.LENGTH_SHORT
//        ).show()
//
//        // Get the latest products list from cloud firestore.
//        getProductListFromFireStore()
//    }
//
//    /**
//     * A function to show the alert dialog for the confirmation of delete product from cloud firestore.
//     */
//    private fun showAlertDialogToDeleteProduct(productID: String) {
//
//        MaterialAlertDialogBuilder(this@DishMenuActivity)
//            .setTitle(resources.getString(R.string.delete_dialog_title))
//            .setMessage(resources.getString(R.string.delete_dialog_message))
//            .setCancelable(false)
//            .setIcon(android.R.drawable.ic_dialog_alert)
//            .setNegativeButton(resources.getString(R.string.no)) {  dialogInterface, _ ->
//                dialogInterface.dismiss()
//            }
//            .setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->
//
//                // TODO Step 7: Call the function to delete the product from cloud firestore.
//                // START
//                // Show the progress dialog.
//                showProgressDialog(resources.getString(R.string.please_wait))
//
//                // Call the function of Firestore class.
//                FirestoreClass().deleteProduct(this@ProductsFragment, productID)
//                // END
//
//                dialogInterface.dismiss()
//            }
//            .show()
//
//    }
}