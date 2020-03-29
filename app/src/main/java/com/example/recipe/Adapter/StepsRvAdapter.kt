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

class StepsRvAdapter(private val aryLstSteps: ArrayList<String>) :
    RecyclerView.Adapter<StepsRvAdapter.StepsViewHolder>() {
    private lateinit var context: Context

    init {
        if (aryLstSteps.size == 0) {
            aryLstSteps.add("")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.field,
            parent, false
        )
        context = parent.context
        return StepsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StepsViewHolder, position: Int) {
        val currentItem = aryLstSteps[position]
        val sPosition = (position + 1).toString()
        holder.tvNumber.setText(sPosition)
        holder.etField.setText(currentItem)
        var bDelete = false
        if (currentItem.isNotEmpty()) {
            holder.ivSymbol.setImageResource(R.drawable.ic_delete)
            bDelete = true
        } else {
            holder.ivSymbol.setImageResource(R.drawable.ic_add_recipe)
        }
        holder.ivSymbol.setOnClickListener {
            if (bDelete) {
                aryLstSteps.removeAt(position)
            } else {
                if (holder.etField.text.isNotEmpty()) {
                    val sField = holder.etField.text
                    aryLstSteps[position] = sField.toString()
                    aryLstSteps.add("")
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return aryLstSteps.size
    }

    class StepsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNumber: TextView = itemView.tv_number
        val etField: EditText = itemView.et_field
        val ivSymbol: ImageView = itemView.iv_symbol

    }
}