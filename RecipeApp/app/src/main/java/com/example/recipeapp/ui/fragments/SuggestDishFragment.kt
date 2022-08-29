package com.example.recipeapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapters.MaterialCheckAdapter
import com.example.recipeapp.databinding.FragmentSuggestDishBinding
import com.example.recipeapp.models.Material
import com.example.recipeapp.ui.activities.ListMaterialSelectedActivity
import com.example.recipeapp.utils.AnimationUtil
import com.example.recipeapp.utils.Constants


class SuggestDishFragment : Fragment() {

    private var _binding: FragmentSuggestDishBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var listMaterial: ArrayList<Material> = ArrayList()
    private var listMaterialChoosed: ArrayList<Material> = ArrayList()
//    private lateinit var adapterChoosed: MaterialChoosedAdapter
    private lateinit var adapterChecked: MaterialCheckAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(false)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentSuggestDishBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listMaterial = Constants.list_material.map{it.copy()} as ArrayList<Material>



        //set adapter and display data of material
        binding.rvMaterial.layoutManager = GridLayoutManager(activity, 3)
        binding.rvMaterial.setHasFixedSize(true)
        adapterChecked = MaterialCheckAdapter(requireActivity(), object: MaterialCheckAdapter.ClickListener{
            override fun onClickListener(item: Material, imageAddToCart: ImageView) {
                listMaterialChoosed.add(item)
                AnimationUtil.translateAnimation(binding.viewAnimation, imageAddToCart, binding.viewEndAnimation, object :Animation.AnimationListener{
                    override fun onAnimationStart(animation: Animation?) {


                    }
                    override fun onAnimationEnd(animation: Animation?) {
                        binding.tvCartCounter.text = (listMaterialChoosed.size.toString())

                    }

                    override fun onAnimationRepeat(animation: Animation?) {



                    }

                })


            }

        })
        binding.rvMaterial.adapter = adapterChecked
        adapterChecked.setData(listMaterial)

        binding.fabCart.setOnClickListener {
            val intent: Intent = Intent(requireActivity(), ListMaterialSelectedActivity::class.java)
            intent.putExtra(Constants.EXTRA_MATERIAL_LIST, listMaterialChoosed)
            startActivityForResult(intent, Constants.MATERIAL_LIST)
        }





    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.MATERIAL_LIST)   {
                listMaterialChoosed = data?.getParcelableArrayListExtra<Material>(Constants.EXTRA_MATERIAL_LIST)!!
//                adapterChecked.setData(Constants.list_material)
                for (item in listMaterial) {
                    item.isSelected = listMaterialChoosed.contains(item)
                }
                adapterChecked.setData(listMaterial)
                binding.tvCartCounter.text = (listMaterialChoosed.size.toString())
            }
        }
//
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}