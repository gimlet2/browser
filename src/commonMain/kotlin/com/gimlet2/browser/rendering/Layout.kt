package com.gimlet2.browser.rendering

/**
 * Layout engine - computes positions and dimensions for elements
 * Implements a simplified box model
 */

/**
 * Rectangle representing position and dimensions
 */
data class Rect(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float
)

/**
 * Edge sizes (margins, borders, padding)
 */
data class EdgeSizes(
    var top: Float = 0f,
    var right: Float = 0f,
    var bottom: Float = 0f,
    var left: Float = 0f
)

/**
 * Box dimensions including content, padding, border, and margin
 */
data class Dimensions(
    var content: Rect = Rect(0f, 0f, 0f, 0f),
    var padding: EdgeSizes = EdgeSizes(),
    var border: EdgeSizes = EdgeSizes(),
    var margin: EdgeSizes = EdgeSizes()
) {
    /**
     * Total width including padding and border
     */
    fun paddingBox(): Rect {
        return Rect(
            content.x - padding.left,
            content.y - padding.top,
            content.width + padding.left + padding.right,
            content.height + padding.top + padding.bottom
        )
    }
    
    /**
     * Total width including padding, border, and margin
     */
    fun marginBox(): Rect {
        val pb = paddingBox()
        return Rect(
            pb.x - border.left - margin.left,
            pb.y - border.top - margin.top,
            pb.width + border.left + border.right + margin.left + margin.right,
            pb.height + border.top + border.bottom + margin.top + margin.bottom
        )
    }
}

/**
 * Styled node - combines DOM node with matched CSS rules
 */
data class StyledNode(
    val node: DomNode,
    val styles: Map<String, CssValue>,
    val children: List<StyledNode>
) {
    fun getValue(property: String): CssValue? = styles[property]
    
    fun getDisplay(): String {
        return when (val value = getValue("display")) {
            is CssValue.Keyword -> value.value
            else -> "block"
        }
    }
}

/**
 * Layout box - represents a box in the layout tree
 */
sealed class BoxType {
    object BlockBox : BoxType()
    object InlineBox : BoxType()
    object AnonymousBox : BoxType()
}

data class LayoutBox(
    val boxType: BoxType,
    val styledNode: StyledNode?,
    var dimensions: Dimensions = Dimensions(),
    val children: MutableList<LayoutBox> = mutableListOf()
)

/**
 * Layout engine
 */
class LayoutEngine {
    
    /**
     * Build layout tree from styled node tree
     */
    fun layout(styledNode: StyledNode, containingBlock: Dimensions): LayoutBox {
        val root = buildLayoutTree(styledNode)
        
        // Set initial containing block dimensions
        root.dimensions.content.width = containingBlock.content.width
        
        // Perform layout
        layoutBlock(root, containingBlock)
        
        return root
    }
    
    /**
     * Build layout tree from styled node
     */
    private fun buildLayoutTree(styledNode: StyledNode): LayoutBox {
        val boxType = when (styledNode.getDisplay()) {
            "block" -> BoxType.BlockBox
            "inline" -> BoxType.InlineBox
            "none" -> return LayoutBox(BoxType.BlockBox, null) // Empty box for display:none
            else -> BoxType.BlockBox
        }
        
        val layoutBox = LayoutBox(boxType, styledNode)
        
        // Recursively build layout tree for children
        for (child in styledNode.children) {
            val childDisplay = child.getDisplay()
            if (childDisplay != "none") {
                val childBox = buildLayoutTree(child)
                layoutBox.children.add(childBox)
            }
        }
        
        return layoutBox
    }
    
    /**
     * Layout a block-level element
     */
    private fun layoutBlock(box: LayoutBox, containingBlock: Dimensions) {
        // Calculate width
        calculateBlockWidth(box, containingBlock)
        
        // Calculate position
        calculateBlockPosition(box, containingBlock)
        
        // Layout children
        layoutBlockChildren(box)
        
        // Calculate height
        calculateBlockHeight(box)
    }
    
    /**
     * Calculate width for block element
     */
    private fun calculateBlockWidth(box: LayoutBox, containingBlock: Dimensions) {
        val style = box.styledNode?.styles ?: return
        
        // Default to auto
        var width = style["width"]
        
        // Calculate margin, border, padding
        val zero = CssValue.Length(0f, CssUnit.PX)
        
        val marginLeft = style["margin-left"] ?: style["margin"] ?: zero
        val marginRight = style["margin-right"] ?: style["margin"] ?: zero
        val borderLeft = style["border-left-width"] ?: style["border-width"] ?: zero
        val borderRight = style["border-right-width"] ?: style["border-width"] ?: zero
        val paddingLeft = style["padding-left"] ?: style["padding"] ?: zero
        val paddingRight = style["padding-right"] ?: style["padding"] ?: zero
        
        val total = sumLengths(listOf(
            marginLeft, marginRight,
            borderLeft, borderRight,
            paddingLeft, paddingRight,
            width ?: zero
        ))
        
        // If width is not auto and total > container width, treat margins as 0
        val containerWidth = containingBlock.content.width
        
        if (width != null && total > containerWidth) {
            box.dimensions.margin.left = 0f
            box.dimensions.margin.right = 0f
        } else {
            box.dimensions.margin.left = toPx(marginLeft)
            box.dimensions.margin.right = toPx(marginRight)
        }
        
        box.dimensions.border.left = toPx(borderLeft)
        box.dimensions.border.right = toPx(borderRight)
        box.dimensions.padding.left = toPx(paddingLeft)
        box.dimensions.padding.right = toPx(paddingRight)
        
        // Calculate content width
        if (width == null || width is CssValue.Keyword && width.value == "auto") {
            val used = box.dimensions.margin.left + box.dimensions.margin.right +
                       box.dimensions.border.left + box.dimensions.border.right +
                       box.dimensions.padding.left + box.dimensions.padding.right
            box.dimensions.content.width = containerWidth - used
        } else {
            box.dimensions.content.width = toPx(width)
        }
    }
    
