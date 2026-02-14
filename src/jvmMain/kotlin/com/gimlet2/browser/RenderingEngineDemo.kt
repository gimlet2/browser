package com.gimlet2.browser

import com.gimlet2.browser.rendering.*
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.Button
import javafx.scene.control.TextArea
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

/**
 * Demo application showcasing the custom rendering engine
 * Renders HTML/CSS using the from-scratch rendering engine
 */
class RenderingEngineDemo : Application() {
    
    private val renderingEngine = BrowserRenderingEngine()
    private lateinit var canvas: Canvas
    private lateinit var canvasRenderer: CanvasRenderer
    private lateinit var htmlInput: TextArea
    private lateinit var cssInput: TextArea
    
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Custom Rendering Engine Demo"
        
        // Create canvas for rendering
        canvas = Canvas(800.0, 600.0)
        canvasRenderer = CanvasRenderer(canvas)
        
        // Create HTML input
        htmlInput = TextArea().apply {
            promptText = "Enter HTML here..."
            prefHeight = 150.0
            text = """
                <div class="container">
                    <h1 id="title">Custom Rendering Engine</h1>
                    <p class="text">Built from scratch in Kotlin!</p>
                    <div class="box">This is a styled box</div>
                </div>
            """.trimIndent()
        }
        
        // Create CSS input
        cssInput = TextArea().apply {
            promptText = "Enter CSS here..."
            prefHeight = 150.0
            text = """
                .container {
                    background-color: #f0f0f0;
                    padding: 20px;
                    margin: 10px;
                }
                h1 {
                    color: #2c3e50;
                    margin: 10px;
                }
                .text {
                    color: #34495e;
                    margin: 10px;
                }
                .box {
                    background-color: #3498db;
                    color: #ffffff;
                    padding: 15px;
                    margin: 10px;
                    border-color: #2980b9;
                    border-width: 2px;
                }
            """.trimIndent()
        }
        
        // Create render button
        val renderButton = Button("Render").apply {
            setOnAction {
                renderContent()
            }
        }
        
        // Create example buttons
        val example1Button = Button("Example 1: Simple").apply {
            setOnAction {
                htmlInput.text = """
                    <div>
                        <h1>Hello World</h1>
                        <p>This is a simple example.</p>
                    </div>
                """.trimIndent()
                cssInput.text = """
                    div {
                        background-color: #ecf0f1;
                        padding: 20px;
                    }
                    h1 {
                        color: #e74c3c;
                    }
                    p {
                        color: #555;
                    }
                """.trimIndent()
                renderContent()
            }
        }
        
        val example2Button = Button("Example 2: Styled Box").apply {
            setOnAction {
                htmlInput.text = """
                    <div class="card">
                        <h2 class="header">Card Title</h2>
                        <p>Card content goes here</p>
                        <div class="footer">Footer text</div>
                    </div>
                """.trimIndent()
                cssInput.text = """
                    .card {
                        background-color: #ffffff;
                        border-color: #ddd;
                        border-width: 1px;
                        padding: 20px;
                        margin: 10px;
                    }
                    .header {
                        color: #333;
                        background-color: #f8f9fa;
                        padding: 10px;
                        margin: -20px;
                        margin-bottom: 10px;
                    }
                    .footer {
                        color: #666;
                        background-color: #f1f1f1;
                        padding: 10px;
                        margin: 10px;
                        margin-top: 20px;
                    }
                """.trimIndent()
                renderContent()
            }
        }
        
        // Create input panel
        val inputPanel = VBox(10.0).apply {
            children.addAll(
                htmlInput,
                cssInput,
                HBox(10.0).apply {
                    children.addAll(renderButton, example1Button, example2Button)
                }
            )
        }
        
        // Create main layout
        val root = BorderPane().apply {
            center = canvas
            bottom = inputPanel
        }
        
        val scene = Scene(root, 1000.0, 900.0)
        primaryStage.scene = scene
        primaryStage.show()
        
        // Render initial content
        renderContent()
    }
    
    private fun renderContent() {
        try {
            val html = htmlInput.text
            val css = cssInput.text
            
            // Use the custom rendering engine
            val displayList = renderingEngine.render(html, css, 800f, 600f)
            
            // Render to canvas
            canvasRenderer.render(displayList)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * Main entry point for the rendering engine demo
 */
fun main(args: Array<String>) {
    Application.launch(RenderingEngineDemo::class.java, *args)
}
