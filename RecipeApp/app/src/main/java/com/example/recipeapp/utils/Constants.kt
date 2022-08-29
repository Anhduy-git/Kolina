package com.example.recipeapp.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import com.example.recipeapp.R
import com.example.recipeapp.models.DishType
import com.example.recipeapp.models.Material
import com.example.recipeapp.ui.fragments.SummaryDishFragment

object Constants {



    const val USERS_LIKE: String = "list_user_like"
    const val USERS_DISLIKE: String = "list_user_dislike"
    const val LIST_COMMENT: String = "list_comment"
    const val LIKE: String = "like"
    const val DISLIKE: String = "dislike"
    const val COMMENT: String = "comment"

    //collection in cloud firestore
    const val USERS: String = "users"

    const val DISHES_MAN: String = "dishes_man"
    const val DISHES_NGOT: String = "dishes_ngot"
    const val DISHES_TRAICAY: String = "dishes_traicay"
    const val DISHES_LAU: String = "dishes_lau"
    const val DISHES_NUONG: String = "dishes_nuong"
    const val DISHES_CHAY: String = "dishes_chay"
    const val DISHES_SHARE: String = "dishes_share"
    const val DISHES: String = "dishes"


    const val MYRECIPEAPP_PREFERENCES: String = "MyRecipeAppPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val PICK_IMAGE_REQUEST_CODE: Int = 1
    const val READ_STORAGE_PERMISSION_CODE: Int = 2
    const val ADD_DISH: Int = 3
    const val DISH_INFO: Int = 4
    const val ADD_COMMENT: Int = 5
    const val MATERIAL_LIST: Int = 6

    const val EXTRA_UPDATE_INDEX: String = "extra_update_index"



    const val MALE: String = "Male"
    const val FEMALE: String = "Female"
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE: String = "image"
    const val COMPLETED_PROFILE: String = "profileCompleted"

    const val DISH_TITLE: String = "title"
    const val DISH_USER_NAME: String = "user_name"
    const val DISH_DESCRIPTION: String = "description"
    const val DISH_SERVING_SIZE: String = "serving_size"
    const val DISH_TYPE: String = "dish_type"
    const val DISH_MAIN_MATERIAL: String = "main_material"
    const val DISH_MATERIAL: String = "material"
    const val DISH_RECIPE: String = "recipe"
    const val DISH_ID: String = "dish_id"




    //prefix of save name in firebase storage
    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val DISH_IMAGE: String = "Dish_Image"

    //user's ID of a product
    const val USER_ID: String = "user_id"

    //ID of a product
    const val EXTRA_DISH_ID: String = "extra_dish_id"

    const val EXTRA_DISH_TYPE: String = "dish_type"

    const val EXTRA_DISH: String = "extra_dish"

    val EXTRA_USER_ID: String = "extra_user_id"

    const val EXTRA_MATERIAL_LIST = "extra_material_list"

    const val EXTRA_VIEW_ONLY = "extra_view_only"


    val list_dish_type = arrayListOf<DishType>(
        DishType("Man", R.drawable.monman, R.color.dish_type_color_man),
        DishType("Ngot", R.drawable.monngot, R.color.dish_type_color_ngot)
       ,DishType("Nuong", R.drawable.monnuong, R.color.dish_type_color_nuong),
        DishType("Chay", R.drawable.monchay, R.color.dish_type_color_chay),
        DishType("Lau", R.drawable.lau, R.color.dish_type_color_lau),
        DishType("Trai Cay", R.drawable.traicay, R.color.dish_type_color_traicay)
    )
    val list_material = arrayListOf<Material>(
        Material("Bot Mi", R.drawable.bot_mi), Material("Fish", R.drawable.ca),
        Material("Candy", R.drawable.keo), Material("Vegetable", R.drawable.rau_cu),
        Material("Rau Thom", R.drawable.rau_thom), Material("Chicken", R.drawable.thit_ga),
        Material("Pork", R.drawable.thit_heo), Material("Fruits", R.drawable.trai_cay),
        Material("Eggs", R.drawable.trung)
    )


    fun showImageChooserFragment(fragment: Fragment) {
        //Intent for launching the image selection of phone storage
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        //Launches the image selection of phone storage using the constant code
        fragment.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun showImageChooserActivity(activity: Activity) {
        //Intent for launching the image selection of phone storage
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        //Launches the image selection of phone storage using the constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /**
     * A function to get the image file extension of the selected image.
     *
     * @param activity Activity reference.
     * @param uri Image file uri.
     */
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        /*
         * MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
         *
         * getSingleton(): Get the singleton instance of MimeTypeMap.
         *
         * getExtensionFromMimeType: Return the registered extension for the given MIME type.
         *
         * contentResolver.getType: Return the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}