    /**
     * Calculate position for block element
     */
    private fun calculateBlockPosition(box: LayoutBox, containingBlock: Dimensions) {
        val style = box.styledNode?.styles ?: return
        val zero = CssValue.Length(0f, CssUnit.PX)
        
        // Margin, border, padding for top/bottom
        box.dimensions.margin.top = toPx(style["margin-top"] ?: style["margin"] ?: zero)
        box.dimensions.margin.bottom = toPx(style["margin-bottom"] ?: style["margin"] ?: zero)
        
        box.dimensions.border.top = toPx(style["border-top-width"] ?: style["border-width"] ?: zero)
        box.dimensions.border.bottom = toPx(style["border-bottom-width"] ?: style["border-width"] ?: zero)
        
        box.dimensions.padding.top = toPx(style["padding-top"] ?: style["padding"] ?: zero)
        box.dimensions.padding.bottom = toPx(style["padding-bottom"] ?: style["padding"] ?: zero)
        
        // Position relative to container
        box.dimensions.content.x = containingBlock.content.x +
                                    box.dimensions.margin.left +
                                    box.dimensions.border.left +
                                    box.dimensions.padding.left
        
        box.dimensions.content.y = containingBlock.content.y +
                                    containingBlock.content.height +
                                    box.dimensions.margin.top +
                                    box.dimensions.border.top +
                                    box.dimensions.padding.top
    }
    
    /**
     * Layout children of block element
     */
    private fun layoutBlockChildren(box: LayoutBox) {
        for (child in box.children) {
            layoutBlock(child, box.dimensions)
            // Increase height to accommodate child
            box.dimensions.content.height += child.dimensions.marginBox().height
        }
    }
    
    /**
     * Calculate height for block element
     */
    private fun calculateBlockHeight(box: LayoutBox) {
        // If height is explicitly set, use that
        box.styledNode?.getValue("height")?.let { height ->
            box.dimensions.content.height = toPx(height)
        }
        // Otherwise, height was calculated during child layout
    }
    
    /**
     * Convert CSS value to pixels
     */
    private fun toPx(value: CssValue): Float {
        return when (value) {
            is CssValue.Length -> {
                when (value.unit) {
                    CssUnit.PX -> value.value
                    CssUnit.EM -> value.value * 16f // Assume 16px base font size
                    CssUnit.REM -> value.value * 16f
                    CssUnit.PERCENT -> value.value // Would need parent context
                }
            }
            else -> 0f
        }
    }
    
    /**
     * Sum multiple length values
     */
    private fun sumLengths(values: List<CssValue>): Float {
        return values.sumOf { toPx(it).toDouble() }.toFloat()
    }
}

/**
 * Style tree builder - matches CSS rules to DOM nodes
 */
class StyleTreeBuilder {
    
    /**
     * Build styled tree by matching CSS rules to DOM
     */
    fun styleTree(node: DomNode, stylesheet: Stylesheet): StyledNode? {
        return when (node) {
            is ElementNode -> {
                val styles = matchedRules(node, stylesheet)
                val children = node.children.mapNotNull { styleTree(it, stylesheet) }
                StyledNode(node, styles, children)
            }
            is TextNode -> {
                // Text nodes don't have styles
                StyledNode(node, emptyMap(), emptyList())
            }
            is Document -> {
                // Style document children
                val children = node.children.mapNotNull { styleTree(it, stylesheet) }
                StyledNode(node, emptyMap(), children)
            }
        }
    }
    
    /**
     * Match CSS rules to element and compute final styles
     */
    private fun matchedRules(element: ElementNode, stylesheet: Stylesheet): Map<String, CssValue> {
        val styles = mutableMapOf<String, CssValue>()
        
        for (rule in stylesheet.rules) {
            for (selector in rule.selectors) {
                if (matches(element, selector)) {
                    for (declaration in rule.declarations) {
                        styles[declaration.property] = declaration.value
                    }
                }
            }
        }
        
        return styles
    }
    
    /**
     * Check if selector matches element
     */
    private fun matches(element: ElementNode, selector: Selector): Boolean {
        return when (selector) {
            is Selector.Simple -> {
                // Check tag name
                if (selector.tagName != null && 
                    !element.tagName.equals(selector.tagName, ignoreCase = true)) {
                    return false
                }
                
                // Check id
                if (selector.id != null && element.id() != selector.id) {
                    return false
                }
                
                // Check classes
                val elementClasses = element.classes().toSet()
                for (selectorClass in selector.classes) {
                    if (!elementClasses.contains(selectorClass)) {
                        return false
                    }
                }
                
                true
            }
        }
    }
}
