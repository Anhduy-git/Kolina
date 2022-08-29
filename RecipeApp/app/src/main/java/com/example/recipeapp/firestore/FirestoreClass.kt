package com.example.recipeapp.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.recipeapp.models.*
import com.example.recipeapp.ui.activities.*
import com.example.recipeapp.ui.fragments.ShareDishFragment
import com.example.recipeapp.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firestore.v1.DocumentTransform


class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    /**
     * A function to make an entry of the registered user in the FireStore database.
     */
    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }
    /**
     * A function to get the user id of current logged user.
     */
    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }
    /**
     * A function to get the logged user details from from FireStore Database.
     */
    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->


                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!

                //store userdata in share preference
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYRECIPEAPP_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                //Key: logged_in_username
                //value: Anh Duy
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                // TODO Step 6: Pass the result to the Login Activity.
                // START
                when (activity) {
                    is LoginActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is CommentActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is AddDishActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                    is AddShareDishActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                    is CommentActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddDishActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddShareDishActivity -> {
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }
    fun getUserDetailsById(activity: Activity, userId: String) {

        // Here we pass the collection name from which we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(userId)
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                // Here we have received the document snapshot which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!


                // TODO Step 6: Pass the result to the Login Activity.
                // START
                when (activity) {
                    is UserProfileShareActivity -> activity.userDetailsSuccess(user)
                }
                // END
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is UserProfileShareActivity -> {
                        activity.hideProgressDialog()
                    }
//
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }
    // START
    /**
     * A function to update the user profile data into the database.
     *
     * @param activity The activity is used for identifying the Base activity to which the result is passed.
     * @param userHashMap HashMap of fields which are to be updated.
     */
    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        // Collection Name
        mFireStore.collection(Constants.USERS)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(getCurrentUserID())
            // A HashMap of fields which are to be updated.
            .update(userHashMap)
            .addOnSuccessListener {

                // TODO Step 9: Notify the success result to the base activity.
                // START
                // Notify the success result.
                when (activity) {
                    is UserProfileActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userProfileUpdateSuccess()
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is UserProfileActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }

    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String) {


        //getting the storage reference
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(
                activity,
                imageFileURI
            )
        )


        //adding the file to reference
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                // The image upload is success

                // Get the downloadable url from the task snapshot
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->

                        // TODO Step 8: Pass the success result to base class.
                        // START
                        // Here call a function of base activity for transferring the result to it.
                        when (activity) {
                            is UserProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is AddDishActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is EditDishActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                            is AddShareDishActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }


                        }
                        // END
                    }
            }
            .addOnFailureListener { exception ->

                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddDishActivity -> {
                        activity.hideProgressDialog()
                    }
                    is EditDishActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddShareDishActivity -> {
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
    /**
     * A function to make an entry of the user's product in the cloud firestore database.
     */

    fun uploadDishDetails(activity: Activity, dish: Dish) {
        val newDishDocument:DocumentReference = mFireStore.collection(Constants.DISHES)
            .document()
        dish.dish_id = newDishDocument.id
        newDishDocument
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(dish, SetOptions.merge())
            .addOnSuccessListener {
                when (activity) {
                    is AddDishActivity  -> activity.dishUploadSuccess()
                    is EditDishActivity -> activity.dishUploadSuccess()
                    is DishDetailsShareActivity -> activity.dishCloneSuccess()
                }
                // Here call a function of base activity for transferring the result to it.

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is AddDishActivity  -> activity.hideProgressDialog()
                    is EditDishActivity -> activity.hideProgressDialog()
                    is DishDetailsShareActivity -> activity.hideProgressDialog()
                }



                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }

    fun uploadShareDishDetails(activity: Activity, dish: Dish) {
        val newDishDocument:DocumentReference = mFireStore.collection(Constants.DISHES_SHARE)
            .document()
        dish.dish_id = newDishDocument.id
        newDishDocument
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(dish, SetOptions.merge())

            .addOnSuccessListener {
                uploadLike(LikePost(newDishDocument.id, ArrayList()))
                uploadDislike(DislikePost(newDishDocument.id, ArrayList()))
                uploadComment(CommentPost(newDishDocument.id, ArrayList()))
                when (activity) {
                    is AddShareDishActivity -> activity.dishUploadSuccess(newDishDocument.id)
                    is DishDetailsActivity -> activity.dishUploadSuccess()
                }
            }
            .addOnFailureListener { e ->
                when (activity) {

                    is AddShareDishActivity -> activity.hideProgressDialog()
                }



                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }

    }

    /**
     * A function to get the products list from cloud firestore.
     *
     * @param fragment The fragment is passed as parameter as the function is called from fragment and need to the success result.
     */
    fun getDishList(activity: Activity, dishType: String) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.DISHES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.DISH_TYPE, dishType)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->


                // Here we have created a new instance for Products ArrayList.
                val dishList: ArrayList<Dish> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val dish = i.toObject(Dish::class.java)
                    dish!!.dish_id = i.id

                    dishList.add(dish)
                }

                when (activity) {
                    is DishMenuActivity -> {
                        activity.successDishListFromFireStore(dishList)
                    }

                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (activity) {
                    is DishMenuActivity -> {
                        activity.hideProgressDialog()
                    }

                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }
    fun getAllDishList(activity: Activity) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.DISHES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->


                // Here we have created a new instance for Products ArrayList.
                val dishList: ArrayList<Dish> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val dish = i.toObject(Dish::class.java)
                    dish!!.dish_id = i.id

                    dishList.add(dish)
                }

                when (activity) {

                    is ResultDishActivity -> {
                        activity.successDishListFromFireStore(dishList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (activity) {

                    is ResultDishActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }

    fun getShareDishList(fragment: Fragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.DISHES_SHARE).orderBy("date", Query.Direction.DESCENDING)
            .get()
            // Will get the documents snapshots.
            .addOnSuccessListener { document ->


                // Here we have created a new instance for Products ArrayList.
                val dishList: ArrayList<Dish> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val dish = i.toObject(Dish::class.java)
                    dish!!.dish_id = i.id

                    dishList.add(dish)
                }

                when (fragment) {
                    is ShareDishFragment -> {
                        fragment.successDishListFromFireStore(dishList)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is ShareDishFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }
    fun updateDishData(activity: Activity, dishHashMap: HashMap<String, Any>, dishType: String, dishId: String) {
        // Collection Name
        mFireStore.collection(Constants.DISHES)
            // Document ID against which the data to be updated. Here the document id is the current logged in user id.
            .document(dishId)
            // A HashMap of fields which are to be updated.
            .update(dishHashMap)
            .addOnSuccessListener {

                // TODO Step 9: Notify the success result to the base activity.
                // START
                // Notify the success result.
                when (activity) {
                    is EditDishActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.dishUpdateSuccess()
                    }
                }
                // END
            }
            .addOnFailureListener { e ->

                when (activity) {
                    is EditDishActivity -> {
                        // Hide the progress dialog if there is any error. And print the error in log.
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
    fun uploadLike(likePost: LikePost) {

        mFireStore.collection(Constants.LIKE)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(likePost, SetOptions.merge())
//            .addOnSuccessListener {
//
//                // Here call a function of base activity for transferring the result to it.
//
//            }
//            .addOnFailureListener { e ->
//
//
//            }
    }
    fun isLiked(dishId: String, functionCallBack: FunctionCallBackIsLiked) {

        mFireStore.collection(Constants.LIKE)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->
                var liked = false
                val likePost =  document.documents.get(0).toObject(LikePost::class.java)!!
                if (likePost.list_user_like?.contains(getCurrentUserID())!!){
                    liked = true
                }
                functionCallBack.onCallbackIsLiked(liked)
            }
            .addOnFailureListener {

            }

    }
    fun updateLike(dishId: String){

        mFireStore.collection(Constants.LIKE)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->
                document.documents.get(0).reference.update(Constants.USERS_LIKE, FieldValue.arrayUnion(getCurrentUserID()))
            }
            .addOnFailureListener {
            }
    }
    fun updateNotLike(dishId: String){

        mFireStore.collection(Constants.LIKE)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->
                document.documents.get(0).reference.update(Constants.USERS_LIKE, FieldValue.arrayRemove(getCurrentUserID()))
            }
            .addOnFailureListener {
            }
    }
    fun getNumUsersLike(dishId: String, functionCallBack: FunctionCallBackNumLiked) {
        mFireStore.collection(Constants.LIKE)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->

                val likePost =  document.documents.get(0).toObject(LikePost::class.java)!!
                val numLiked = likePost.list_user_like?.size
                functionCallBack.onCallbackNumLiked(numLiked!!)
            }
            .addOnFailureListener {

            }
    }

    fun uploadDislike(dislikePost: DislikePost) {

        mFireStore.collection(Constants.DISLIKE)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(dislikePost, SetOptions.merge())
//            .addOnSuccessListener {
//
//                // Here call a function of base activity for transferring the result to it.
//
//            }
//            .addOnFailureListener { e ->
//
//
//            }
    }
    fun isDisliked(dishId: String, functionCallBack: FunctionCallBackIsDisliked) {

        mFireStore.collection(Constants.DISLIKE)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->
                var disliked = false
                val dislikePost =  document.documents.get(0).toObject(DislikePost::class.java)!!
                if (dislikePost.list_user_dislike?.contains(getCurrentUserID())!!){
                    disliked = true
                }
                functionCallBack.onCallbackIsDisliked(disliked)
            }
            .addOnFailureListener {

            }

    }
    fun updateDislike(dishId: String){

        mFireStore.collection(Constants.DISLIKE)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->
                document.documents.get(0).reference.update(Constants.USERS_DISLIKE, FieldValue.arrayUnion(getCurrentUserID()))
            }
            .addOnFailureListener {
            }
    }
    fun updateNotDislike(dishId: String){

        mFireStore.collection(Constants.DISLIKE)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->
                document.documents.get(0).reference.update(Constants.USERS_DISLIKE, FieldValue.arrayRemove(getCurrentUserID()))
            }
            .addOnFailureListener {
            }
    }
    fun getNumUsersDislike(dishId: String, functionCallBack: FunctionCallBackNumDisliked) {
        mFireStore.collection(Constants.DISLIKE)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->

                val dislikePost =  document.documents.get(0).toObject(DislikePost::class.java)!!
                val numDisliked = dislikePost.list_user_dislike?.size
                functionCallBack.onCallbackNumDisliked(numDisliked!!)
            }
            .addOnFailureListener {

            }
    }
    fun uploadComment(commentPost: CommentPost) {

        mFireStore.collection(Constants.COMMENT)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(commentPost, SetOptions.merge())
//            .addOnSuccessListener {
//
//                // Here call a function of base activity for transferring the result to it.
//
//            }
//            .addOnFailureListener { e ->
//
//
//            }
    }
    fun updateComment(activity: CommentActivity, dishId: String, comment: Comment){

        mFireStore.collection(Constants.COMMENT)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->
                document.documents.get(0).reference.update(Constants.LIST_COMMENT, FieldValue.arrayUnion(comment))
                activity.successAddComment()

            }
            .addOnFailureListener {
            }
    }
    fun getCommentList(activity: Activity, dishId: String) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.COMMENT)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->
                val commentPost = document.documents[0].toObject(CommentPost::class.java)!!


                when (activity) {
                    is CommentActivity -> {
                        activity.successCommentListFromFireStore(commentPost.list_comment!!)
                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (activity) {
                    is CommentActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }
    fun getNumComment(dishId: String, functionCallBack: FunctionCallBackNumComment) {
        mFireStore.collection(Constants.COMMENT)
            .whereEqualTo(Constants.DISH_ID, dishId)
            .get()
            .addOnSuccessListener { document ->

                val commentPost =  document.documents.get(0).toObject(CommentPost::class.java)!!
                val numComment = commentPost.list_comment?.size
                functionCallBack.onCallbackNumComment(numComment!!)
            }
            .addOnFailureListener {

            }
    }

    //    /**
//     * A function to get the product details based on the product id.
//     */
//    fun getDishDetails(fragment: SummaryDishFragment, dishId: String, typeDish: String) {
//        Log.d("testhaha", "yes")
//
//        // The collection name for PRODUCTS
//        mFireStore.collection(typeDish)
//            .document(dishId)
//            .get() // Will get the document snapshots.
//            .addOnSuccessListener { document ->
//
//                // Here we get the product details in the form of document.
//
//
//                // Convert the snapshot to the object of Product data model class.
//                val dish = document.toObject(Dish::class.java)!!
//
//                fragment.dishDetailsSuccess(dish)
//            }
//            .addOnFailureListener { e ->
//
//                // Hide the progress dialog if there is an error.
//                fragment.hideProgressDialog()
//
//            }
//    }
//    /**
//     * A function to get the dashboard items list. The list will be an overall items list, not based on the user's id.
//     */
//    fun getDashboardItemsList(fragment: DashboardFragment) {
//        // The collection name for PRODUCTS
//        mFireStore.collection(Constants.PRODUCTS)
//            .get() // Will get the documents snapshots.
//            .addOnSuccessListener { document ->
//
//                // Here we get the list of boards in the form of documents.
//                Log.e(fragment.javaClass.simpleName, document.documents.toString())
//
//                // Here we have created a new instance for Products ArrayList.
//                val productsList: ArrayList<Product> = ArrayList()
//
//                // A for loop as per the list of documents to convert them into Products ArrayList.
//                for (i in document.documents) {
//
//                    val product = i.toObject(Product::class.java)!!
//                    product.product_id = i.id
//                    productsList.add(product)
//                }
//
//                // Pass the success result to the base fragment.
//                fragment.successDashboardItemsList(productsList)
//            }
//            .addOnFailureListener { e ->
//                // Hide the progress dialog if there is any error which getting the dashboard items list.
//                fragment.hideProgressDialog()
//                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.", e)
//            }
//    }
//    /**
//     * A function to delete the product from the cloud firestore.
//     */
    fun deleteDish(activity: Activity, dishId: String) {

        mFireStore.collection(Constants.DISHES)
            .document(dishId)
            .delete()
            .addOnSuccessListener {

                // TODO Step 4: Notify the success result to the base class.
                // START
                // Notify the success result to the base class.
                when(activity) {
                    is DishDetailsActivity -> activity.dishDeleteSuccess()

                }

                // END
            }
            .addOnFailureListener { e ->

                // Hide the progress dialog if there is an error.
                when(activity) {
                    is DishDetailsActivity -> activity.hideProgressDialog()

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }
}