package com.example.recipe.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.Item.Recipe
import com.example.recipe.R
import com.example.recipe.Util.ImageUtil
import kotlinx.android.synthetic.main.item_main.view.*

class MainRvAdapter(private val aryRecipe: ArrayList<Recipe>) :
    RecyclerView.Adapter<MainRvAdapter.MainRvViewHolder>() {
    fun updateRecipe(alRecipe: ArrayList<Recipe>) {
        aryRecipe.clear()
        var x = 0
        while (x < alRecipe.size) {
            aryRecipe.add(alRecipe[x])
            x++
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRvViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_main,
            parent, false
        )
        return MainRvViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainRvViewHolder, position: Int) {
        holder.bindItems(aryRecipe[position])
    }

    override fun getItemCount(): Int {
        return aryRecipe.size
    }

    class MainRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(recipe: Recipe) {
            itemView.tv_recipe_name.text = recipe.sRecipeName
            val image =ImageUtil().convertStringToBitmap(recipe.sImage)
            itemView.iv_recipe.setImageBitmap(image)
        }
    }
}