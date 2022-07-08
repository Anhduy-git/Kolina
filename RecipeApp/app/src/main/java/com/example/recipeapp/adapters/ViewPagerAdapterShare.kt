package com.example.recipeapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.recipeapp.ui.fragments.*

class ViewPagerAdapterShare(fragmentManager: FragmentManager, lifeCycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifeCycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when (position) {
            0 -> {
                SummaryDishShareFragment()
            }
            1 -> {
                MaterialDishShareFragment()
            }
            2 -> {
                RecipeDishShareFragment()
            }
            else -> {
                Fragment()
            }
        }
    }

}