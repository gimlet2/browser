package com.gimlet2.browser.rendering

/**
 * CSS (Cascading Style Sheets) representation and parser
 */

/**
 * CSS value types
 */
sealed class CssValue {
    data class Keyword(val value: String) : CssValue()
    data class Length(val value: Float, val unit: CssUnit) : CssValue()
    data class Color(val r: Int, val g: Int, val b: Int, val a: Float = 1.0f) : CssValue()
}

/**
 * CSS length units
 */
enum class CssUnit {
    PX,  // Pixels
    EM,  // Relative to font size
    REM, // Relative to root font size
    PERCENT // Percentage
}

/**
 * CSS selector types
 */
sealed class Selector {
    data class Simple(val tagName: String? = null, val id: String? = null, val classes: List<String> = emptyList()) : Selector()
}

/**
 * CSS declaration (property: value)
 */
data class Declaration(
    val property: String,
    val value: CssValue
)

/**
 * CSS rule (selector + declarations)
 */
data class Rule(
    val selectors: List<Selector>,
    val declarations: List<Declaration>
)

/**
 * CSS stylesheet
 */
data class Stylesheet(
    val rules: List<Rule>
)

/**
 * Simple CSS parser
 */
class CssParser {
    
    private var position = 0
    private lateinit var input: String
    
    /**
     * Parse CSS string into a Stylesheet
     */
    fun parse(css: String): Stylesheet {
        input = css
        position = 0
        
        val rules = mutableListOf<Rule>()
        
        while (position < input.length) {
            skipWhitespace()
            if (position >= input.length) break
            
            val rule = parseRule()
            if (rule != null) {
                rules.add(rule)
            }
        }
        
        return Stylesheet(rules)
    }
    
    /**
     * Parse a CSS rule
     */
    private fun parseRule(): Rule? {
        val selectors = parseSelectors()
        if (selectors.isEmpty()) return null
        
        skipWhitespace()
        if (!consume('{')) return null
        
        val declarations = parseDeclarations()
        
        skipWhitespace()
        consume('}')
        
        return Rule(selectors, declarations)
    }
    
    /**
     * Parse selectors (comma-separated)
     */
    private fun parseSelectors(): List<Selector> {
        val selectors = mutableListOf<Selector>()
        
        while (position < input.length) {
            skipWhitespace()
            if (peek() == '{') break
            
            val selector = parseSimpleSelector()
            if (selector != null) {
                selectors.add(selector)
            }
            
            skipWhitespace()
            if (!consume(',')) break
        }
        
        return selectors
    }
    
    /**
     * Parse a simple selector (tag, #id, .class)
     */
    private fun parseSimpleSelector(): Selector? {
        var tagName: String? = null
        var id: String? = null
        val classes = mutableListOf<String>()
        
        while (position < input.length) {
            skipWhitespace()
            
            when (peek()) {
                '#' -> {
                    position++
                    id = parseIdentifier()
                }
                '.' -> {
                    position++
                    classes.add(parseIdentifier())
                }
                '{', ',' -> break
                else -> {
                    if (tagName == null && peek().isLetter()) {
                        tagName = parseIdentifier()
                    } else {
                        break
                    }
                }
            }
        }
        
        if (tagName == null && id == null && classes.isEmpty()) {
            return null
        }
        
        return Selector.Simple(tagName, id, classes)
    }
    
    /**
     * Parse CSS declarations
     */
    private fun parseDeclarations(): List<Declaration> {
        val declarations = mutableListOf<Declaration>()
        
        while (position < input.length) {
            skipWhitespace()
            if (peek() == '}') break
            
            val declaration = parseDeclaration()
            if (declaration != null) {
                declarations.add(declaration)
            }
            
            skipWhitespace()
            consume(';')
        }
        
        return declarations
    }
    
    /**
     * Parse a single declaration
     */
    private fun parseDeclaration(): Declaration? {
        val property = parseIdentifier()
        if (property.isEmpty()) return null
        
        skipWhitespace()
        if (!consume(':')) return null
        
        skipWhitespace()
        val value = parseValue()
        
        return Declaration(property, value)
    }
    
    /**
     * Parse CSS value
     */
    private fun parseValue(): CssValue {
        skipWhitespace()
        
        // Try to parse color
        if (peek() == '#') {
            return parseColor()
        }
        
        // Try to parse length
        val start = position
        while (position < input.length && (peek().isDigit() || peek() == '.')) {
            position++
        }
        
        if (position > start) {
            val numStr = input.substring(start, position)
            val num = numStr.toFloatOrNull() ?: 0f
            
            val unit = parseUnit()
            return CssValue.Length(num, unit)
        }
        
        // Parse as keyword
        val keyword = parseIdentifier()
        return CssValue.Keyword(keyword)
    }
    
    /**
     * Parse CSS color value
     */
    private fun parseColor(): CssValue.Color {
        consume('#')
        val start = position
        
        while (position < input.length && peek().isLetterOrDigit()) {
            position++
        }
        
        val hex = input.substring(start, position)
        
        return when (hex.length) {
            3 -> {
                val r = hex[0].toString().repeat(2).toInt(16)
                val g = hex[1].toString().repeat(2).toInt(16)
                val b = hex[2].toString().repeat(2).toInt(16)
                CssValue.Color(r, g, b)
            }
            6 -> {
                val r = hex.substring(0, 2).toInt(16)
                val g = hex.substring(2, 4).toInt(16)
                val b = hex.substring(4, 6).toInt(16)
                CssValue.Color(r, g, b)
            }
            else -> CssValue.Color(0, 0, 0)
        }
    }
    
    /**
     * Parse CSS unit
     */
    private fun parseUnit(): CssUnit {
        val unit = parseIdentifier().lowercase()
        return when (unit) {
            "px" -> CssUnit.PX
            "em" -> CssUnit.EM
            "rem" -> CssUnit.REM
            "%" -> CssUnit.PERCENT
            else -> CssUnit.PX
        }
    }
    
    /**
     * Parse identifier (alphanumeric + dash + underscore)
     */
    private fun parseIdentifier(): String {
        val start = position
        while (position < input.length && 
               (peek().isLetterOrDigit() || peek() == '-' || peek() == '_')) {
            position++
        }
        return input.substring(start, position)
    }
    
    /**
     * Skip whitespace and comments
     */
    private fun skipWhitespace() {
        while (position < input.length) {
            if (input[position].isWhitespace()) {
                position++
            } else if (peek() == '/' && peek(1) == '*') {
                // Skip CSS comment
                position += 2
                while (position < input.length - 1) {
                    if (peek() == '*' && peek(1) == '/') {
                        position += 2
                        break
                    }
                    position++
                }
            } else {
                break
            }
        }
    }
    
    /**
     * Peek at character
     */
    private fun peek(offset: Int = 0): Char {
        val pos = position + offset
        return if (pos < input.length) input[pos] else '\u0000'
    }
    
    /**
     * Consume expected character
     */
    private fun consume(expected: Char): Boolean {
        if (peek() == expected) {
            position++
            return true
        }
        return false
    }
}
