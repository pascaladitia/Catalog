package com.pascal.catalog.core.domain.model

sealed interface DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>
    data class Error(val message: String, val cause: Throwable? = null) : DataResult<Nothing>
}
