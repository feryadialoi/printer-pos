package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models

import com.google.gson.annotations.SerializedName
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations.Equal
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations.Label
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations.Min
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.validations.rules.annotations.NotEmpty

data class ChangePasswordRequest(
    @Label(value = "Password lama")
    @NotEmpty
    @SerializedName(value = "old_password") var oldPassword: String,

    @Label(value = "Password baru")
    @NotEmpty
    @SerializedName(value = "new_password") var newPassword: String,

    @Label(value = "Konfirmasi password baru")
    @Equal(value = "newPassword")
    @NotEmpty
    @SerializedName(value = "confirm_new_password") var confirmNewPassword: String,
)