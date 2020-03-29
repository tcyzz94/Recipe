package com.example.recipe.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipe.R
import kotlinx.android.synthetic.main.field.view.*

class IngredientsRvAdapter(
    private val alIngredients: ArrayList<String>
) :
    RecyclerView.Adapter<IngredientsRvAdapter.IngredientViewHolder>() {
    private lateinit var context: Context
    init {
        if(alIngredients.size==0){
            alIngredients.add("")
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.field,
            parent, false
        )
        context = parent.context
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentItem = alIngredients[position]
        val sPosition = (position + 1).toString()
        holder.tvNumber.setText(sPosition)
        holder.etField.setText(currentItem)
        var bDelete = false
        if (currentItem.isNotEmpty()) {
            holder.ivSymbol.setImageResource(R.drawable.ic_delete)
            bDelete = true
        }else{
            holder.ivSymbol.setImageResource(R.drawable.ic_add_recipe)
        }
        holder.ivSymbol.setOnClickListener {
            if(bDelete){
                alIngredients.removeAt(position)
            }else{
                if (holder.etField.text.isNotEmpty()) {
                    val sField = holder.etField.text
                    alIngredients[position] = sField.toString()
                    alIngredients.add("")
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return alIngredients.size
    }

    class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.tv_number
        val etField: EditText = itemView.et_field
        val ivSymbol: ImageView = itemView.iv_symbol

    }
}