package com.example.msorbaro.shoppinglistfinal.data

import android.arch.persistence.room.*

@Dao
interface ItemsDAO {

    @Query("SELECT * FROM item")
    fun findAllItems(): List<Item>

    @Insert
    fun insertItem(item: Item) : Long

    @Delete
    fun deleteItem(item: Item)

    @Update
    fun updateItem(item: Item)
}
