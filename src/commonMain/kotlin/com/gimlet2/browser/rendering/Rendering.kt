package com.gimlet2.browser.rendering

/**
 * Rendering engine - paints layout boxes to display commands
 * This generates a list of display commands that can be rendered by different backends
 */

/**
 * Display command - represents a drawing operation
 */
sealed class DisplayCommand {
    data class SolidColor(
        val color: CssValue.Color,
        val rect: Rect
    ) : DisplayCommand()
    
    data class Text(
        val text: String,
        val rect: Rect,
        val color: CssValue.Color = CssValue.Color(0, 0, 0)
    ) : DisplayCommand()
    
    data class Rectangle(
        val rect: Rect,
        val borderColor: CssValue.Color,
        val borderWidth: Float
    ) : DisplayCommand()
}

/**
 * Display list - ordered list of display commands
 */
data class DisplayList(
    val commands: List<DisplayCommand>
)

/**
 * Rendering engine
 */
class RenderingEngine {
    
    /**
     * Build display list from layout tree
     */
    fun paint(layoutBox: LayoutBox): DisplayList {
        val commands = mutableListOf<DisplayCommand>()
        renderLayoutBox(layoutBox, commands)
        return DisplayList(commands)
    }
    
    /**
     * Render a layout box and its children
     */
    private fun renderLayoutBox(layoutBox: LayoutBox, commands: MutableList<DisplayCommand>) {
        // Render background
        renderBackground(layoutBox, commands)
        
        // Render borders
        renderBorders(layoutBox, commands)
        
        // Render content (text)
        renderContent(layoutBox, commands)
        
        // Render children
        for (child in layoutBox.children) {
            renderLayoutBox(child, commands)
        }
    }
    
    /**
     * Render background color
     */
    private fun renderBackground(layoutBox: LayoutBox, commands: MutableList<DisplayCommand>) {
        val backgroundColor = layoutBox.styledNode?.getValue("background-color")
        if (backgroundColor is CssValue.Color) {
            commands.add(
                DisplayCommand.SolidColor(
                    backgroundColor,
                    layoutBox.dimensions.paddingBox()
                )
            )
        }
    }
    
    /**
     * Render borders
     */
    private fun renderBorders(layoutBox: LayoutBox, commands: MutableList<DisplayCommand>) {
        val borderColor = layoutBox.styledNode?.getValue("border-color")
        if (borderColor is CssValue.Color) {
            val d = layoutBox.dimensions
            val pb = d.paddingBox()
            
            // Top border
            if (d.border.top > 0) {
                commands.add(
                    DisplayCommand.Rectangle(
                        Rect(pb.x, pb.y, pb.width, d.border.top),
                        borderColor,
                        d.border.top
                    )
                )
            }
            
            // Right border
            if (d.border.right > 0) {
                commands.add(
                    DisplayCommand.Rectangle(
                        Rect(pb.x + pb.width - d.border.right, pb.y, d.border.right, pb.height),
                        borderColor,
                        d.border.right
                    )
                )
            }
            
            // Bottom border
            if (d.border.bottom > 0) {
                commands.add(
                    DisplayCommand.Rectangle(
                        Rect(pb.x, pb.y + pb.height - d.border.bottom, pb.width, d.border.bottom),
                        borderColor,
                        d.border.bottom
                    )
                )
            }
            
            // Left border
            if (d.border.left > 0) {
                commands.add(
                    DisplayCommand.Rectangle(
                        Rect(pb.x, pb.y, d.border.left, pb.height),
                        borderColor,
                        d.border.left
                    )
                )
            }
        }
    }
    
    /**
     * Render text content
     */
    private fun renderContent(layoutBox: LayoutBox, commands: MutableList<DisplayCommand>) {
        val node = layoutBox.styledNode?.node
        if (node is TextNode) {
            val color = layoutBox.styledNode.getValue("color") as? CssValue.Color 
                ?: CssValue.Color(0, 0, 0)
            
            commands.add(
                DisplayCommand.Text(
                    node.text,
                    layoutBox.dimensions.content,
                    color
                )
            )
        }
    }
}

/**
 * Simple renderer that outputs to a canvas or buffer
 * This is a placeholder for actual rendering implementation
 */
class SimpleRenderer {
    
    /**
     * Render display list to string representation (for testing)
     */
    fun renderToString(displayList: DisplayList): String {
        val sb = StringBuilder()
        sb.appendLine("Display List:")
        for (command in displayList.commands) {
            when (command) {
                is DisplayCommand.SolidColor -> {
                    sb.appendLine("  - Fill rect (${command.rect.x}, ${command.rect.y}, " +
                        "${command.rect.width}, ${command.rect.height}) " +
                        "with color rgb(${command.color.r}, ${command.color.g}, ${command.color.b})")
                }
                is DisplayCommand.Text -> {
                    sb.appendLine("  - Draw text '${command.text}' at " +
                        "(${command.rect.x}, ${command.rect.y})")
                }
                is DisplayCommand.Rectangle -> {
                    sb.appendLine("  - Draw border (${command.rect.x}, ${command.rect.y}, " +
                        "${command.rect.width}, ${command.rect.height}) " +
                        "width ${command.borderWidth}")
                }
            }
        }
        return sb.toString()
    }
}
