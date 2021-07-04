package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshAlertDialogDangerLayoutBinding

class FreshAlertDialogDanger(private val viewBinding: FreshAlertDialogDangerLayoutBinding) {

    private val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(viewBinding.root.context)

    private var onClickNo: () -> Unit = {}
    private var onClickYes: () -> Unit = {}

    init {
        dialogBuilder.setView(viewBinding.root)
    }

    fun setCancelable(isCancelable: Boolean): FreshAlertDialogDanger {
        dialogBuilder.setCancelable(isCancelable)
        return this
    }

    fun setAlertDialogText(text: String): FreshAlertDialogDanger {
        viewBinding.tvFreshAlertDialogText.text = text
        return this
    }

    fun setButtonYesText(buttonText: String): FreshAlertDialogDanger {
        viewBinding.tvFreshAlertDialogDangerButtonYesText.text = buttonText
        return this
    }

    fun setButtonYesOnClickListener(onClick: () -> Unit): FreshAlertDialogDanger {
        this.onClickYes = onClick
        return this
    }

    fun setButtonNoText(buttonText: String): FreshAlertDialogDanger {
        viewBinding.tvFreshAlertDialogDangerButtonNoText.text = buttonText
        return this
    }

    fun setButtonNoOnClickListener(onClick: () -> Unit): FreshAlertDialogDanger {
        this.onClickNo = onClick
        return this
    }

    fun create(): AlertDialog {
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding.cvFreshAlertDialogDangerButtonYes.setOnClickListener {
            dialog.dismiss()
            onClickYes()
        }
        viewBinding.cvFreshAlertDialogDangerButtonNo.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }


}