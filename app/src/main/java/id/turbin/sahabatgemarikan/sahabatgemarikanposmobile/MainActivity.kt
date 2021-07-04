package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.ActivityMainBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FreshAlertDialogConfirmationLayoutBinding
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.freshcomponent.FreshAlertDialogConfirmation
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Product
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation.Router
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.sharedpreferences.UserSharedPreferences
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.viewmodels.ProductViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var productViewModel: ProductViewModel

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var userSharedPreferences: UserSharedPreferences

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var viewBinding: ActivityMainBinding

    private lateinit var navController: NavController

    companion object {
        @JvmStatic
        private val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(viewBinding.root)

        initView()

        initNavHeader()

        initMenu()

        observeProducts()

    }

    private fun initView() {
        initToolbar()
        initActionBarWithNavController()
    }

    private fun initToolbar() {
        setSupportActionBar(viewBinding.appBarMain.toolbar)
        supportActionBar?.apply {
            title = "Sahabat Gemarikan"
            elevation = 15F

            viewBinding.appBarMain.ibProductCart.setOnClickListener {
                goToSaleActivity()
            }
        }
    }

    private fun initNavHeader() {
        val profile = userSharedPreferences.getProfile()
        Log.d(TAG, profile.toString())
        val headerView = viewBinding.navView.getHeaderView(0)
        val navImage: ImageView = headerView.findViewById(R.id.iv_nav_header_main_image)
        val navName: TextView = headerView.findViewById(R.id.tv_nav_header_main_name)
        val navOutletCode: TextView = headerView.findViewById(R.id.tv_nav_header_main_outlet_code)

        navName.text = profile.name
        navOutletCode.text = profile.code

        navImage.setOnClickListener { accessProfile() }
    }

    private fun accessProfile() {
        viewBinding.drawerLayout.closeDrawer(Gravity.LEFT, true)
        router.navigateToProfileActivity(this@MainActivity)
    }

    private fun initActionBarWithNavController() {
        val drawerLayout: DrawerLayout = viewBinding.drawerLayout
        val navView: NavigationView = viewBinding.navView
        navController = findNavController(R.id.nav_host_fragment)
//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_create_sale,
                R.id.nav_sales_history,
                R.id.nav_setting,
                R.id.nav_logout,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    private fun initMenu() {
        initOnClickCreateSale()
        initOnClickSalesHistory()
        initOnClickSetting()
        initOnClickLogout()
    }

    private fun initOnClickCreateSale() {
        viewBinding.navCreateSale.setOnClickListener { handleOnClickCreateSale() }
    }

    private fun handleOnClickCreateSale() {
        viewBinding.drawerLayout.closeDrawer(Gravity.LEFT, true)
        navController.navigate(R.id.action_create_sale)
    }

    private fun initOnClickSalesHistory() {
        viewBinding.navSalesHistory.setOnClickListener { handleOnClickSalesHistory() }
    }

    private fun handleOnClickSalesHistory() {
        viewBinding.drawerLayout.closeDrawer(Gravity.LEFT, true)
        router.navigateToSaleHistoryActivity(this)
    }

    private fun initOnClickSetting() {
        viewBinding.drawerLayout.closeDrawer(Gravity.LEFT, true)
        viewBinding.navSetting.setOnClickListener { handleOnClickSetting() }
    }

    private fun handleOnClickSetting() {
        viewBinding.drawerLayout.closeDrawer(Gravity.LEFT, true)
        router.navigateToSettingActivity(this)
    }

    private fun initOnClickLogout() {
        viewBinding.drawerLayout.closeDrawer(Gravity.LEFT, true)
        viewBinding.navLogout.setOnClickListener { handleLogout() }
    }

    private val onDestinationChangedListener =
        NavController.OnDestinationChangedListener { navController, destination, arguments ->
            toggleShowCartButton(destination = destination)
        }


    private fun toggleShowCartButton(destination: NavDestination) {
        if (destination.id == R.id.nav_create_sale) {
            val products = productViewModel.products.value?.filter { it.quantity > 0 }
            if (products != null) {
                if (products.isEmpty()) {
                    viewBinding.appBarMain.tvProductCartCount.visibility = View.INVISIBLE
                } else {
                    viewBinding.appBarMain.tvProductCartCount.visibility = View.VISIBLE
                }
                viewBinding.appBarMain.ibProductCart.visibility = View.VISIBLE
            }
        } else {
            viewBinding.appBarMain.ibProductCart.visibility = View.INVISIBLE
            viewBinding.appBarMain.tvProductCartCount.visibility = View.INVISIBLE
        }
    }


    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(onDestinationChangedListener)
    }

    override fun onPause() {
        navController.removeOnDestinationChangedListener(onDestinationChangedListener)
        super.onPause()
    }

    private fun goToSaleActivity() {
        router.navigateToSaleActivity(this)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun productsQuantityListen(products: MutableList<Product>) {
        val productsWithQuantityNotZero = products.filter { product -> product.quantity > 0 }

        if (productsWithQuantityNotZero.isEmpty()) {
            viewBinding.appBarMain.tvProductCartCount.visibility = View.INVISIBLE
        } else {
            viewBinding.appBarMain.tvProductCartCount.visibility = View.VISIBLE
        }
        viewBinding.appBarMain.tvProductCartCount.text =
            products.filter { product -> product.quantity > 0 }.size.toString()
    }

    private fun observeProducts() {
        productViewModel.products.observe(this) { products ->
            productsQuantityListen(products = products)
        }
    }

    private fun confirmLogout() {
        logout()
    }

    private fun handleLogout() {
        viewBinding.drawerLayout.closeDrawer(Gravity.LEFT, true)
        FreshAlertDialogConfirmation(viewBinding = FreshAlertDialogConfirmationLayoutBinding.inflate(layoutInflater))
            .setAlertDialogText("Anda yakin ingin logout ?")
            .setButtonNoText("Batal")
            .setButtonYesText("Logout")
            .setButtonYesOnClickListener {
                confirmLogout()
            }
            .create()
            .show()

    }
}