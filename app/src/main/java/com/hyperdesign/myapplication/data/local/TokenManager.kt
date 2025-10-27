package com.hyperdesign.myapplication.data.local

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.hyperdesign.myapplication.domain.Entity.UserEntity
import kotlinx.coroutines.flow.update

class TokenManager(private val context: Context) {

    companion object {
        private const val PREFS_NAME = "encrypted_prefs"
        private const val ACCESS_TOKEN = "access_token"
        private const val USER_DATA = "user_data"
        private const val OTP_CODE = "otp_code"
        private const val LANGUAGE = "language"
        private const val FCM_TOKEN = "fcm_token"

        private const val BRANCH_ID = "branch_id"

        private const val ADDRESS_ID ="address_id"
        private const val HOTLINE ="hotline"

        private const val AREAID = "area_id"
        private const val CURRENT_RESTURANT_BRANCH="current_resturent_Branch"
        private const val STATUS = "status"
    }

    private val masterKey: MasterKey by lazy {
        try {
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        } catch (e: Exception) {
            Log.e("TokenManager", "Failed to create MasterKey: ${e.message}")
            throw e
        }
    }



    private val encryptedSharedPreferences by lazy {
        try {
            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            Log.e("TokenManager", "Failed to create EncryptedSharedPreferences: ${e.message}")
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().clear().apply()
            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun saveAccessToken(accessToken: String) {
        encryptedSharedPreferences.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .apply()
    }

    fun saveHomeHotline(phone:String){
        encryptedSharedPreferences.edit()
            .putString(HOTLINE,phone)
            .apply()
    }

    fun getHomeHotline() = encryptedSharedPreferences.getString(HOTLINE,"")

    fun saveAreaId(areaId:String){
        encryptedSharedPreferences.edit()
            .putString(AREAID,areaId)
            .apply()
    }

    fun getAreaId() = encryptedSharedPreferences.getString(AREAID,"")


    fun setAddressId(addressId:String){
        encryptedSharedPreferences.edit()
            .putString(ADDRESS_ID, addressId)
            .apply()
    }

    fun getAddressId(): String? {
        return encryptedSharedPreferences.getString(ADDRESS_ID,"")
    }

    fun saveUserData(user: UserEntity?) {
        val gson = Gson()
        encryptedSharedPreferences.edit()
            .putString(USER_DATA, gson.toJson(user))
            .apply()
    }

    fun saveBranchId(id:Int){
        encryptedSharedPreferences.edit()
            .putInt(BRANCH_ID,id)
            .apply()
    }

    fun saveStatus(status:Int){
        encryptedSharedPreferences.edit()
            .putInt(STATUS,status)
            .apply()
    }
    fun saveCurrentResturentBranch(branch:String){
        encryptedSharedPreferences.edit()
            .putString(CURRENT_RESTURANT_BRANCH,branch)
            .apply()
    }

    fun getCurrentResturentBranch(): String? {
        return encryptedSharedPreferences.getString(CURRENT_RESTURANT_BRANCH,"")
    }

    fun getStatus():Int{

        return  encryptedSharedPreferences.getInt(STATUS,0)

    }

    fun getUserData(): UserEntity? {
        val json = encryptedSharedPreferences.getString(USER_DATA, "") ?: ""
        return if (json.isNotEmpty()) Gson().fromJson(json, UserEntity::class.java) else null
    }

    fun getAccessToken(): String? =
        encryptedSharedPreferences.getString(ACCESS_TOKEN, null)

    fun getBranchId():Int? = encryptedSharedPreferences.getInt(BRANCH_ID,0)

    fun saveOtpCode(otpCode: Int) {
        encryptedSharedPreferences.edit()
            .putInt(OTP_CODE, otpCode)
            .apply()
    }

    fun clearOtpCode() {
        encryptedSharedPreferences.edit()
            .remove(OTP_CODE)
            .apply()
    }

    fun saveLanguage(value: String) {
        encryptedSharedPreferences.edit()
            .putString(LANGUAGE, value)
            .apply()
    }

    fun getLanguage(): String? =
        encryptedSharedPreferences.getString(LANGUAGE, null)

    fun getOtpCode(): Int =
        encryptedSharedPreferences.getInt(OTP_CODE, 0)

    fun saveFcmToken(value: String) {
        encryptedSharedPreferences.edit()
            .putString(FCM_TOKEN, value)
            .apply()
    }

    fun deleteUserData() {
        encryptedSharedPreferences.edit()
            .remove(USER_DATA)
            .apply()
    }

    fun getFcmToken(): String? =
        encryptedSharedPreferences.getString(FCM_TOKEN, null)

    fun clearAll() {
        try {
            Log.d("TokenManager", "Before clear - AccessToken: ${getAccessToken()}, UserData: ${getUserData()}")
            encryptedSharedPreferences.edit().clear().apply()
            Log.d("TokenManager", "After clear - AccessToken: ${getAccessToken()}, UserData: ${getUserData()}")
        } catch (e: Exception) {
            Log.e("TokenManager", "Failed to clear EncryptedSharedPreferences: ${e.message}")
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit().clear().apply()
        }
    }
}
