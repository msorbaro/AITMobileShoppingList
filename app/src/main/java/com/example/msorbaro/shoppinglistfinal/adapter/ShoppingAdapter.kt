package com.example.msorbaro.shoppinglistfinal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.msorbaro.shoppinglistfinal.R
import android.support.v7.widget.RecyclerView;
import com.example.msorbaro.shoppinglistfinal.MainActivity
import com.example.msorbaro.shoppinglistfinal.data.AppDatabase
import com.example.msorbaro.shoppinglistfinal.data.Item
import com.example.msorbaro.shoppinglistfinal.touch.ItemTochHelperAdapter
import kotlinx.android.synthetic.main.itemrow.view.*
import java.util.*

class ShoppingAdapter : RecyclerView.Adapter<ShoppingAdapter.ViewHolder>, ItemTochHelperAdapter {


    var shoppingList = mutableListOf<Item>()


    val context : Context

    constructor(context: Context, items: List<Item>) : super() {
        this.context = context
        this.shoppingList.addAll(items)
        MainActivity.Companion.TOTAL = calculateTotal()
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.itemrow, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = shoppingList[position]
        holder.nameitem.text = item.itemName
        holder.cbDone.isChecked = item.done
        holder.descriptionitem.text = item.itemDescription
        holder.priceitem.text = item.itemPrice
        holder.catagoryitem.text = item.itemCatagory
        holder.btnDelete.setOnClickListener { deleteItem(holder.adapterPosition) }
        holder.btnEdit.setOnClickListener { (context as MainActivity).showEditItemDialog(item, holder.adapterPosition) }

        holder.cbDone.setOnClickListener {
            item.done = holder.cbDone.isChecked
            if(item.itemCatagory == context.getString(R.string.christmas) && !item.done) { MainActivity.Companion.TOTAL = MainActivity.Companion.TOTAL + 1 }
            else { MainActivity.Companion.TOTAL = MainActivity.Companion.TOTAL - 1 }
            Thread { AppDatabase.getInstance( context ).itemDao().updateItem(item) }.start()
        }
        
        if(holder.catagoryitem.text == context.getString(R.string.food)){ holder.imageV.setImageResource(R.drawable.food) }
        else if (holder.catagoryitem.text == context.getString(R.string.clothes)){ holder.imageV.setImageResource(R.drawable.clothes) }
        else if (holder.catagoryitem.text == context.getString(R.string.christmas)){ holder.imageV.setImageResource(R.drawable.christmas) }
        else { holder.imageV.setImageResource(R.drawable.other) }
    }

    private fun deleteItem(adapterPosition: Int) {
        Thread {
            if(shoppingList[adapterPosition].itemCatagory == context.getString(R.string.christmas)){
                MainActivity.Companion.TOTAL = MainActivity.Companion.TOTAL - 1 }
            
            AppDatabase.getInstance(
                    context).itemDao().deleteItem(shoppingList[adapterPosition])

            shoppingList.removeAt(adapterPosition)

            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            } }.start()
    }

     fun deleteAllItems(){
        for (item in shoppingList){
            deleteItem(0)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val nameitem = itemView.name
        val cbDone = itemView.cbDone
        val descriptionitem = itemView.descriptions
        val priceitem = itemView.price
        val catagoryitem = itemView.catagory
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val imageV = itemView.imageView
    }


    fun addItem(item: Item) {
        if (item.itemCatagory == context.getString(R.string.christmas) && !item.done)
        {
            MainActivity.Companion.TOTAL = MainActivity.Companion.TOTAL + 1
        }
        shoppingList.add(0, item)

        notifyItemInserted(0)
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(shoppingList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun calculateTotal() : Int {
        var total = 0
        for (item in shoppingList) {
            if(item.itemCatagory == context.getString(R.string.christmas) && !item.done)
            total += 1
        }

        return total
    }


    fun updateItem(item: Item, editIndex: Int) {
        MainActivity.Companion.TOTAL = calculateTotal()

        shoppingList[editIndex] = item
        notifyItemChanged(editIndex)
    }

}