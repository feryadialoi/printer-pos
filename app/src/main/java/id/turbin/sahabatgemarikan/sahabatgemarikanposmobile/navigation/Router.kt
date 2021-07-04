package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.navigation

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.*


class Router {
    private lateinit var intent: Intent

    fun navigateToProductActivity(activity: Activity) {
        intent = Intent(activity, ProductActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToProductActivityAndFinish(activity: Activity) {
        navigateToProductActivity(activity)
        activity.finish()
    }

    fun navigateToLoginActivity(activity: Activity) {
        intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToLoginActivityAndFinish(activity: Activity) {
        navigateToLoginActivity(activity)
        activity.finish()
    }

    fun navigateToSaleActivity(activity: Activity) {
        intent = Intent(activity, SaleActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToSaleDetailActivity(activity: Activity, saleId: Long) {
        intent = Intent(activity, SaleDetailActivity::class.java)
        intent.putExtra("saleId", saleId)
        activity.startActivity(intent)
    }

    fun navigateToSaleDetailActivityAndFinish(activity: Activity, saleId: Long) {
        navigateToSaleDetailActivity(activity = activity, saleId = saleId)
        activity.finish()
    }

    fun navigateToSaleHistoryActivity(activity: Activity) {
        intent = Intent(activity, SaleHistoryActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToSettingActivity(activity: Activity) {
        intent = Intent(activity, SettingActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToListPrinterActivity(activity: Activity) {
        intent = Intent(activity, ListPrinterActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToListPrinterActivityForResult(activity: Activity, requestCode: Int) {
        intent = Intent(activity, ListPrinterActivity::class.java)
        activity.startActivityForResult(intent, requestCode)
    }

    fun enableBluetooth(activity: Activity, requestCode: Int) {
        intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity.startActivityForResult(intent, requestCode)
    }

    fun navigateToMainActivity(activity: Activity){
        intent = Intent(activity, MainActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToChangePasswordActivity(activity: Activity){
        intent = Intent(activity, ChangePasswordActivity::class.java)
        activity.startActivity(intent)
    }

    fun navigateToMainActivityAndFinish(activity: Activity){
        navigateToMainActivity(activity)
        activity.finish()
    }

    fun navigateToProfileActivity(activity: Activity){
        intent = Intent(activity, ProfileActivity::class.java)
        activity.startActivity(intent)
    }


}