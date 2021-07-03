package com.example.elegantcalorietracker.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatEditText
import com.example.elegantcalorietracker.databinding.DialogEditTextBinding

typealias DialogWithTextFieldClickListener = (
    dialog: DialogInterface,
    which: Int,
    editText: AppCompatEditText
) -> Unit

fun showDialogWithTextField(
    context: Context,
    title: String,
    inputType: Int? = null,
    hint: String? = null,
    positiveText: String = "Ok",
    negativeText: String = "Cancel",
    positiveListener: DialogWithTextFieldClickListener? = null,
    negativeListener: DialogWithTextFieldClickListener? = null
) {
    // EditText settings
    val dialogLayout =
        DialogEditTextBinding.inflate(LayoutInflater.from(context))
    val editText = dialogLayout.edit
    editText.apply {
        this.hint = hint
        if (inputType != null) this.inputType = inputType
        requestFocus()
    }
    // Dialog settings
    val dialog = AlertDialog.Builder(context)
        .setTitle(title)
        .setView(editText.rootView)
        .setPositiveButton(positiveText) { dialog, which ->
            positiveListener?.invoke(dialog, which, editText)
        }
        .setNegativeButton(negativeText) { dialog, which ->
            negativeListener?.invoke(dialog, which, editText)
        }
        .create()
    // Dismiss focus when leaving the dialog
    dialog.window?.clearFlags(
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
    )
    // Opens keyboard when the dialog appears
    dialog.window?.setSoftInputMode(
        WindowManager.LayoutParams
            .SOFT_INPUT_STATE_VISIBLE
    )
    dialog.show()
}
