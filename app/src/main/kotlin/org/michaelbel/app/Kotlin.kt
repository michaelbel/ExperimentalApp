@file:CheatSheetFile("Kotlin keyword and construct quick reference")
@file:Suppress("unused", "UNUSED_PARAMETER", "MemberVisibilityCanBePrivate", "RemoveExplicitTypeArguments")

package org.michaelbel.app

import kotlin.LazyThreadSafetyMode.NONE
import kotlin.properties.Delegates
import kotlin.random.Random
import kotlin.reflect.KProperty

typealias Username = String

// Annotation targets
@Target(AnnotationTarget.FILE)
annotation class CheatSheetFile(val info: String)

@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.SETTER_PARAMETER)
annotation class ParamInfo(val info: String)

@Target(AnnotationTarget.PROPERTY)
annotation class PropertyInfo(val info: String)

@Target(AnnotationTarget.PROPERTY_DELEGATE)
annotation class DelegateInfo(val detail: String = "")

@Target(AnnotationTarget.RECEIVER)
annotation class ReceiverInfo

enum class NetworkState { Idle, Loading, Error }

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val throwable: Throwable) : Result<Nothing>()
}

interface Clickable {
    fun click(): Unit
}

abstract class BaseWidget(protected val identifier: Identifier) : Clickable {
    abstract val title: String

    open fun describe(): String = "Widget(${identifier.value}) titled $title"

    override fun click() {
        println("Base click from $title")
    }
}

@JvmInline
value class Identifier(val value: String)

open class FancyWidget constructor(identifier: Identifier) : BaseWidget(identifier) {

    final override val title: String = "Fancy"

    private var clickCount: Int = 0
    internal val notes: MutableList<String> = mutableListOf()

    lateinit var style: String

    val lazyNumber: Int by lazy(NONE) { Random.nextInt() }

    var observableText: String by Delegates.observable("init") { _: KProperty<*>, old, new ->
        notes += "Text changed from $old to $new"
    }

    var customAccessor: String = "getter/setter"
        get() = field.uppercase()
        private set(value) {
            field = "[$value]"
        }

    init {
        notes += "init block executed"
        style = "Material"
    }

    override fun click() {
        super.click()
        clickCount++
    }

    inner class Label(private val text: String) {
        fun show(): String = "Label($text) for ${this@FancyWidget.title} with style $style"
    }

    companion object CompanionExample {
        const val DEFAULT_STYLE: String = "Default"

        fun of(name: String): FancyWidget = FancyWidget(Identifier(name))
    }
}

data class User(public val name: Username, var age: Int)

class DelegatedClickable(private val delegate: Clickable) : Clickable by delegate

class AnnotatedHolder(
    @param:ParamInfo("Constructor parameter")
    @property:PropertyInfo("Readable property")
    val id: Identifier,

    @setparam:ParamInfo("Setter parameter annotation")
    var mutableName: String
) {
    @delegate:DelegateInfo("Delegate sample")
    val delegatedNote by lazy { "Delegated property" }
}

class Calculator : Clickable {

    override fun click() {
        println("Calculator clicked")
    }

    tailrec fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    inline fun <reified T> firstAsOrNull(items: List<Any>): T? {
        return items.firstOrNull { it is T } as? T
    }

    inline fun repeatWithCallbacks(
        times: Int,
        crossinline onEach: (index: Int) -> Unit,
        noinline onDone: () -> Unit
    ) {
        for (i in 0 until times) {
            onEach(i)
        }
        onDone()
    }

    suspend fun suspendWork(delayMillis: Long): Result<String> {
        return Result.Success("Completed in $delayMillis ms")
    }

    operator fun Identifier.plus(other: Identifier): Identifier = Identifier(this.value + other.value)

    infix fun Identifier.merge(other: Identifier): Identifier = this + other

    fun controlFlowExamples(items: List<Any?>) {
        val nullableValue: Any? = null
        val booleanFlag = true
        val oppositeFlag = false

        if (booleanFlag && !oppositeFlag) {
            println("if/else branch")
        } else {
            println("else branch")
        }

        when (val state = items.randomOrNull()) {
            null -> println("Found null")
            is Int -> println("Found Int $state")
            is String -> println("Found String ${state.length}")
            else -> println("Unknown type")
        }

        val rangeSum = (1..3).sumOf { it }
        println("for-loop result: $rangeSum")

        var counter = 0
        while (counter < 3) {
            counter++
        }

        do {
            counter--
        } while (counter > 0)

        for (number in 1..5) {
            if (number == 2) continue
            if (number == 4) break
        }

        val handled: String = try {
            riskyOperation()
        } catch (e: IllegalStateException) {
            "Caught: ${e.message}"
        } finally {
            println("finally always executes")
        }

        println(handled)

        val intList = listOf(1, 2, 3)
        if (2 in intList) {
            println("2 is in list")
        }

        val casted: String? = items.firstOrNull { it is String } as? String
        println(casted ?: "no cast")

        val result: Int = run calculate@{
            for (number in intList) {
                if (number == 2) return@calculate number * 2
            }
            0
        }
        println("Labeled return: $result")
    }

    fun <T> bounded(values: List<T>) where T : Number, T : Comparable<T> {
        println("Max is ${values.maxOrNull()}")
    }

    private fun riskyOperation(): String {
        throw IllegalStateException("Something went wrong")
    }
}

interface Producer<out T> {
    fun produce(): T
}

class StringProducer : Producer<String> {
    override fun produce(): String = "Produced"
}

class Logger {
    fun log(message: String) = println("LOG: $message")
}

context(Logger)
fun String.logSelf() {
    log("context receiver for '$this'")
}

@ReceiverInfo
fun @receiver:ReceiverInfo String.receiverAnnotatedLength(): Int = length

external fun externalMeaning(input: Int): Int

object KeywordPlayground {

    val user = User("Alice", 30)

    var numbers: MutableList<Int> = mutableListOf()

    fun useFunctions(calculator: Calculator) {
        val result = calculator.gcd(42, 56)
        println("gcd result: $result")

        val merged = Identifier("A") merge Identifier("B")
        println("merged identifier: ${merged.value}")

        calculator.repeatWithCallbacks(
            times = 3,
            onEach = { index -> println("Index $index") },
            onDone = { println("Completed") }
        )

        val firstString: String? = calculator.firstAsOrNull<String>(listOf(1, "value"))
        println("Found with reified: $firstString")
    }

    fun varargExample(vararg ids: Identifier): List<Identifier> {
        return ids.toList()
    }

    fun delegationExample() {
        val clickable: Clickable = object : Clickable {
            override fun click() = println("Anonymous click")
        }
        DelegatedClickable(clickable).click()
    }
}

// The expect/actual and dynamic keywords are primarily used in multiplatform or Kotlin/JS projects.
// They are shown here as text snippets to keep this Android/JVM module compiling while still
// serving as a reference:
val platformNotes: String = """
expect fun platformValue(): String
actual fun platformValue(): String = "JVM"
val jsValue: dynamic = js("({})")
""".trimIndent()


