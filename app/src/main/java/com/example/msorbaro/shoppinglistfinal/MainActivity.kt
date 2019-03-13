package com.example.msorbaro.shoppinglistfinal


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import com.example.msorbaro.shoppinglistfinal.adapter.ShoppingAdapter
import com.example.msorbaro.shoppinglistfinal.data.AppDatabase
import com.example.msorbaro.shoppinglistfinal.data.Item
import com.example.msorbaro.shoppinglistfinal.touch.ItemTouchHelperCallback
import hu.aut.android.todorecyclerviewdemo.ItemDialog
import hu.aut.android.todorecyclerviewdemo.ItemDialog.ItemHandler
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), ItemHandler {
    private lateinit var shoppingAdapter: ShoppingAdapter

    companion object {
        val KEY_ITEM_TO_EDIT = "KEY_ITEM_TO_EDIT"
        var TOTAL = 0
    }
    private var editIndex: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initRecycleView()

        totalprice.text = TOTAL.toString()
    }

    private fun initRecycleView() {
        Thread {
            val items = AppDatabase.getInstance(
                    this@MainActivity
            ).itemDao().findAllItems()

            shoppingAdapter = ShoppingAdapter(
                    this@MainActivity,
                    items
            )
            runOnUiThread {
                recyclerShopping.adapter = shoppingAdapter
                val callback = ItemTouchHelperCallback(shoppingAdapter)
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(recyclerShopping)
            }
        }.start()
    }

    private fun showAddShopping() {
        ItemDialog().show(supportFragmentManager,
                "TAG_CREATE")
    }

    public fun showEditItemDialog(itemToEdit: Item, idx: Int) {
        editIndex = idx
        val editItemDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_ITEM_TO_EDIT, itemToEdit)
        editItemDialog.arguments = bundle

        editItemDialog.show(supportFragmentManager,
                "EDITITEMDIALOG")
    }

    override fun itemCreated(item: Item) = Thread {
        val itemId = AppDatabase.getInstance(
                this@MainActivity).itemDao().insertItem(item)

        item.itemId = itemId

        runOnUiThread {
            shoppingAdapter.addItem(item)
        }
    }.start()

    override fun itemUpdated(item: Item) {
        Thread {
            AppDatabase.getInstance(
                    this@MainActivity).itemDao().updateItem(item)

            runOnUiThread{
                shoppingAdapter.updateItem(item, editIndex)
            }
        }.start()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_new -> {
                showAddShopping()
            }
            R.id.action_delete -> {
                shoppingAdapter.deleteAllItems()
            }
            R.id.action_total -> {
                calculateChristmas()
            }
        }

        return true
    }

    fun calculateChristmas() {

        val c = Calendar.getInstance()
        var day = c.get(Calendar.DAY_OF_MONTH)
        day = 25 - day
        if (c.get(Calendar.MONTH) == 10){
            day = day +30
        }

        Toast.makeText(getBaseContext(), getString(R.string.beginngxmas) + " " + day + " " + getString(R.string.midxmas) + " " + TOTAL.toString() + " " + getString(R.string.endxmas), Toast.LENGTH_LONG).show()
    }


}



