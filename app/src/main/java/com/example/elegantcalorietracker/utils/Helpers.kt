package com.example.elegantcalorietracker.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.WindowManager
import com.example.elegantcalorietracker.databinding.FoodDialogBinding

private fun showDialogWithTextField(
    context: Context,
    title: String,
    hint: String? = null,
    positiveText: String? = "Ok",
    negativeText: String? = "Cancel",
    positiveListener: DialogInterface.OnClickListener? = null,
    negativeListener: DialogInterface.OnClickListener? = null,
):
    DialogClickListener = {
    val servingEditText = FoodDialogBinding
        .inflate(LayoutInflater.from(context)).textField
    servingEditText.apply {
        this.hint = hint
        requestFocus()
    }
    val dialog = AlertDialog.Builder(context)
        .setTitle(title)
        .setView(servingEditText.rootView)
        .setPositiveButton(positiveText, positiveListener)
        .setNegativeButton(negativeText, negativeListener)
        .create()
    dialog.window?.clearFlags(
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
    )
    dialog.window?.setSoftInputMode(
        WindowManager.LayoutParams
            .SOFT_INPUT_STATE_VISIBLE
    )
    dialog.show()
}
