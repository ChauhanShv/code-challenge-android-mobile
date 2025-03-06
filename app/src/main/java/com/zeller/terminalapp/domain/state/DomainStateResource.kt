package com.zeller.terminalapp.domain.state

sealed class DomainStateResource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : DomainStateResource<T>(data)
    class Error<T>(message: String?, data: T? = null) : DomainStateResource<T>(data, message)
    class Loading<T> : DomainStateResource<T>()
}