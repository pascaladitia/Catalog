package com.pascal.catalog.core.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.pascal.catalog.core.network.model.UserDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object PreferencesLogin {

    private const val PREFS_NAME = "login_prefs"

    private const val IS_SAVE_LOGIN = "is_save_login"
    private const val RESPONSE_LOGIN = "response_login"

    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        encodeDefaults = true
    }

    private fun prefs(context: Context) =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private inline fun edit(context: Context, block: SharedPreferences.Editor.() -> Unit) {
        prefs(context).edit(commit = true, block)
    }

    private inline fun <reified T> putObject(
        context: Context,
        key: String,
        value: T?
    ) {
        edit(context) {
            putString(key, value?.let { json.encodeToString(it) })
        }
    }

    private inline fun <reified T> getObject(
        context: Context,
        key: String
    ): T? {
        return prefs(context).getString(key, null)
            ?.let { json.decodeFromString(it) }
    }

    fun setLoginResponse(context: Context, responseLogin: UserDto?) =
        putObject(context, RESPONSE_LOGIN, responseLogin)

    fun saveLoginData(context: Context, isSaveLogin: Boolean) =
        edit(context) { putBoolean(IS_SAVE_LOGIN, isSaveLogin) }

    fun getIsSaveLogin(context: Context): Boolean =
        prefs(context).getBoolean(IS_SAVE_LOGIN, false)

    fun getLoginResponse(context: Context): UserDto? =
        getObject(context, RESPONSE_LOGIN)

    fun deleteLoginData(context: Context) =
        edit(context) { clear() }
}
