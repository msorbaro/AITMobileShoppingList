package com.example.msorbaro.shoppinglistfinal.touch

import android.view.ViewGroup
import com.example.msorbaro.shoppinglistfinal.adapter.ShoppingAdapter

interface ItemTochHelperAdapter {
    fun onDismissed(position: Int)
    fun onItemMoved(fromPosition: Int, toPosition: Int)
}