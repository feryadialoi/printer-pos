package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivityLoginBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshAlertDialogSuccessLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.extensions.retrofit.parseError
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.*
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.LoginResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.LoginRequest
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.properties.HttpStatusCode
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.AuthSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.UserSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.Validator
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.AuthViewModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {


    private lateinit var viewBinding: ActivityLoginBinding

    @Inject
    lateinit var authViewModel: AuthViewModel

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authSharedPreferences: AuthSharedPreferences

    @Inject
    lateinit var userSharedPreferences: UserSharedPreferences

    @Inject
    lateinit var validator: Validator

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        // setContentView(R.layout.activity_login)
        setContentView(viewBinding.root)

        initView()
    }

    private fun initView() {
        initLoadingIndicator()
        initButtonLogin()
        initTextField()

//        FreshButton(viewBinding = viewBinding.loginButtonLogin)
//            .setText("Masuk")
//            .setOnClickListener { Log.d(TAG, "button") }
//            .create()
//
//        FreshButtonSmall(viewBinding = viewBinding.loginButtonSmall)
//            .setText("tombol kecil")
//            .setOnClickListener { Log.d(TAG, "small button") }
//            .create()
//
//        FreshButtonSmallOutlined(viewBinding = viewBinding.loginButtonSmallOutlined)
//            .setText("outlined")
//            .setOnClickListener { Log.d(TAG, "small outlined") }
//            .create()
//
//        FreshProductCard(viewBinding = viewBinding.loginProductCart)
//            .setProductName("Ikan Tenggiri 500gr")
//            .setProductPrice("34.000")
//            .setProductUnit("500gr")
//            .setProductQuantity("2")
//            .setDecrementOnClickListener { Log.d(TAG, "fresh product decrement") }
//            .setIncrementOnClickListener { Log.d(TAG, "fresh product increment") }
//            .setAddOnClickListener { Log.d(TAG, "fresh product add") }
//            .setImage("https://sahabatgemarikan.id/37-large_default/tenggiri-steak-segar.jpg")
//            .create()
//
//        FreshProductCard(viewBinding = viewBinding.loginProductCart2)
//            .toggleShowButton(toggleShowButton = FreshProductCard.ToggleShowButton.COUNTER_BUTTON)
//            .setProductName("Ikan Tongkol 500gr")
//            .setProductPrice("22.000")
//            .setProductUnit("500gr")
//            .setProductQuantity("2")
//            .setDecrementOnClickListener { Log.d(TAG, "fresh product decrement") }
//            .setIncrementOnClickListener { Log.d(TAG, "fresh product increment") }
//            .setAddOnClickListener { Log.d(TAG, "fresh product add") }
//            .setImage("https://sahabatgemarikan.id/37-large_default/tenggiri-steak-segar.jpg")
//            .create()
//
//        FreshSaleCard(viewBinding = viewBinding.loginSaleCard)
//            .setCode("JFDK434234")
//            .setItemCount("2 barang")
//            .setSubtotal("68.000")
//            .setDate("04/04/2021")
//            .setOnClickListener { Log.d(TAG, "sale") }
//            .create()
//
//        FreshSaleProductCard(viewBinding = viewBinding.loginSaleProduct)
//            .setProductName("Ikan Tenggiri 500gr")
//            .setPriceXQuantity("34.000 x 2")
//            .setSubtotal("68.000")
//            .setOnClickListener { Log.d(TAG, "sale product") }
//            .create()
//
//        FreshProductCartCard(viewBinding = viewBinding.loginProductCartCard)
//            .setProductName("Ikan Tongkol 500gr")
//            .setProductPrice("22.000")
//            .setProductUnit("500gr")
//            .setProductQuantity("2")
//            .setOnClick { Log.d(TAG, "on click") }
//            .setProductIncrementOnClickListener { Log.d(TAG, "increment") }
//            .setProductDecrementOnClickListener { Log.d(TAG, "decrement") }
//            .create()
    }

    private fun initTextField() {
        viewBinding.tiLoginEmail.editText?.doOnTextChanged { text, start, before, count ->
            viewBinding.tiLoginEmail.error = null
        }
        viewBinding.tiLoginPassword.editText?.doOnTextChanged { text, start, before, count ->
            viewBinding.tiLoginPassword.error = null
        }
    }

    private fun initLoadingIndicator() {
        viewBinding.pbLoginLoading.visibility = View.GONE
    }

    private fun initButtonLogin() {
//        viewBinding.btnLoginLogin.text = getString(R.string.btn_login_login)
//        viewBinding.btnLoginLogin.setOnClickListener { login() }

        FreshButton(viewBinding = viewBinding.btnFreshLoginButtonLogin)
            .setText(text = "Masuk")
            .setOnClickListener { login() }
    }


    private fun login() {
        val loginRequest = LoginRequest(
            email = viewBinding.tiLoginEmail.editText?.text.toString(),
            password = viewBinding.tiLoginPassword.editText?.text.toString(),
        )

        validator.validate<LoginRequest>(
            data = loginRequest,
            onError = { errors ->
                Log.d(TAG, errors.toString())
                errors.forEach { error ->
                    when (error.name) {
                        "email" -> {
                            viewBinding.tiLoginEmail.error = error.textError
                            viewBinding.tiLoginEmail.errorIconDrawable = null
                        }
                        "password" -> {
                            viewBinding.tiLoginPassword.error = error.textError
                            viewBinding.tiLoginPassword.errorIconDrawable = null
                        }
                    }
                }
            },
            onValid = {
                Log.d(TAG, "valid, $it")

                authViewModel.login(loginRequest = loginRequest).observe(this) { state ->
                    Log.d(TAG, state.toString())
                    when (state) {
                        is LoadingResourceState -> {
                            loginLoading()
                        }
                        is SuccessResourceState<*> -> {
                            loginSuccess(loginResponse = (state.data as ApiResponse<*>).data as LoginResponse)
                        }
                        is ErrorResourceState -> {
                            loginError(error = state.error)
                        }
                    }
                }
            }
        )
    }

    private fun loginLoading() {
        viewBinding.pbLoginLoading.visibility = View.VISIBLE
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    private fun loginSuccess(loginResponse: LoginResponse) {
        viewBinding.pbLoginLoading.visibility = View.GONE
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//        router.navigateToProductActivityAndFinish(this)
        router.navigateToMainActivityAndFinish(this)
        authSharedPreferences.setAuthToken(loginResponse.token)
        userSharedPreferences.setProfile(loginResponse.user)
    }

    private fun loginError(error: Exception) {
        viewBinding.pbLoginLoading.visibility = View.GONE
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        when (error) {
            is IOException -> {
            }
            is HttpException -> {
                when (error.code()) {
                    HttpStatusCode.UNAUTHORIZED -> {
                        val errorResponse = error.response()?.errorBody()?.parseError()!!
                        loginErrorAlertDialog(errorResponse.message)
                    }
                }
            }
            else -> {
            }
        }
    }

    private fun loginErrorAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Login Gagal")
            .setMessage(message)
            .setPositiveButton("Tutup") { dialog, which -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun handleUnauthorizedEvent() {
        Log.d(TAG, "OVERRIDE handleUnauthorizedEvent")
    }
}

