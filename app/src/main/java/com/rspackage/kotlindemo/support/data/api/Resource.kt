package com.rspackage.kotlindemo.support.data.api

/**
 * A generic class that contains data and status about loading this data.
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val code: Int? = -1
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, code: Int? = -1, data: T? = null) : Resource<T>(data, message, code)
}