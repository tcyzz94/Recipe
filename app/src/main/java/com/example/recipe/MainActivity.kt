package com.example.recipe

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.recipe.Adapter.MainRvAdapter
import com.example.recipe.Database.DatabaseHandler
import com.example.recipe.Fragment.DetailFragment
import com.example.recipe.Interface.rvItemCB
import com.example.recipe.Item.Recipe
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, rvItemCB {
    private val mAryLstRecipeType = ArrayList<String>()
    var dbHandler: DatabaseHandler? = null
    private var aryLstRecipe = ArrayList<Recipe>()
    private lateinit var rvMainRecipe: RecyclerView
    private lateinit var mSwipeRecyclerView: SwipeRefreshLayout
    private lateinit var adpMainRvAdapter: MainRvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAryLstRecipeType.add("All")
        mAryLstRecipeType.add("Chinese")
        mAryLstRecipeType.add("Malay")
        mAryLstRecipeType.add("Indian")

        val fabAddRecipe = findViewById<FloatingActionButton>(R.id.fab_add_recipe)
        val spRecipeType = findViewById<Spinner>(R.id.sp_recipe_type)
        val adpRecipeType = ArrayAdapter(this, R.layout.spinner_item, mAryLstRecipeType)
        spRecipeType.adapter = adpRecipeType
        dbHandler = DatabaseHandler(this)
        rvMainRecipe = findViewById(R.id.recycler_view)
        adpMainRvAdapter = MainRvAdapter(aryLstRecipe)
        adpMainRvAdapter.setCallBackListener(this)
        rvMainRecipe.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvMainRecipe.adapter = adpMainRvAdapter

        mSwipeRecyclerView = findViewById(R.id.swipe)
        mSwipeRecyclerView.setOnRefreshListener(this)

        fabAddRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onRefresh() {
        updateList()
        mSwipeRecyclerView.isRefreshing = false
    }

    fun updateList() {
        aryLstRecipe = (dbHandler as DatabaseHandler).task()
        adpMainRvAdapter.updateRecipe(aryLstRecipe)
    }

    override fun passDataCallBack(recipe: Int) {
        val fragment = DetailFragment()
        val data = Bundle()
        data.putSerializable("recipe",aryLstRecipe[recipe])
        fragment.arguments=data
        val ft:FragmentTransaction =supportFragmentManager.beginTransaction()
        ft.replace(R.id.fragmentContainer,fragment)
        ft.addToBackStack("detail")
        ft.commit()
    }


}
