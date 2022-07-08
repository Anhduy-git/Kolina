package com.example.recipeapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.DishTypeAdapter
import com.example.recipeapp.databinding.FragmentMenuBinding
import com.example.recipeapp.models.DishType
import com.example.recipeapp.ui.activities.SettingsActivity
import com.example.recipeapp.utils.Constants


class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set adapter and display data
        binding.rvDishType.layoutManager = GridLayoutManager(activity, 2)
        binding.rvDishType.setHasFixedSize(true)
        val adapter = DishTypeAdapter(requireActivity())
        binding.rvDishType.adapter = adapter
        adapter.setData(Constants.list_dish_type)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.action_settings -> {

                // TODO Step 9: Launch the SettingActivity on click of action item.
                // START
                startActivity(Intent(activity, SettingsActivity::class.java))
                // END
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
//    private fun loadDishTypeData() {
//        listDishType.add(DishType("Man", R.drawable.monman))
//        listDishType.add(DishType("Ngot", R.drawable.monngot))
//        listDishType.add(DishType("Nuong", R.drawable.monnuong))
//        listDishType.add(DishType("Chay", R.drawable.monchay))
//        listDishType.add(DishType("Lau", R.drawable.lau))
//        listDishType.add(DishType("Trai Cay", R.drawable.traicay))
//    }
}