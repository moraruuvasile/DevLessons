package com.example.devlessons.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.devlessons.Model.Category
import com.example.devlessons.R
import kotlinx.android.synthetic.main.main_list_item.view.*

class MainRecyAdap(val listCategories: List<Category>, val itemClick: (Category) -> Unit)
    : RecyclerView.Adapter<MainRecyAdap.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return ViewHolder(view, itemClick, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCategory(listCategories[position]/*, context*/)
    }


    override fun getItemCount() = listCategories.count()

    inner class ViewHolder(
        itemView: View,
        val itemClick: (Category) -> Unit,
        val context: Context) : RecyclerView.ViewHolder(itemView) {

        fun bindCategory(category: Category) {
            val resourceId = context.resources.getIdentifier(
                category.image,
                "drawable", context.packageName
            )

            itemView.categoryImage.setImageResource(resourceId)
            itemView.categoryName.text = category.title

            itemView.setOnClickListener { itemClick(category) }
        }
    }
}