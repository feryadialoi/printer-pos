package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.extensions.retrofit

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ErrorResponse
import okhttp3.ResponseBody

fun ResponseBody.parseError(): ErrorResponse {
    return Gson().fromJson(this.charStream(), ErrorResponse::class.java)

}
