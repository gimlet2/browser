package com.gimlet2.browser

import com.gimlet2.browser.rendering.*
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color as FxColor
import javafx.scene.text.Font

/**
 * JavaFX Canvas renderer for the custom rendering engine
 * Renders display commands from the rendering engine to a JavaFX Canvas
 */
class CanvasRenderer(private val canvas: Canvas) {
    
    private val gc: GraphicsContext = canvas.graphicsContext2D
    
    /**
     * Render a display list to the canvas
     */
    fun render(displayList: DisplayList) {
        // Clear canvas
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)
        gc.fill = FxColor.WHITE
        gc.fillRect(0.0, 0.0, canvas.width, canvas.height)
        
        // Render each command
        for (command in displayList.commands) {
            renderCommand(command)
        }
    }
    
    /**
     * Render a single display command
     */
    private fun renderCommand(command: DisplayCommand) {
        when (command) {
            is DisplayCommand.SolidColor -> {
                val color = cssColorToFx(command.color)
                gc.fill = color
                gc.fillRect(
                    command.rect.x.toDouble(),
                    command.rect.y.toDouble(),
                    command.rect.width.toDouble(),
                    command.rect.height.toDouble()
                )
            }
            is DisplayCommand.Text -> {
                val color = cssColorToFx(command.color)
                gc.fill = color
                gc.font = Font.font(16.0) // Default font size
                gc.fillText(
                    command.text,
                    command.rect.x.toDouble(),
                    command.rect.y.toDouble() + 16.0 // Baseline offset
                )
            }
            is DisplayCommand.Rectangle -> {
                val color = cssColorToFx(command.borderColor)
                gc.stroke = color
                gc.lineWidth = command.borderWidth.toDouble()
                gc.strokeRect(
                    command.rect.x.toDouble(),
                    command.rect.y.toDouble(),
                    command.rect.width.toDouble(),
                    command.rect.height.toDouble()
                )
            }
        }
    }
    
    /**
     * Convert CSS color to JavaFX color
     */
    private fun cssColorToFx(color: CssValue.Color): FxColor {
        return FxColor.rgb(color.r, color.g, color.b, color.a.toDouble())
    }
}
