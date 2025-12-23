package dev.yidafu.terrain

/**
 * Custom assertion function for runtime validation.
 *
 * Unlike Kotlin's built-in assert(), this function is always evaluated
 * regardless of JVM assertion settings.
 *
 * @param value The condition to check
 * @param lazyMessage Lazy error message generator
 * @throws AssertionError if value is false
 */
internal inline fun assert(
    value: Boolean,
    lazyMessage: () -> String,
) {
    if (!value) {
        val message = lazyMessage()
        throw AssertionError(message)
    }
}
