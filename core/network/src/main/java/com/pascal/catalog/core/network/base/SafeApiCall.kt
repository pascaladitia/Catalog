package com.pascal.catalog.core.network.base

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.bodyAsText
import java.io.IOException

abstract class SafeApiCall {

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): T {
        return try {
            apiCall()
        } catch (e: Exception) {
            val message = when (e) {
                is ClientRequestException -> e.response.bodyAsText().ifBlank { e.message }
                is ServerResponseException -> e.response.bodyAsText().ifBlank { e.message }

                is IOException -> "Network error. Please check your connection."
                else -> e.message.orEmpty().ifBlank { "Unknown error" }
            }
            throw Exception(message)
        }
    }
}
