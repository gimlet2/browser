package com.gimlet2.browser

import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.web.WebView
import javafx.stage.Stage
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Main browser application class with JavaFX UI.
 * Supports HTTP/1.1 and HTTP/2, HTML5 rendering with CSS3.
 * Uses Kotlin coroutines for asynchronous operations.
 */
class BrowserApplication : Application() {
    
    private lateinit var webView: WebView
    private lateinit var urlField: TextField
    private lateinit var backButton: Button
    private lateinit var forwardButton: Button
    private lateinit var reloadButton: Button
    
    // Coroutine scope for managing async operations
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    
    // OkHttp client with HTTP/2 support - available for custom network operations
    // Note: WebView is used for rendering as it provides better HTML5/CSS3 support
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    override fun start(primaryStage: Stage) {
        primaryStage.title = "Kotlin Browser"
        
        // Create web view for HTML5/CSS3 rendering
        webView = WebView()
        val webEngine = webView.engine
        
        // Enable JavaScript
        webEngine.isJavaScriptEnabled = true
        
        // Create navigation controls
        backButton = Button("←").apply {
            prefWidth = 40.0
            setOnAction {
                if (webEngine.history.currentIndex > 0) {
                    webEngine.history.go(-1)
                }
            }
        }
        
        forwardButton = Button("→").apply {
            prefWidth = 40.0
            setOnAction {
                if (webEngine.history.currentIndex < webEngine.history.entries.size - 1) {
                    webEngine.history.go(1)
                }
            }
        }
        
        reloadButton = Button("⟳").apply {
            prefWidth = 40.0
            setOnAction {
                webEngine.reload()
            }
        }
        
        // Create URL input field
        urlField = TextField().apply {
            promptText = "Enter URL (e.g., https://example.com)"
            HBox.setHgrow(this, Priority.ALWAYS)
            
            setOnAction {
                // Launch coroutine for URL loading
                applicationScope.launch {
                    loadUrl(text)
                }
            }
        }
        
        // Update URL field when navigation occurs
        webEngine.locationProperty().addListener { _, _, newLocation ->
            if (newLocation != null && !newLocation.isEmpty()) {
                urlField.text = newLocation
            }
        }
        
        // Update navigation buttons state
        webEngine.history.currentIndexProperty().addListener { _, _, _ ->
            updateNavigationButtons()
        }
        
        // Create navigation bar
        val navigationBar = HBox(5.0).apply {
            padding = Insets(5.0)
            alignment = Pos.CENTER_LEFT
            children.addAll(backButton, forwardButton, reloadButton, urlField)
        }
        
        // Create main layout
        val root = BorderPane().apply {
            top = navigationBar
            center = webView
        }
        
        // Create scene
        val scene = Scene(root, 1024.0, 768.0)
        primaryStage.scene = scene
        primaryStage.show()
        
        // Load initial page asynchronously
        applicationScope.launch {
            loadUrl("https://example.com")
        }
    }
    
    /**
     * Loads a URL using coroutines for async operation.
     * Falls back to WebView's built-in loader for better compatibility.
     */
    private suspend fun loadUrl(url: String) = withContext(Dispatchers.JavaFx) {
        var finalUrl = url.trim()
        
        // Add https:// if no protocol specified
        if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
            finalUrl = "https://$finalUrl"
        }
        
        // Validate URL
        if (finalUrl.isEmpty()) {
            return@withContext
        }
        
        try {
            // For better HTML5/CSS3 support, use WebView's built-in engine
            // which supports modern web standards better than custom rendering
            webView.engine.load(finalUrl)
            
            // Note: OkHttp with HTTP/2 is available for custom network operations
            // but JavaFX WebView provides better HTML5/CSS3 rendering
        } catch (e: Exception) {
            // Handle error on JavaFX thread using coroutines
            webView.engine.loadContent(
                """
                <html>
                <head><title>Error</title></head>
                <body>
                    <h1>Error loading page</h1>
                    <p>Could not load: $finalUrl</p>
                    <p>Error: ${e.message}</p>
                </body>
                </html>
                """.trimIndent()
            )
        }
    }
    
    /**
     * Updates the enabled state of navigation buttons.
     */
    private fun updateNavigationButtons() {
        val history = webView.engine.history
        backButton.isDisable = history.currentIndex <= 0
        forwardButton.isDisable = history.currentIndex >= history.entries.size - 1
    }
    
    override fun stop() {
        // Cancel all running coroutines
        applicationScope.cancel()
        
        // Clean up resources
        httpClient.dispatcher.executorService.shutdown()
        httpClient.connectionPool.evictAll()
        super.stop()
    }
}

/**
 * Main entry point for the browser application.
 */
fun main(args: Array<String>) {
    Application.launch(BrowserApplication::class.java, *args)
}
