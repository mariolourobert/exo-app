package com.deezer.exoapplication.utils

import com.deezer.exoapplication.utils.ResultOf.Failure
import com.deezer.exoapplication.utils.ResultOf.Success


sealed class ResultOf<out S, out F> {
    abstract val value: Any?

    data class Success<out S>(override val value: S) : ResultOf<S, Nothing>()
    data class Failure<out F>(override val value: F) : ResultOf<Nothing, F>()

    val isSuccess: Boolean get() = this is Success<*>
    val isFailure: Boolean get() = this is Failure<*>

    companion object {
        inline fun <reified S> success(value: S): Success<S> = Success(value)

        inline fun <reified F> failure(value: F): Failure<F> = Failure(value)
    }
}

fun <S, F> ResultOf<S, F>.successOrNull(): S? = when (this) {
    is Success -> value
    is Failure -> null
}

fun <S, F> ResultOf<S, F>.successOrElse(onFailure: (failure: F) -> S): S = when (this) {
    is Success -> value
    is Failure -> onFailure(value)
}

fun <S, F> ResultOf<S, F>.successOrDefault(default: S): S = when (this) {
    is Success -> value
    is Failure -> default
}

fun <S, F> ResultOf<S, F>.successOrThrow(
    throwable: (F) -> Throwable = { IllegalStateException() },
): S = when (this) {
    is Success -> value
    is Failure -> throw throwable(value)
}

fun <S, F> ResultOf<S, F>.failureOrNull(): F? = when (this) {
    is Success -> null
    is Failure -> value
}

fun <S, F> ResultOf<S, F>.failureOrThrow(
    throwable: (S) -> Throwable = { IllegalStateException() },
): F = when (this) {
    is Success -> throw throwable(value)
    is Failure -> value
}

inline fun <S1, S2, F> ResultOf<S1, F>.mapSuccess(
    transform: (S1) -> S2,
): ResultOf<S2, F> = when (this) {
    is Success -> Success(transform(value))
    is Failure -> this
}

suspend inline fun <S1, S2, F> ResultOf<S1, F>.suspendableMapSuccess(
    transform: suspend (S1) -> S2,
): ResultOf<S2, F> = when (this) {
    is Success -> Success(transform(value))
    is Failure -> this
}

inline fun <S1, S2, F> ResultOf<S1, F>.flatMapSuccess(
    map: (S1) -> ResultOf<S2, F>,
): ResultOf<S2, F> = when (this) {
    is Success -> map(value)
    is Failure -> this
}

inline fun <S, F1, F2> ResultOf<S, F1>.mapFailure(
    transform: (F1) -> F2,
): ResultOf<S, F2> = when (this) {
    is Success -> this
    is Failure -> Failure(transform(value))
}

inline fun <S, F> ResultOf<S, F>.onSuccess(
    action: (S) -> Unit,
): ResultOf<S, F> = apply {
    when (this) {
        is Success -> action(value)
        is Failure -> Unit
    }
}

inline fun <S, F> ResultOf<S, F>.onFailure(
    action: (F) -> Unit,
): ResultOf<S, F> = apply {
    when (this) {
        is Success -> Unit
        is Failure -> action(value)
    }
}
