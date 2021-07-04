package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshAlertDialogConfirmationLayoutBinding

class FreshAlertDialogConfirmation(private val viewBinding: FreshAlertDialogConfirmationLayoutBinding) {

    private val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(viewBinding.root.context)

    private var onClickNo: () -> Unit = {}
    private var onClickYes: () -> Unit = {}

    init {
        dialogBuilder.setView(viewBinding.root)
    }

    fun setCancelable(isCancelable: Boolean): FreshAlertDialogConfirmation {
        dialogBuilder.setCancelable(isCancelable)
        return this
    }

    fun setAlertDialogText(text: String): FreshAlertDialogConfirmation {
        viewBinding.tvFreshAlertDialogText.text = text
        return this
    }

    fun setButtonYesText(buttonText: String): FreshAlertDialogConfirmation {
        viewBinding.tvFreshAlertDialogConfirmationButtonYesText.text = buttonText
        return this
    }

    fun setButtonYesOnClickListener(onClick: () -> Unit): FreshAlertDialogConfirmation {
        this.onClickYes = onClick
        return this
    }

    fun setButtonNoText(buttonText: String): FreshAlertDialogConfirmation {
        viewBinding.tvFreshAlertDialogConfirmationButtonNoText.text = buttonText
        return this
    }

    fun setButtonNoOnClickListener(onClick: () -> Unit): FreshAlertDialogConfirmation {
        this.onClickNo = onClick
        return this
    }

    fun create(): AlertDialog {
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding.cvFreshAlertDialogConfirmationButtonYes.setOnClickListener {
            dialog.dismiss()
            onClickYes()
        }
        viewBinding.cvFreshAlertDialogConfirmationButtonNo.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }


}