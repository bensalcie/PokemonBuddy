package bensalcie.app.core.network

/**
 * Represent the result of an API call.
 * @param [T] The type of the data returned by the API call.
 * @property [data] The data returned by the API call.
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val throwable: Throwable) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}