package com.gimlet2.browser.rendering

/**
 * Simple HTML parser for building DOM tree from HTML string
 * This is a basic implementation that handles common HTML structures
 */
class HtmlParser {
    
    private var position = 0
    private lateinit var input: String
    
    /**
     * Parse HTML string into a Document
     */
    fun parse(html: String): Document {
        input = html
        position = 0
        
        val document = Document()
        
        while (position < input.length) {
            skipWhitespace()
            if (position >= input.length) break
            
            val node = parseNode()
            if (node != null) {
                document.appendChild(node)
            }
        }
        
        return document
    }
    
    /**
     * Parse a single node (element or text)
     */
    private fun parseNode(): DomNode? {
        return when {
            peek() == '<' -> parseElement()
            else -> parseText()
        }
    }
    
    /**
     * Parse an HTML element
     */
    private fun parseElement(): ElementNode? {
        // Opening tag
        if (!consume('<')) return null
        
        val tagName = parseTagName()
        val attributes = parseAttributes()
        
        if (!consume('>')) {
            // Self-closing tag
            if (consume('/') && consume('>')) {
                return ElementNode(tagName, attributes.toMutableMap())
            }
        }
        
        val element = ElementNode(tagName, attributes.toMutableMap())
        
        // Parse children (if not a self-closing tag)
        if (!isSelfClosingTag(tagName)) {
            while (position < input.length) {
                skipWhitespace()
                
                // Check for closing tag
                if (peek() == '<' && peek(1) == '/') {
                    consume('<')
                    consume('/')
                    val closingTag = parseTagName()
                    consume('>')
                    
                    if (closingTag.equals(tagName, ignoreCase = true)) {
                        break
                    }
                } else {
                    val child = parseNode()
                    if (child != null) {
                        element.appendChild(child)
                    }
                }
            }
        }
        
        return element
    }
    
    /**
     * Parse element tag name
     */
    private fun parseTagName(): String {
        val start = position
        while (position < input.length && !input[position].isWhitespace() && 
               input[position] != '>' && input[position] != '/') {
            position++
        }
        return input.substring(start, position)
    }
    
    /**
     * Parse element attributes
     */
    private fun parseAttributes(): Map<String, String> {
        val attributes = mutableMapOf<String, String>()
        
        while (position < input.length) {
            skipWhitespace()
            
            if (peek() == '>' || peek() == '/') break
            
            val name = parseAttributeName()
            skipWhitespace()
            
            if (consume('=')) {
                skipWhitespace()
                val value = parseAttributeValue()
                attributes[name] = value
            } else {
                attributes[name] = ""
            }
        }
        
        return attributes
    }
    
    /**
     * Parse attribute name
     */
    private fun parseAttributeName(): String {
        val start = position
        while (position < input.length && !input[position].isWhitespace() && 
               input[position] != '=' && input[position] != '>' && input[position] != '/') {
            position++
        }
        return input.substring(start, position)
    }
    
    /**
     * Parse attribute value (handles quoted and unquoted values)
     */
    private fun parseAttributeValue(): String {
        if (peek() == '"' || peek() == '\'') {
            val quote = peek()
            consume(quote)
            val start = position
            while (position < input.length && peek() != quote) {
                position++
            }
            val value = input.substring(start, position)
            consume(quote)
            return value
        } else {
            val start = position
            while (position < input.length && !input[position].isWhitespace() && 
                   input[position] != '>') {
                position++
            }
            return input.substring(start, position)
        }
    }
    
    /**
     * Parse text node
     */
    private fun parseText(): TextNode? {
        val start = position
        while (position < input.length && peek() != '<') {
            position++
        }
        
        val text = input.substring(start, position).trim()
        return if (text.isNotEmpty()) TextNode(text) else null
    }
    
    /**
     * Skip whitespace characters
     */
    private fun skipWhitespace() {
        while (position < input.length && input[position].isWhitespace()) {
            position++
        }
    }
    
    /**
     * Peek at current character
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
    
    /**
     * Check if tag is self-closing (void element)
     */
    private fun isSelfClosingTag(tagName: String): Boolean {
        val selfClosingTags = setOf(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr"
        )
        return selfClosingTags.contains(tagName.lowercase())
    }
}
