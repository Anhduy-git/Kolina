package com.example.recipeapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.adapters.DishTypeAdapter
import com.example.recipeapp.adapters.MaterialListSelectedAdapter
import com.example.recipeapp.databinding.ActivityListMaterialSelectedBinding
import com.example.recipeapp.models.Material
import com.example.recipeapp.utils.Constants

class ListMaterialSelectedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListMaterialSelectedBinding
    private lateinit var materialList: ArrayList<Material>
    private var materialListSize: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListMaterialSelectedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvMaterialSelected.layoutManager = LinearLayoutManager(this@ListMaterialSelectedActivity)
        binding.rvMaterialSelected.setHasFixedSize(true)
        val adapter = MaterialListSelectedAdapter(this@ListMaterialSelectedActivity)
        binding.rvMaterialSelected.adapter = adapter

        if (intent.hasExtra(Constants.EXTRA_MATERIAL_LIST)) {
            materialList = intent.getParcelableArrayListExtra(Constants.EXTRA_MATERIAL_LIST)!!
            materialListSize = materialList.size
            adapter.setData(materialList)
        }

        binding.viewResultBtn.setOnClickListener {
            val intent: Intent = Intent(this@ListMaterialSelectedActivity, ResultDishActivity::class.java)
            intent.putExtra(Constants.EXTRA_MATERIAL_LIST, materialList)
            startActivity(intent)
        }
    }
    override fun finish() {
        if (materialList.size != materialListSize) {
            val intentIdx: Intent = Intent()
            intentIdx.putParcelableArrayListExtra(Constants.EXTRA_MATERIAL_LIST, materialList)
            setResult(RESULT_OK, intentIdx)
        }
        super.finish()
    }
}