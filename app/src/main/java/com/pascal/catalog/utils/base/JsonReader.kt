package com.pascal.catalog.utils.base

import android.content.Context
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

object JsonReader {
    inline fun <reified T> load(context: Context, fileName: String): T {
        val inputStream = context.assets.open(fileName)
        val text = BufferedReader(InputStreamReader(inputStream)).use { it.readText() }
        return Json { ignoreUnknownKeys = true }.decodeFromString(text)
    }
}
