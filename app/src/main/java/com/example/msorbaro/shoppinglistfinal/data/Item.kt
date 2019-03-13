package com.example.msorbaro.shoppinglistfinal.data
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item")
data class Item(
        @PrimaryKey(autoGenerate = true) var itemId: Long?,
        @ColumnInfo(name = "done") var done: Boolean,
        @ColumnInfo(name = "itemName") var itemName: String,
        @ColumnInfo(name = "itemDescription") var itemDescription: String,
        @ColumnInfo(name = "itemPrice") var itemPrice: String,
        @ColumnInfo(name = "itemCatagory") var itemCatagory: String
) : Serializable
