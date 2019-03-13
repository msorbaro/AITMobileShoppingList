package hu.aut.android.todorecyclerviewdemo

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import com.example.msorbaro.shoppinglistfinal.MainActivity
import com.example.msorbaro.shoppinglistfinal.R
import com.example.msorbaro.shoppinglistfinal.data.Item
import kotlinx.android.synthetic.main.additemlayout.view.*
import kotlinx.android.synthetic.main.itemrow.view.*
import java.lang.RuntimeException
import java.util.*

class ItemDialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)
    }

    private lateinit var itemHandler: ItemHandler

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException(
                    getString(R.string.exception))
        }
    }

    private lateinit var etItemName: EditText
    private lateinit var etItemDescriptio: EditText
    private lateinit var etItemCatagory: Spinner
    private lateinit var etItemChecked: CheckBox
    private lateinit var etItemPrice: EditText




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.newitem))

        val rootView = requireActivity().layoutInflater.inflate(
                R.layout.additemlayout, null
        )
        etItemName = rootView.itemName
        etItemPrice = rootView.itemCost
        etItemCatagory = rootView.dropdown
        etItemDescriptio = rootView.description
        etItemChecked = rootView.purchased


        builder.setView(rootView)

        val arguments = this.arguments
        if (arguments != null && arguments.containsKey(
                        MainActivity.KEY_ITEM_TO_EDIT)) {
            val newitem = arguments.getSerializable(
                    MainActivity.KEY_ITEM_TO_EDIT
            ) as Item
            etItemName.setText(newitem.itemName)
            etItemDescriptio.setText(newitem.itemDescription)
            if(newitem.itemCatagory == context?.getString(R.string.food)){
                etItemCatagory.setSelection(0)
            }
            else if (newitem.itemCatagory == context?.getString(R.string.clothes)){
                etItemCatagory.setSelection(1)
            }
            else if (newitem.itemCatagory == context?.getString(R.string.christmas)){
                etItemCatagory.setSelection(3)
            }
            else {
                etItemCatagory.setSelection(2)
            }
            etItemChecked.setChecked(newitem.done)
            etItemPrice.setText(newitem.itemPrice)

            builder.setTitle(getString(R.string.edititem))
        }

        builder.setPositiveButton(getString(R.string.o)) {
            dialog, witch -> // empty
        }

        return builder.create()
    }


    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etItemName.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null && arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }
                dialog.dismiss()
            } else {
                etItemName.error = getString(R.string.errormessage)
            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemCreated(
                Item(
                        null,
                        etItemChecked.isChecked,
                        etItemName.text.toString(),
                        etItemDescriptio.text.toString(),
                        etItemPrice.text.toString(),
                        etItemCatagory.selectedItem.toString()
                )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
                MainActivity.KEY_ITEM_TO_EDIT
        ) as Item
        itemToEdit.itemName = etItemName.text.toString()
        itemToEdit.itemPrice = etItemPrice.text.toString()
        itemToEdit.itemCatagory = etItemCatagory.selectedItem.toString()
        itemToEdit.itemDescription = etItemDescriptio.text.toString()
        itemToEdit.done = etItemChecked.isChecked

        itemHandler.itemUpdated(itemToEdit)
    }

}