package com.nfccards.android.resources

sealed class Resource<T>(
    val data: T? = null,
    val message: String = "",
) {
    class Success<T>(data: T, message: String = "") : Resource<T>(data = data, message= message)
    class Error<T>(message: String) : Resource<T>(message = message)
    class Loading<T> : Resource<T>()
}
