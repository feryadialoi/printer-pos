package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivityProfileBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshButton
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Profile
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.ErrorResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.LoadingResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.states.SuccessResourceState
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.ProfileViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileActivity : BaseActivity() {

    private val TAG = this::class.simpleName

    @Inject
    lateinit var profileViewModel: ProfileViewModel

    @Inject
    lateinit var router: Router

    private lateinit var viewBinding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)
        Log.d(TAG, "onCreateView")

        initView()

        onLoadProfiles()

    }

    private fun initView() {
        initToolbar()
        initTextViewOutletCode()
        initTextViewAddress()
        initButtonChangePassword()
    }

    private fun initToolbar() {
        setSupportActionBar(viewBinding.tbActivityProfile)
        supportActionBar.apply {
            title = getString(R.string.profile_activity_title)
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }

    private fun onLoadProfiles() {
        profileViewModel.loadProfiles().observe(this) { state ->
            when (state) {
                is LoadingResourceState -> {
                    onLoadProfileLoading()
                }

                is SuccessResourceState<*> -> {
                    onLoadProfileSuccess(profile = (state.data as ApiResponse<*>).data as Profile)
                }

                is ErrorResourceState -> {
                    onLoadProfileError(error = state.error)
                }
            }
        }
    }

    private fun initButtonChangePassword() {
//        viewBinding.btActivityProfileChangePassword.text =
//            getString(R.string.profile_activity_change_password)
//        viewBinding.btActivityProfileChangePassword.setOnClickListener { changePassword() }

        FreshButton(viewBinding = viewBinding.btnFreshProfileChangePassword)
            .setText(text = "Ubah Password")
            .setOnClickListener { changePassword() }
    }

    private fun changePassword() {
        router.navigateToChangePasswordActivity(this)
    }

    private fun initTextViewOutletCode() {
        viewBinding.tvActivityProfileLabelOutletCode.text =
            getString(R.string.profile_activity_tv_outlet_code)
    }


    private fun initTextViewAddress() {
        viewBinding.tvActivityProfileLabelAddress.text =
            getString(R.string.profile_activity_tv_address)
    }


    private fun onLoadProfileLoading() {
        viewBinding.pbActivityProfileLoading.visibility = View.VISIBLE
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    private fun onLoadProfileSuccess(profile: Profile) {
        viewBinding.pbActivityProfileLoading.visibility = View.GONE
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        viewBinding.tvActivityProfileOutletCode.text = if (profile.code != null) profile.code else "-"
        viewBinding.tvActivityProfileName.text = if (profile.name != null) profile.name else "-"
        viewBinding.tvActivityProfileEmail.text = if (profile.email != null) profile.email else "-"
        viewBinding.tvActivityProfilePhoneNumber.text = if (profile.phone != null) profile.phone else "-"
        viewBinding.tvActivityProfileAddress.text = if (profile.address != null) profile.address else "-"
    }

    private fun onLoadProfileError(error: Exception) {
        viewBinding.pbActivityProfileLoading.visibility = View.GONE
        window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

}