package com.gimlet2.browser.rendering

/**
 * DOM (Document Object Model) representation
 * This is the tree structure that represents the HTML document
 */

/**
 * Base class for all DOM nodes
 */
sealed class DomNode {
    abstract val children: MutableList<DomNode>
    
    fun appendChild(child: DomNode) {
        children.add(child)
    }
}

/**
 * Element node representing an HTML element
 */
data class ElementNode(
    val tagName: String,
    val attributes: MutableMap<String, String> = mutableMapOf(),
    override val children: MutableList<DomNode> = mutableListOf()
) : DomNode() {
    
    fun getAttribute(name: String): String? = attributes[name]
    
    fun setAttribute(name: String, value: String) {
        attributes[name] = value
    }
    
    fun id(): String? = attributes["id"]
    
    fun classes(): List<String> {
        return attributes["class"]?.split(" ")?.filter { it.isNotEmpty() } ?: emptyList()
    }
}

/**
 * Text node representing text content
 */
data class TextNode(
    val text: String,
    override val children: MutableList<DomNode> = mutableListOf()
) : DomNode()

/**
 * Document root node
 */
data class Document(
    override val children: MutableList<DomNode> = mutableListOf()
) : DomNode() {
    
    fun getElementByTagName(tagName: String): ElementNode? {
        return findElementByTagName(this, tagName)
    }
    
    private fun findElementByTagName(node: DomNode, tagName: String): ElementNode? {
        if (node is ElementNode && node.tagName.equals(tagName, ignoreCase = true)) {
            return node
        }
        for (child in node.children) {
            val result = findElementByTagName(child, tagName)
            if (result != null) return result
        }
        return null
    }
    
    fun getElementById(id: String): ElementNode? {
        return findElementById(this, id)
    }
    
    private fun findElementById(node: DomNode, id: String): ElementNode? {
        if (node is ElementNode && node.id() == id) {
            return node
        }
        for (child in node.children) {
            val result = findElementById(child, id)
            if (result != null) return result
        }
        return null
    }
}
