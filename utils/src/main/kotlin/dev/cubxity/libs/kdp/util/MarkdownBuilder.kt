package dev.cubxity.libs.kdp.util

/**
 * @author Cubxity
 * @since 6/10/2019
 */
class MarkdownBuilder {
    private val builder = StringBuilder()

    operator fun String.unaryPlus() {
        builder.append(this)
    }

    /**
     * Appends [s] into [builder]
     * @return this instance for chaining
     */
    fun append(s: String): MarkdownBuilder {
        builder.append(s)
        return this
    }

    /**
     * Returns [builder]
     */
    override fun toString() = builder.toString()
}