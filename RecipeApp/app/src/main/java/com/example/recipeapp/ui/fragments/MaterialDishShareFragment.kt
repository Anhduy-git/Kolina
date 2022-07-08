package com.example.recipeapp.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.CustomViewListMaterialAdapter
import com.example.recipeapp.adapters.CustomViewListMaterialAdapterGet

import com.example.recipeapp.databinding.FragmentMaterialDishBinding
import com.example.recipeapp.databinding.FragmentMaterialDishShareBinding
import com.example.recipeapp.databinding.FragmentSummaryDishBinding

import com.example.recipeapp.models.Dish
import com.example.recipeapp.ui.activities.DishDetailsActivity
import com.example.recipeapp.ui.activities.DishDetailsShareActivity
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.GlideLoader


class MaterialDishShareFragment : Fragment() {
    private var _binding: FragmentMaterialDishShareBinding? = null
    private val binding get() = _binding!!
    private var mDishDetails: Dish? = null
    private lateinit var materialAdapter: CustomViewListMaterialAdapterGet
    private var listMaterial: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMaterialDishShareBinding.inflate(inflater, container, false)
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
            listMaterial = mDishDetails?.material!!
            binding.rvMaterial.layoutManager = LinearLayoutManager(requireActivity())
            materialAdapter = CustomViewListMaterialAdapterGet(requireActivity())
            binding.rvMaterial.adapter = materialAdapter
            materialAdapter.setData(listMaterial)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDishDetails = null
    }



}