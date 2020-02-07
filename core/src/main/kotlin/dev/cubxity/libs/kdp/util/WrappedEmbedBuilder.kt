package dev.cubxity.libs.kdp.util

import dev.cubxity.libs.kdp.processing.CommandProcessingContext
import dev.cubxity.libs.kdp.respond.RespondContext
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import java.awt.Color

/**
 * @author Cubxity
 * @since 6/10/2019
 */
class WrappedEmbedBuilder : EmbedBuilder() {

    /**
     * Extension to set title
     */
    var title: String?
        get() = null
        set(value) {
            setTitle(value)
        }

    /**
     * Extension to set color
     */
    var color: Color?
        get() = null
        set(value) {
            setColor(value)
        }

    /**
     * Extension to set footer
     */
    var footer: String?
        get() = null
        set(value) {
            setFooter(value)
        }

    /**
     * Extension to set image
     */
    var image: String?
        get() = null
        set(value) {
            setImage(value)
        }

    /**
     * Alias for appendDescription
     */
    operator fun String.unaryPlus() = appendDescription(this)

    /**
     * Creates a field then configure it with [opt]
     * @param name field's name
     * @param inline
     */
    fun field(name: String, inline: Boolean = false, opt: EasyStringBuilder.() -> Unit) {
        addField(name, EasyStringBuilder().apply(opt).toString(), inline)
    }

}

/**
 * Adds an embed response into [CommandProcessingContext]
 * @param opt callback to configure [WrappedEmbedBuilder]
 */
@BuilderTagMarker
suspend fun CommandProcessingContext.embed(opt: WrappedEmbedBuilder.() -> Unit = {}) =
    WrappedEmbedBuilder().apply(opt).also {
        RespondContext(channel, MessageBuilder(it.build()))
            .also { i -> kdp.respondPipeline.execute(i) }
    }

/**
 * Creates a wrapped embed
 * @param opt callback to configure [WrappedEmbedBuilder]
 */
@BuilderTagMarker
fun wrappedEmbed(opt: WrappedEmbedBuilder.() -> Unit = {}) = WrappedEmbedBuilder().apply(opt).build()