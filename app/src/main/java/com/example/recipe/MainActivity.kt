package com.example.recipe


import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private val mAryLstRecipeType = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAryLstRecipeType.add("All")
        mAryLstRecipeType.add("Chinese")
        mAryLstRecipeType.add("Malay")
        mAryLstRecipeType.add("Indian")

        val fabAddRecipe =findViewById<FloatingActionButton>(R.id.fab_add_recipe)
        val spRecipeType = findViewById<Spinner>(R.id.sp_recipe_type)
        val adpRecipeType = ArrayAdapter(this, R.layout.spinner_item, mAryLstRecipeType)
        spRecipeType.adapter = adpRecipeType

        fabAddRecipe.setOnClickListener{
            val intent = Intent(this,AddRecipeActivity::class.java)
            startActivity(intent)
        }
    }

}
