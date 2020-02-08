package dev.cubxity.libs.kdp.util

/**
 * Wraps [text] in bold
 */
fun bold(text: String, escape: Boolean = true): String {
    var txt = text
    if (escape)
        txt = txt.replace("**", "\\*")
    return "**$txt**"
}


/**
 * Wraps text produced in [opt] in bold
 * Note: text produced in [opt] might breaks the bold
 */
fun bold(opt: MarkdownBuilder.() -> Unit) = "**${MarkdownBuilder().apply(opt)}**"

/**
 * Wraps [text] in italic
 */
fun italic(text: String, escape: Boolean = true): String {
    var txt = text
    if (escape)
        txt = txt.replace("*", "\\*")
    return "*$txt*"
}

/**
 * Wraps text produced in [opt] in italic
 * Note: text produced in [opt] might breaks the italic
 */
fun italic(opt: MarkdownBuilder.() -> Unit) = "*${MarkdownBuilder().apply(opt)}*"

/**
 * Wraps [text] in underline
 */
fun underline(text: String, escape: Boolean = true): String {
    var txt = text
    if (escape)
        txt = txt.replace("__", "\\__")
    return "__${txt}__"
}

/**
 * Wraps text produced in [opt] in underline
 * Note: text produced in [opt] might breaks the underline
 */
fun underline(opt: MarkdownBuilder.() -> Unit) = "__${MarkdownBuilder().apply(opt)}__"

/**
 * Wraps [text] in code
 */
fun code(text: String, escape: Boolean = true): String {
    var txt = text
    if (escape)
        txt = txt.replace("`", "\\`")
    return "`$txt`"
}

/**
 * Wraps text produced in [opt] in code
 * Note: text produced in [opt] might breaks the code
 */
fun code(opt: MarkdownBuilder.() -> Unit) = "`${MarkdownBuilder().apply(opt)}`"

/**
 * Wraps [text] in codeBlock
 */
fun codeBlock(text: String, escape: Boolean = true): String {
    var txt = text
    if (escape)
        txt = txt.replace("```", "\\```")
    return "```$txt```"
}

/**
 * Wraps text produced in [opt] in codeBlock
 * Note: text produced in [opt] might breaks the codeBlock
 */
fun codeBlock(language: String = "", opt: MarkdownBuilder.() -> Unit) = "```$language\n${MarkdownBuilder().apply(opt)}```"

/**
 * Creates a link
 */
fun link(alt: String, url: String) = "[$alt]($url)"