package com.pascal.catalog.core.security

object NativeSecrets {
    init {
        System.loadLibrary("catalog-secrets")
    }

    external fun nativeBaseUrl(): String
    external fun nativeSalt(): String

    fun baseUrl(): String = nativeBaseUrl()
    fun signatureSeed(): String = nativeSalt()
}
