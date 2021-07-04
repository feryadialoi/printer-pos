package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivityChangePasswordBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshButton
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ChangePasswordRequest
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.Validator
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.AuthViewModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var validator: Validator

    private lateinit var viewBinding: ActivityChangePasswordBinding

    companion object {
        @JvmStatic
        private val TAG = "ChangePasswordActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityChangePasswordBinding.inflate(layoutInflater)
        // setContentView(R.layout.change_password_activity)
        setContentView(viewBinding.root)

        initView()

        textInputLayoutOnChange()
    }

    private fun initView() {
        initLoadingIndicator()
        initToolbar()
        initTextInputLayoutOldPassword()
        initTextInputLayoutNewPassword()
        initTextInputLayoutConfirmNewPassword()
        initButtonSave()
    }

    private fun textInputLayoutOnChange() {
        viewBinding.tilOldPassword.editText?.doOnTextChanged { text, start, before, count ->
            viewBinding.tilOldPassword.error = null
        }

        viewBinding.tilNewPassword.editText?.doOnTextChanged { text, start, before, count ->
            viewBinding.tilNewPassword.error = null
        }

        viewBinding.tilConfirmNewPassword.editText?.doOnTextChanged { text, start, before, count ->
            val confirmNewPassword = text.toString()
            val newPassword: String = viewBinding.tilNewPassword.editText!!.text.toString()

            if (confirmNewPassword != newPassword) {
                viewBinding.tilConfirmNewPassword.error = "Password tidak sama"
                viewBinding.tilConfirmNewPassword.errorIconDrawable = null
            } else {
                Log.d(TAG, "error null")
                viewBinding.tilConfirmNewPassword.error = null
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(viewBinding.tbActivityChangePassword)
        supportActionBar?.apply {
            title = "Ubah Password"
            elevation = 15F
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }



    private fun initLoadingIndicator() {
        viewBinding.pbActivityChangePasswordLoading.visibility = View.GONE
    }

    private fun initTextInputLayoutOldPassword() {
        viewBinding.tilOldPassword.hint = getString(R.string.change_password_activity_old_password)
    }

    private fun initTextInputLayoutNewPassword() {
        viewBinding.tilNewPassword.hint = getString(R.string.change_password_activity_new_password)
    }

    private fun initTextInputLayoutConfirmNewPassword() {
        viewBinding.tilConfirmNewPassword.hint =
            getString(R.string.change_password_activity_confirm_new_password)
    }

    private fun initButtonSave() {
//        viewBinding.btnSave.text = getString(R.string.save)
//        viewBinding.btnSave.setOnClickListener { changePassword() }

        FreshButton(viewBinding = viewBinding.btnFreshChangePasswordSave)
            .setText(text = "Simpan")
            .setOnClickListener { changePassword() }
    }

    private fun changePassword() {

        val changePasswordRequest = ChangePasswordRequest(
            oldPassword = viewBinding.tilOldPassword.editText?.text.toString(),
            newPassword = viewBinding.tilNewPassword.editText?.text.toString(),
            confirmNewPassword = viewBinding.tilConfirmNewPassword.editText?.text.toString()
        )

        validator.validate<ChangePasswordRequest>(
            data = changePasswordRequest,
            onError = { errors ->
                Log.d(TAG, errors.toString())
                errors.forEach { error ->
                    when (error.name) {
                        "oldPassword" -> {
                            viewBinding.tilOldPassword.error = error.textError
                            viewBinding.tilOldPassword.errorIconDrawable = null
                        }
                        "newPassword" -> {
                            viewBinding.tilNewPassword.error = error.textError
                            viewBinding.tilNewPassword.errorIconDrawable = null
                        }
                        "confirmNewPassword" -> {
                            viewBinding.tilConfirmNewPassword.error = error.textError
                            viewBinding.tilConfirmNewPassword.errorIconDrawable = null
                        }
                    }
                }
            },
            onValid = {
                authViewModel.changePassword(changePasswordRequest = changePasswordRequest)
                    .observe(this) { state ->
                        when (state) {
                            is LoadingResourceState -> {
                                onChangePasswordLoading()
                            }
                            is SuccessResourceState<*> -> {
                                onChangePasswordSuccess((state.data as ApiResponse<*>).message)
                            }
                            is ErrorResourceState -> {
                                onChangePasswordError(error = state.error)
                            }
                        }
                    }
            }
        )




    }

    private fun checkPasswordMatch(newPassword: String, confirmNewPassword: String): Boolean {
        return newPassword.equals(confirmNewPassword)
    }

    private fun onChangePasswordError(error: Exception) {
        viewBinding.pbActivityChangePasswordLoading.visibility = View.GONE
        when(error){
            is IOException -> {}
            is HttpException -> {
                Log.e(TAG, error.response().toString())
            }
            else -> {}
        }
    }

    private fun onChangePasswordSuccess(changePasswordResponse: String) {
        viewBinding.pbActivityChangePasswordLoading.visibility = View.GONE
        Toast.makeText(applicationContext, changePasswordResponse, Toast.LENGTH_LONG).show()
        onBackPressed()
    }

    private fun onChangePasswordLoading() {
        viewBinding.pbActivityChangePasswordLoading.visibility = View.VISIBLE
    }
}
