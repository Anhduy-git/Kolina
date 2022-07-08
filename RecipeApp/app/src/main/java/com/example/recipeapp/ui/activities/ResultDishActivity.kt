package com.example.recipeapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.DishAdapter
import com.example.recipeapp.databinding.ActivityResultDishBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.Dish
import com.example.recipeapp.models.Material
import com.example.recipeapp.utils.Constants

class ResultDishActivity : BaseActivity() {
    private lateinit var binding: ActivityResultDishBinding
    private var listAllDish: ArrayList<Dish> = ArrayList()
    private var materialList: ArrayList<Material>? = null
    private var materialListName: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultDishBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        //set up reycler view
        binding.rvDish.layoutManager = LinearLayoutManager(this@ResultDishActivity)
        binding.rvDish.setHasFixedSize(true)

        if (intent.hasExtra(Constants.EXTRA_MATERIAL_LIST)) {
            materialList = intent.getParcelableArrayListExtra(Constants.EXTRA_MATERIAL_LIST)!!
            if (materialList != null) {

                materialListName = ArrayList()
                for (item in materialList!!) {
                    materialListName!!.add(item.name)
                }

                getDishListFromFireStore()
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarDishResultActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarDishResultActivity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun successDishListFromFireStore(dishList: ArrayList<Dish>) {

        for (item in dishList) {
            if (materialListName!!.containsAll(item.main_material!!)) {
                listAllDish.add(item)
            }
        }

        hideProgressDialog()

        val adapterDish =
            DishAdapter(this@ResultDishActivity)
        // END
        binding.rvDish.adapter = adapterDish
        adapterDish.submitList(listAllDish)

    }

    private fun getDishListFromFireStore() {

        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))


        FirestoreClass().getAllDishList(this@ResultDishActivity)





    }
}