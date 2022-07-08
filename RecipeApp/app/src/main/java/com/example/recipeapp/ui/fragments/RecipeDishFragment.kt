package com.example.recipeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.CustomViewListMaterialAdapter
import com.example.recipeapp.adapters.CustomViewListRecipeAdapter
import com.example.recipeapp.adapters.CustomViewListRecipeAdapterGet
import com.example.recipeapp.databinding.FragmentRecipeDishBinding
import com.example.recipeapp.models.Dish
import com.example.recipeapp.ui.activities.DishDetailsActivity
import com.example.recipeapp.utils.Constants


class RecipeDishFragment : Fragment() {

    private var _binding: FragmentRecipeDishBinding? = null
    private val binding get() = _binding!!
    private var mDishDetails: Dish? = null
    private lateinit var recipeAdapter: CustomViewListRecipeAdapterGet
    private var listRecipe: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeDishBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //get dish ID from intent
        getDishDetails()

    }

    private fun getDishDetails() {
        mDishDetails = (activity as DishDetailsActivity).mDishDetails

        if (mDishDetails != null) {
            listRecipe = mDishDetails?.recipe!!
            binding.rvRecipe.layoutManager = LinearLayoutManager(requireActivity())
            recipeAdapter = CustomViewListRecipeAdapterGet(requireActivity())
            binding.rvRecipe.adapter = recipeAdapter
            recipeAdapter.setData(listRecipe)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDishDetails = null
    }


}