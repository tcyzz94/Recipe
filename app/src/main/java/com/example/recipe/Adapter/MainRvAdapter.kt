package com.example.recipe.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.Database.DatabaseHandler
import com.example.recipe.Interface.rvItemCB
import com.example.recipe.Item.Recipe
import com.example.recipe.MainActivity
import com.example.recipe.R
import com.example.recipe.Util.ImageUtil
import kotlinx.android.synthetic.main.item_main.view.*

class MainRvAdapter(
    private val aryRecipe: ArrayList<Recipe>
) :
    RecyclerView.Adapter<MainRvAdapter.MainRvViewHolder>() {

    var dbHandler: DatabaseHandler? = null
    lateinit var pContext: Context
    internal lateinit var callback: rvItemCB
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
        pContext=parent.context
        return MainRvViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainRvViewHolder, position: Int) {
        holder.bindItems(aryRecipe[position])
        holder.itemView.rl_item_main.setOnLongClickListener {
            val picDialog = AlertDialog.Builder(holder.itemView.context)
            picDialog.setTitle("Delete?")
            val picDialogItems = arrayOf("No", "Yes")
            picDialog.setItems(picDialogItems) { dialog, which ->
                when (which) {
                    0 -> return@setItems
                    1 -> {
                        dbHandler = DatabaseHandler(holder.itemView.context)
                        dbHandler!!.deleteTask(aryRecipe[position].id)
                        aryRecipe.removeAt(position)
                        notifyDataSetChanged()
                    }
                }
            }
            picDialog.show()
            return@setOnLongClickListener true
        }

        holder.itemView.rl_item_main.setOnClickListener {
            callback.passDataCallBack(position)
        }
    }

    override fun getItemCount(): Int {
        return aryRecipe.size
    }

    fun setCallBackListener(listen: rvItemCB) {
        callback=listen
    }

    class MainRvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(recipe: Recipe) {
            itemView.tv_recipe_name.text = recipe.sRecipeName
            val image = ImageUtil().convertStringToBitmap(recipe.sImage)
            itemView.iv_recipe.setImageBitmap(image)
        }
    }

}