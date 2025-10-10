package bensalcie.app.core.network

/**
 * A sealed class representing the result of an API call, which can be in one of three states:
 * Success, Error, or Loading.
 *
 * @param T The type of the data expected on a successful API call.
 */
sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val throwable: Throwable) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}