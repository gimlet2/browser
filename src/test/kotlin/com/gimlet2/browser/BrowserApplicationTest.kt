package com.gimlet2.browser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Basic tests for the browser application components.
 * GUI tests are limited due to JavaFX headless constraints.
 */
class BrowserApplicationTest {
    
    @Test
    fun `test application class exists`() {
        // Verify the main application class can be loaded
        val appClass = BrowserApplication::class.java
        assertNotNull(appClass)
        assertEquals("BrowserApplication", appClass.simpleName)
    }
    
    @Test
    fun `test main function exists`() {
        // Verify the main function exists and can be referenced
        val mainFunction = ::main
        assertNotNull(mainFunction)
    }
}
