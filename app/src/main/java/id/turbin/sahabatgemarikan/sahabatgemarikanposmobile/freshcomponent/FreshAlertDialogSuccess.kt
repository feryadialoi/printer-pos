package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshAlertDialogSuccessLayoutBinding

class FreshAlertDialogSuccess(private val viewBinding: FreshAlertDialogSuccessLayoutBinding) {

    private val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(viewBinding.root.context)

    private var onClick: () -> Unit = {}

    init {
        dialogBuilder.setView(viewBinding.root)
    }

    fun setCancelable(isCancelable: Boolean): FreshAlertDialogSuccess {
        dialogBuilder.setCancelable(isCancelable)
        return this
    }

    fun setAlertDialogText(text: String): FreshAlertDialogSuccess {
        viewBinding.tvFreshAlertDialogText.text = text
        return this
    }

    fun setButtonText(buttonText: String): FreshAlertDialogSuccess {
        viewBinding.tvFreshAlertDialogSuccessButtonText.text = buttonText
        return this
    }

    fun setButtonOnClickListener(onClick: () -> Unit): FreshAlertDialogSuccess {
        this.onClick = onClick
        return this
    }

    fun create(): AlertDialog {
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding.cvFreshAlertDialogSuccessButton.setOnClickListener {
            dialog.dismiss()
            onClick()
        }

        return dialog
    }


}