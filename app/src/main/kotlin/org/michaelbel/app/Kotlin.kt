package org.michaelbel.app

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.contracts.*

//region https://kotlinlang.org/docs/whatsnew22.html#preview-of-context-parameters

// UserService defines the dependency required in the context
interface UserService {
    fun log(message: String)
    fun findUserById(id: Int): String
}

// Declares a function with a context parameter
context(users: UserService)
fun outputMessage(message: String) {
    // Uses log from the context
    users.log("Log: $message")
}

// Declares a property with a context parameter
context(users: UserService)
val firstUser: String
    // Uses findUserById from the context
    get() = users.findUserById(1)

// Uses "_" as context parameter name
context(_: UserService)
fun logWelcome() {
    // Finds the appropriate log function from UserService
    outputMessage("Welcome!")
}

//endregion

//region https://kotlinlang.org/docs/whatsnew22.html#preview-of-context-sensitive-resolution

enum class Problem { CONNECTION, AUTHENTICATION, DATABASE, UNKNOWN }

fun message(problem: Problem): String = when (problem) {
    CONNECTION -> "connection"
    AUTHENTICATION -> "authentication"
    DATABASE -> "database"
    UNKNOWN -> "unknown"
}

//endregion

//region https://kotlinlang.org/docs/whatsnew22.html#all-meta-target-for-properties

annotation class Email

data class UserOld(
    val username: String,

    @param:Email      // Constructor parameter
    @field:Email      // Backing field
    @get:Email        // Getter method
    @property:Email   // Kotlin property reference
    val email: String,
) {
    @field:Email
    @get:Email
    @property:Email
    val secondaryEmail: String? = null
}

data class UserNew(
    val username: String,

    // Applies @Email to param, property, field,
    // get, and setparam (if var)
    @all:Email val email: String,
) {
    // Applies @Email to property, field, and get
    // (no param since it's not in the constructor)
    @all:Email val secondaryEmail: String? = null
}

//endregion

//region https://kotlinlang.org/docs/whatsnew22.html#support-for-nested-type-aliases

class NetworkClient {

    typealias ResponseHandler = (code: Int, body: String) -> Unit

    fun send(request: String, handler: ResponseHandler) {
        // ...
    }
}

//endregion

//region https://kotlinlang.org/docs/whatsnew2220.html#support-for-return-statements-in-expression-bodies-with-explicit-return-types

fun getDisplayName(userId: String): String {
    return "User #$userId"
}

// fun getDisplayNameOrDefault(userId: String?): String = getDisplayName(userId ?: return "default")

//endregion

//region https://kotlinlang.org/docs/whatsnew2220.html#data-flow-based-exhaustiveness-checks-for-when-expressions

enum class UserRole { ADMIN, MEMBER, GUEST }

fun getPermissionLevel(role: UserRole): Int {
    // Covers the Admin case outside of the when expression
    if (role == UserRole.ADMIN) return 99

    return when (role) {
        UserRole.MEMBER -> 10
        UserRole.GUEST -> 1
        // You no longer have to include this else branch
        // else -> throw IllegalStateException()
    }
}

//endregion

//region https://kotlinlang.org/docs/whatsnew2220.html#support-for-reified-types-in-catch-clauses

/*inline fun <reified T : Throwable> handleException(block: () -> Unit) {
    try {
        block()
        // This is now allowed after the change
    } catch (e: T) {
        println("Caught specific exception: ${e::class.simpleName}")
    }
}*/

//endregion

//region https://kotlinlang.org/docs/whatsnew2220.html#improved-kotlin-contracts

sealed class Failure {
    class HttpError(val code: Int) : Failure()
    // Insert other failure types here
}

sealed class Result<out T, out F : Failure> {
    class Success<T>(val data: T) : Result<T, Nothing>()
    class Failed<F : Failure>(val failure: F) : Result<Nothing, F>()
}

// Uses a contract to assert a generic type
@OptIn(ExperimentalContracts::class)
fun <T, F : Failure> Result<T, F>.isHttpError(): Boolean {
    contract {
        returns(true) implies (this@isHttpError is Result.Failed<Failure.HttpError>)
    }
    return this is Result.Failed && this.failure is Failure.HttpError
}

//endregion

//region https://kotlinlang.org/docs/whatsnew23.html#explicit-backing-fields

/*fun ebf() {
    val city: StateFlow<String>
        field = MutableStateFlow("")

    fun updateCity(newCity: String) {
        // Smart casting works automatically
        city.value = newCity
    }
}*/

//endregion
