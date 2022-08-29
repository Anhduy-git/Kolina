package com.example.recipeapp.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.CustomViewListMaterialAdapter
import com.example.recipeapp.adapters.CustomViewListRecipeAdapter
import com.example.recipeapp.databinding.ActivityAddDishBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.Dish
import com.example.recipeapp.models.User
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.GlideLoader
import java.io.IOException

class AddDishActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddDishBinding
    private var listMaterial: ArrayList<String> = ArrayList()
    private var listRecipe: ArrayList<String> = ArrayList()
    private var listMainMaterial: ArrayList<String> = ArrayList()
    private lateinit var materialAdapter: CustomViewListMaterialAdapter
    private lateinit var recipeAdapter: CustomViewListRecipeAdapter
    private var mSelectedImageFileUri: Uri? = null
    private var mSelectedImageFileURL: String = ""
    private var dishType: String = ""
    private var mUserDetails: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        getUserDetails()

        if (intent.hasExtra(Constants.EXTRA_DISH_TYPE)) {
            val tmp = intent.getStringExtra(Constants.EXTRA_DISH_TYPE)!!
            when(tmp) {
                "Man" -> dishType = Constants.DISHES_MAN
                "Ngot" -> dishType = Constants.DISHES_NGOT
                "Chay" -> dishType = Constants.DISHES_CHAY
                "Trai Cay" -> dishType = Constants.DISHES_TRAICAY
                "Nuong" -> dishType = Constants.DISHES_NUONG
                "Lau" -> dishType = Constants.DISHES_LAU
            }
        }


        //set up material list
        binding.rvMaterial.layoutManager = LinearLayoutManager(this@AddDishActivity)
        materialAdapter = CustomViewListMaterialAdapter(this@AddDishActivity)
        binding.rvMaterial.adapter = materialAdapter
        materialAdapter.setData(listMaterial)
        //set on add new material
        binding.addMaterialBtn.setOnClickListener(this@AddDishActivity)

        //set up recipe list
        binding.rvRecipe.layoutManager = LinearLayoutManager(this@AddDishActivity)
        recipeAdapter = CustomViewListRecipeAdapter(this@AddDishActivity)
        binding.rvRecipe.adapter = recipeAdapter
        recipeAdapter.setData(listRecipe)
        //set on add new recipe
        binding.addRecipeBtn.setOnClickListener(this@AddDishActivity)

        binding.ivAddUpdateDish.setOnClickListener(this@AddDishActivity)

        binding.submitBtn.setOnClickListener(this@AddDishActivity)



    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarAddDishActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarAddDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.add_recipe_btn -> {
                    listRecipe.add("")
                    recipeAdapter.setData(listRecipe)

                }
                R.id.add_material_btn -> {
                    listMaterial.add("")
                    materialAdapter.setData(listMaterial)

                }
                R.id.iv_add_update_dish -> {
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED
                    ) {

                        Constants.showImageChooserActivity(this@AddDishActivity)
                    } else {

                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.submit_btn -> {
                    if (validateDishDetails()) {
                        getMainMaterial()

                        if (mUserDetails != null) {
                            uploadDishImage()
                        }

                    }
                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooserActivity(this@AddDishActivity)

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        // The uri of selected image from phone storage.
                        mSelectedImageFileUri = data.data!!

                        //use Glide to loading image
                        GlideLoader(this).loadDishPicture(mSelectedImageFileUri!!, binding.ivDishImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddDishActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            // A log is printed when user close or cancel the image selection.
            Log.e("Request Cancelled", "Image selection cancelled")
        }

    }

    private fun validateDishDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_select_dish_image), true)
                false
            }

            TextUtils.isEmpty(binding.etDishTitle.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_dish_title), true)
                false
            }

            TextUtils.isEmpty(binding.etDishDescription.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_dish_description), true)
                false
            }

            TextUtils.isEmpty(binding.etDishServingSize.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_dish_serving_size),
                    true
                )
                false
            }

            //TODO: add more check
            else -> {
                true
            }
        }
    }
    private fun getMainMaterial() {
        for (item in binding.llListMainMaterial.children) {
            if (item is CheckBox) {
                if (item.isChecked) {
                    listMainMaterial.add(item.text.toString())
                }
            }
        }

    }


    fun dishUploadSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@AddDishActivity,
            resources.getString(R.string.dish_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()
        setResult(Activity.RESULT_OK)
        finish()
    }
    private fun uploadDishDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constants.MYRECIPEAPP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        //remove null element in list

        for (item in listMaterial) {
            if (item.trim { it <= ' ' } == "") {
                listMaterial.remove(item)
            }
        }
        for (item in listRecipe) {
            if (item.trim { it <= ' ' } == "") {
                listRecipe.remove(item)
            }
        }

        // Here we get the text from editText and trim the space
        val dish = Dish(
            FirestoreClass().getCurrentUserID(),
            username,
            binding.etDishTitle.text.toString().trim { it <= ' ' },
            binding.etDishDescription.text.toString().trim { it <= ' ' },
            binding.etDishServingSize.text.toString().trim { it <= ' ' },
            mSelectedImageFileURL,
            mUserDetails?.image,
            dishType,
            listMainMaterial,
            listMaterial,
            listRecipe
        )

        FirestoreClass().uploadDishDetails(this@AddDishActivity, dish)
    }
    //
//    /**
//     * A function to upload the selected product image to firebase cloud storage.
//     */
    private fun uploadDishImage() {

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().uploadImageToCloudStorage(
            this@AddDishActivity,
            mSelectedImageFileUri,
            Constants.DISH_IMAGE
        )
    }
    //
//    /**
//     * A function to get the successful result of product image upload.
//     */
    fun imageUploadSuccess(imageURL: String) {

        // Initialize the global image url variable.
        mSelectedImageFileURL = imageURL

        uploadDishDetails()
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getUserDetails(this@AddDishActivity)
    }
    fun userDetailsSuccess(user: User) {
        mUserDetails = user
        hideProgressDialog()
    }
}