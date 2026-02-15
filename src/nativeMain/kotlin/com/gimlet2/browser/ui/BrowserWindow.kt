@file:OptIn(ExperimentalForeignApi::class)

package com.gimlet2.browser.ui

import kotlinx.cinterop.*
import sdl2.*

/**
 * Browser window using SDL2
 * Displays framebuffer content in a native window with URL bar
 */
class BrowserWindow(
    private val width: Int,
    private val height: Int,
    private val title: String = "Kotlin-Native Browser"
) {
    private var window: CPointer<SDL_Window>? = null
    private var renderer: CPointer<SDL_Renderer>? = null
    private var texture: CPointer<SDL_Texture>? = null
    private var isRunning = false
    
    /**
     * Initialize SDL2 and create window
     */
    fun init(): Boolean {
        if (SDL_Init(SDL_INIT_VIDEO) < 0) {
            println("SDL could not initialize! SDL_Error: ${SDL_GetError()?.toKString()}")
            return false
        }
        
        window = SDL_CreateWindow(
            title,
            SDL_WINDOWPOS_CENTERED,
            SDL_WINDOWPOS_CENTERED,
            width,
            height,
            SDL_WINDOW_SHOWN.toUInt()
        )
        
        if (window == null) {
            println("Window could not be created! SDL_Error: ${SDL_GetError()?.toKString()}")
            return false
        }
        
        renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED.toUInt())
        if (renderer == null) {
            println("Renderer could not be created! SDL_Error: ${SDL_GetError()?.toKString()}")
            return false
        }
        
        texture = SDL_CreateTexture(
            renderer,
            SDL_PIXELFORMAT_RGB24.toUInt(),
            SDL_TEXTUREACCESS_STREAMING,
            width,
            height
        )
        
        if (texture == null) {
            println("Texture could not be created! SDL_Error: ${SDL_GetError()?.toKString()}")
            return false
        }
        
        isRunning = true
        println("SDL2 window initialized successfully")
        return true
    }
    
    /**
     * Update window with framebuffer content
     */
    fun updateFramebuffer(framebuffer: Framebuffer) {
        if (texture == null || renderer == null) return
        
        val pixels = framebuffer.getPixels()
        
        pixels.usePinned { pinned ->
            SDL_UpdateTexture(
                texture,
                null,
                pinned.addressOf(0),
                width * 3
            )
        }
        
        SDL_RenderClear(renderer)
        SDL_RenderCopy(renderer, texture, null, null)
        SDL_RenderPresent(renderer)
    }
    
    /**
     * Process events and run main loop
     * Returns true if window should continue running
     */
    fun processEvents(): Boolean {
        memScoped {
            val event = alloc<SDL_Event>()
            
            while (SDL_PollEvent(event.ptr) != 0) {
                when (event.type) {
                    SDL_QUIT -> {
                        isRunning = false
                        return false
                    }
                    SDL_KEYDOWN -> {
                        when (event.key.keysym.sym) {
                            SDLK_ESCAPE, SDLK_q -> {
                                isRunning = false
                                return false
                            }
                        }
                    }
                }
            }
        }
        
        return isRunning
    }
    
    /**
     * Run the window event loop
     */
    fun run(framebuffer: Framebuffer) {
        if (!init()) {
            println("Failed to initialize window")
            return
        }
        
        println("Window opened. Press ESC or Q to close, or close the window.")
        
        // Initial render
        updateFramebuffer(framebuffer)
        
        // Event loop
        while (processEvents()) {
            SDL_Delay(16) // ~60 FPS
        }
        
        println("Window closed.")
        cleanup()
    }
    
    /**
     * Clean up SDL2 resources
     */
    private fun cleanup() {
        texture?.let { SDL_DestroyTexture(it) }
        renderer?.let { SDL_DestroyRenderer(it) }
        window?.let { SDL_DestroyWindow(it) }
        SDL_Quit()
    }
}
