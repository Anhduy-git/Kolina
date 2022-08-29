package com.example.recipeapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.recipeapp.R
import com.example.recipeapp.adapters.DishAdapter
import com.example.recipeapp.adapters.DishShareAdapter
import com.example.recipeapp.databinding.FragmentShareDishBinding
import com.example.recipeapp.firestore.FirestoreClass
import com.example.recipeapp.models.Dish
import com.example.recipeapp.ui.activities.AddDishActivity
import com.example.recipeapp.ui.activities.AddShareDishActivity
import com.example.recipeapp.ui.activities.SettingsActivity
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.GlideLoader
import java.io.IOException


class ShareDishFragment : BaseFragment() {

    private var _binding: FragmentShareDishBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var hasNewComment = false
    private var updateIndex = 0
    private var adapterDish: DishShareAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // If we want to use the option menu in fragment we need to add it.
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        getDishListFromFireStore()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentShareDishBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setup recycler view
        binding.rvDish.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvDish.setHasFixedSize(true)

        binding.swipeRefreshLayout.setOnRefreshListener {
            getDishListFromFireStore()
            binding.swipeRefreshLayout.isRefreshing = false
        }



        // TODO Step 7: Pass the third parameter value.
        // START

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share_dish_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.action_add_share_dish -> {

                // TODO Step 9: Launch the SettingActivity on click of action item.
                // START
                startActivityForResult(Intent(activity, AddShareDishActivity::class.java), Constants.ADD_DISH)
                // END

            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDishListFromFireStore(dishList: ArrayList<Dish>) {

        // Hide Progress dialog.
        hideProgressDialog()
        if (hasNewComment && updateIndex != -1) {
            adapterDish?.notifyItemChanged(updateIndex)
        }
        else {
            adapterDish =
                DishShareAdapter(requireActivity(), this@ShareDishFragment)
            //restore state of recycler view
            adapterDish?.submitList(dishList)
            binding.rvDish.adapter = adapterDish
        }
//        val recyclerViewState : Parcelable;
//        recyclerViewState = binding.rvDish.getLayoutManager()!!.onSaveInstanceState()!!;
//
//// Restore state
//        binding.rvDish.getLayoutManager()!!.onRestoreInstanceState(recyclerViewState);


    }
    fun getDishListFromFireStore() {
        // Show the progress dialog.
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getShareDishList(this@ShareDishFragment)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.ADD_DISH)   {
                getDishListFromFireStore()
            } else if (requestCode == Constants.ADD_COMMENT) {
                hasNewComment = true
                updateIndex = data?.getIntExtra(Constants.EXTRA_UPDATE_INDEX, -1)!!
                getDishListFromFireStore()
            }

        }
//
    }


}