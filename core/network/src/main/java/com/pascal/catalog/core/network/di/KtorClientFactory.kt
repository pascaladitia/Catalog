package com.pascal.catalog.core.network.di

import com.pascal.catalog.core.security.NativeSecrets
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.security.MessageDigest

object KtorClientFactory {

    fun create(json: Json): HttpClient = HttpClient(CIO) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("Ktor: $message")
                }
            }
            level = LogLevel.BODY
        }

        install(HttpTimeout) {
            connectTimeoutMillis = 60_000
            requestTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            url(NativeSecrets.baseUrl())

            val signature = buildSignature(url.build().encodedPath)
            headers.append(HttpHeaders.Accept, "application/json")
            headers.append("X-App-Signature", signature)
        }
    }

    private fun buildSignature(path: String): String {
        val raw = path + ":" + NativeSecrets.signatureSeed()
        val digest = MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
        return digest.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
