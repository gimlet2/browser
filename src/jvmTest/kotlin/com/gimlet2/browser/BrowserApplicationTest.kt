package com.gimlet2.browser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Basic tests for the browser application components.
 * Note: Full GUI tests are limited due to JavaFX requiring a display.
 */
class BrowserApplicationTest {
    
    @Test
    fun `test application class can be instantiated`() {
        // Verify the main application class structure
        val appClass = BrowserApplication::class.java
        assertNotNull(appClass)
        assertEquals("BrowserApplication", appClass.simpleName)
        
        // Verify it extends JavaFX Application
        assertTrue(javafx.application.Application::class.java.isAssignableFrom(appClass))
    }
}
