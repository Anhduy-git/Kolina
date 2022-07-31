package com.example.recipeapp.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentSummaryDishBinding
import com.example.recipeapp.databinding.FragmentSummaryDishShareBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.Dish
import com.example.recipeapp.ui.activities.DishDetailsActivity
import com.example.recipeapp.ui.activities.DishDetailsShareActivity
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.GlideLoader
import java.io.IOException


class SummaryDishShareFragment : BaseFragment() {

    private var _binding: FragmentSummaryDishShareBinding? = null
    private val binding get() = _binding!!
    private var mDishDetails: Dish? = null
    private var listMainMaterial: ArrayList<String>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSummaryDishShareBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get dish ID from intent
        getDishDetails()


    }

    private fun getDishDetails() {
        mDishDetails = (activity as DishDetailsShareActivity).mDishDetailsShare


        if (mDishDetails != null) {
            // Populate the product details in the UI.
            if (mDishDetails!!.image != null) {
                GlideLoader(requireActivity()).loadDishPicture(
                    mDishDetails?.image!!,
                    binding.ivDishImage
                )
            }

            binding.etDishTitle.setText(mDishDetails!!.title)
            binding.etDishDescription.setText(mDishDetails!!.description)
            binding.etDishServingSize.setText(mDishDetails!!.serving_size)
            listMainMaterial = mDishDetails!!.main_material
            val dishType: String = when(mDishDetails?.dish_type) {
                Constants.DISHES_MAN -> "Man"
                Constants.DISHES_TRAICAY -> "Trai Cay"
                Constants.DISHES_NGOT -> "Ngot"
                Constants.DISHES_CHAY -> "Chay"
                Constants.DISHES_LAU -> "Lau"
                Constants.DISHES_NUONG -> "Nuong"
                else -> ""
            }
            binding.tvDishType.setText(resources.getString(R.string.dish_type_variable, dishType))


            //set checked box
            var idx = 0
            if (listMainMaterial != null && listMainMaterial!!.size > 0) {
                for (item in binding.llListMainMaterial.children) {
                    if (item is CheckBox) {
                        if (item.text.toString() == listMainMaterial!![idx]) {
                            item.isChecked = true
                            idx++
                            if (idx >= listMainMaterial!!.size) {
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDishDetails = null
        listMainMaterial = null
    }





}