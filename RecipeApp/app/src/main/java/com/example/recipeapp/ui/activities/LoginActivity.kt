package com.example.recipeapp.ui.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityLoginBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.User
import com.example.recipeapp.utils.Constants
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.*


class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //hide status bar
        @Suppress("DEPRECATION")
        //Android R: Android 11 (API/SDK: 30)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        auth = FirebaseAuth.getInstance()

//        // TODO Step 4: Assign the click listener to the views which we have implemented.
//        // START
//        // Click event assigned to Forgot Password text.
        binding.tvForgotPassword.setOnClickListener(this)
//        // Click event assigned to Login button.
        binding.btnLogin.setOnClickListener(this)
//        // Click event assigned to Register text.
        binding.tvRegister.setOnClickListener(this)





    }
    // In Login screen the clickable components are Login Button, ForgotPassword text and Register Text.
    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {

                    logInRegisteredUser()
                }

                R.id.tv_register -> {
                    // Launch the register screen when the user clicks on the text.
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }
    // END

    // TODO Step 5: Create a function to validate the login details.
    // START
    /**
     * A function to validate the login entries of a user.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.etPassword.text.toString().trim { it <= ' ' }
            //Log in
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->


                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            FirestoreClass().getUserDetails(this@LoginActivity)
                        } else {
                            hideProgressDialog()
                            // If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }

    }

    /**
     * A function to notify user that logged in success and get the user details from the FireStore database after authentication.
     */
    fun userLoggedInSuccess(user: User) {

//
        hideProgressDialog()




        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else {
            // Redirect the user to Main Screen after log in.
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }

//    private fun setUpForGoogleLogin() {
//        oneTapClient = Identity.getSignInClient(this)
//        signInRequest = BeginSignInRequest.builder()
////            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
////                .setSupported(true)
////                .build())
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.your_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(false)
//                    .build())
//            // Automatically sign in when exactly one credential is retrieved.
//            .setAutoSelectEnabled(true)
//            .build()
//    }
//
//    private fun loginWithGoogle() {
//        oneTapClient.beginSignIn(signInRequest)
//            .addOnSuccessListener(this) { result ->
//                try {
//                    startIntentSenderForResult(
//                        result.pendingIntent.intentSender, REQ_ONE_TAP,
//                        null, 0, 0, 0, null)
//                } catch (e: IntentSender.SendIntentException) {
//                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
//                }
//            }
//            .addOnFailureListener(this) { e ->
//                // No saved credentials found. Launch the One Tap sign-up flow, or
//                // do nothing and continue presenting the signed-out UI.
//                Log.d(TAG, e.localizedMessage)
//            }
//
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            Constants.REQ_ONE_TAP -> {
//                try {
//                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
//                    val idToken = credential.googleIdToken
//
//                    when {
//
//                        idToken != null -> {
//                            // Got an ID token from Google. Use it to authenticate
//                            // with Firebase.
//                           val authCredential = GoogleAuthProvider.getCredential(idToken, null)
//                            FirebaseAuth.getInstance().signInWithCredential(authCredential)
//                                .addOnCompleteListener(this) { task ->
//                                    if (task.isSuccessful) {
//
//                                        // Sign in success, update UI with the signed-in user's information
//                                        Log.d(TAG, "signInWithCredential:success")
//
////                                        val user = auth.currentUser
//
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        Log.w(TAG, "signInWithCredential:failure", task.exception)
//                                    }
//                                }
//                        }
//                        else -> {
//                            // Shouldn't happen.
//                        }
//                    }
//
//                } catch (e: ApiException) {
//                    // ...
//                }
//            }
//        }
//    }




}