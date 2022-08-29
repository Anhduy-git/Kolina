package com.example.recipeapp.ui.activities

import android.Manifest
import android.app.Activity
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
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.R
import com.example.recipeapp.adapters.CustomViewListMaterialAdapter
import com.example.recipeapp.adapters.CustomViewListRecipeAdapter
import com.example.recipeapp.databinding.ActivityEditDishBinding
import com.example.recipeapp.models.Dish
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.GlideLoader
import java.io.IOException

class EditDishActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityEditDishBinding
    private var listMaterial: ArrayList<String> = ArrayList()
    private var listRecipe: ArrayList<String> = ArrayList()
    private var listMainMaterial: ArrayList<String> = ArrayList()
    private lateinit var materialAdapter: CustomViewListMaterialAdapter
    private lateinit var recipeAdapter: CustomViewListRecipeAdapter
    private var mSelectedImageFileUri: Uri? = null
    private var mSelectedImageFileURL: String = ""

    private var mDishDetails: Dish? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDishBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.rvMaterial.layoutManager = LinearLayoutManager(this@EditDishActivity)
        materialAdapter = CustomViewListMaterialAdapter(this@EditDishActivity)
        binding.rvMaterial.adapter = materialAdapter
        //set on add new material
        binding.addMaterialBtn.setOnClickListener(this@EditDishActivity)

        //set up recipe list
        binding.rvRecipe.layoutManager = LinearLayoutManager(this@EditDishActivity)
        recipeAdapter = CustomViewListRecipeAdapter(this@EditDishActivity)
        binding.rvRecipe.adapter = recipeAdapter
        binding.addRecipeBtn.setOnClickListener(this@EditDishActivity)

        binding.ivAddUpdateDish.setOnClickListener(this@EditDishActivity)

        binding.saveBtn.setOnClickListener(this@EditDishActivity)

        setupActionBar()
        setupData()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarEditDishActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        binding.toolbarEditDishActivity.setNavigationOnClickListener {
            onBackPressed()
        }

    }
    private fun setupData() {
        if (intent!!.hasExtra(Constants.EXTRA_DISH)) {
            mDishDetails = intent!!.getParcelableExtra(Constants.EXTRA_DISH)!!
        }
        if (mDishDetails != null) {
            binding.etDishTitle.setText(mDishDetails?.title)
            binding.etDishDescription.setText(mDishDetails?.description)
            binding.etDishServingSize.setText(mDishDetails?.serving_size)
            listMaterial = mDishDetails?.material!!
            listRecipe = mDishDetails?.recipe!!
            listMainMaterial = mDishDetails?.main_material!!
            materialAdapter.setData(listMaterial)
            recipeAdapter.setData(listRecipe)
            mSelectedImageFileUri = Uri.parse(mDishDetails?.image)
            GlideLoader(this).loadDishPicture(mSelectedImageFileUri!!, binding.ivDishImage)
            var idx = 0
            for (item in binding.llListMainMaterial.children) {
                if (item is CheckBox) {
                    if (item.text == listMainMaterial[idx]) {
                        item.isChecked = true
                        idx++
                        if (idx >= listMainMaterial.size) {
                            break
                        }
                    }
                }
            }

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

                        Constants.showImageChooserActivity(this@EditDishActivity)
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
                R.id.save_btn -> {
                    getMainMaterial()
                    if (validateDishDetails() && mSelectedImageFileUri.toString() != mDishDetails?.image) {
                        uploadDishImage()
                    } else {
                        showProgressDialog(resources.getString(R.string.please_wait))
                        updateDishDetails()
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
                Constants.showImageChooserActivity(this@EditDishActivity)

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
                            this@EditDishActivity,
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
        listMainMaterial.clear()
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
            this@EditDishActivity,
            resources.getString(R.string.dish_uploaded_success_message),
            Toast.LENGTH_SHORT
        ).show()
        setResult(RESULT_OK)
        finish()
    }

    //
//    /**
//     * A function to upload the selected product image to firebase cloud storage.
//     */
    private fun uploadDishImage() {

        showProgressDialog(resources.getString(R.string.please_wait))


        FirestoreClass().uploadImageToCloudStorage(
            this@EditDishActivity,
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

        updateDishDetails()
    }
    private fun updateDishDetails() {

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

        val dishHashMap = HashMap<String, Any>()

        // Here the field which are not editable needs no update. So, we will update user Mobile Number and Gender for now.

        // Here we get the text from editText and trim the space
        val dishTitle = binding.etDishTitle.text.toString().trim { it <= ' ' }
        //check if has change
        if (dishTitle != mDishDetails?.title) {

            dishHashMap[Constants.DISH_TITLE] = dishTitle
        }

        val dishDescription = binding.etDishDescription.text.toString().trim { it <= ' ' }
        //check if has change
        if (dishDescription != mDishDetails?.description) {
            dishHashMap[Constants.DISH_DESCRIPTION] = dishDescription
        }

        val dishServingSize = binding.etDishServingSize.text.toString().trim { it <= ' ' }
        //check if has change
        if (dishServingSize != mDishDetails?.serving_size) {
            dishHashMap[Constants.DISH_SERVING_SIZE] = dishServingSize
        }


        dishHashMap[Constants.DISH_MAIN_MATERIAL] = listMainMaterial


        dishHashMap[Constants.DISH_MATERIAL] = listMaterial


        dishHashMap[Constants.DISH_RECIPE] = listRecipe


        if (mSelectedImageFileURL.isNotEmpty()) {
            dishHashMap[Constants.IMAGE] = mSelectedImageFileURL
        }


        FirestoreClass().updateDishData(this, dishHashMap, mDishDetails?.dish_type!!, mDishDetails?.dish_id!!)
    }
    /**
     * A function to notify the success result and proceed further accordingly after updating the user details.
     */
    fun dishUpdateSuccess() {

        // Hide the progress dialog
        hideProgressDialog()

        Toast.makeText(
            this@EditDishActivity,
            resources.getString(R.string.dish_updated_success_message),
            Toast.LENGTH_SHORT
        ).show()
        setResult(RESULT_OK)
        finish()
    }
}