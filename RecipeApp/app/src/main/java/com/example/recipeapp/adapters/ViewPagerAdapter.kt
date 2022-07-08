package com.example.recipeapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.recipeapp.ui.fragments.MaterialDishFragment
import com.example.recipeapp.ui.fragments.RecipeDishFragment
import com.example.recipeapp.ui.fragments.SummaryDishFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifeCycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifeCycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
       return when (position) {
            0 -> {
                SummaryDishFragment()
            }
            1 -> {
                MaterialDishFragment()
            }
            2 -> {
                RecipeDishFragment()
            }
            else -> {
                Fragment()
            }
        }
    }

}