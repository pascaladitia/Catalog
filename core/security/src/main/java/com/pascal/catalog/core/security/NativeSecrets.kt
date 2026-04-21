package com.pascal.catalog.core.security

object NativeSecrets {
    init {
        System.loadLibrary("catalog-secrets")
    }

    external fun nativeBaseUrl(): String

    fun baseUrl(): String = nativeBaseUrl()
}
