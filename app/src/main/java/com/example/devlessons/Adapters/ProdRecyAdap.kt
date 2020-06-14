package com.example.devlessons.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.devlessons.Model.Product
import com.example.devlessons.R
import kotlinx.android.synthetic.main.product_list_item.view.*

class ProdRecyAdap(
    val context: Context,
    val products: List<Product>,
    val itemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProdRecyAdap.ViewHolder>() {

    override fun getItemCount() = products.count()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindProduct(products[position], context)
    }

    inner class ViewHolder(itemView: View, val itemClick: (Product) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        fun bindProduct(product: Product, context: Context) {
            val resourceId =
                context.resources.getIdentifier(product.image, "drawable", context.packageName)

            itemView.productImage.setImageResource(resourceId)
            itemView.productName.text = product.title
            itemView.productPrice.text = product.price

            itemView.setOnClickListener { itemClick(product) }
        }
    }
}