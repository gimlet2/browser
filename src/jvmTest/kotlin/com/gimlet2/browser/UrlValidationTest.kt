package com.gimlet2.browser

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

/**
 * Tests for URL validation and formatting logic.
 */
class UrlValidationTest {
    
    @Test
    fun `test URL with https prefix is unchanged`() {
        val url = "https://example.com"
        val processed = processUrl(url)
        assertEquals("https://example.com", processed)
    }
    
    @Test
    fun `test URL with http prefix is unchanged`() {
        val url = "http://example.com"
        val processed = processUrl(url)
        assertEquals("http://example.com", processed)
    }
    
    @Test
    fun `test URL without protocol gets https prefix`() {
        val url = "example.com"
        val processed = processUrl(url)
        assertEquals("https://example.com", processed)
    }
    
    @Test
    fun `test URL with spaces is trimmed`() {
        val url = "  https://example.com  "
        val processed = processUrl(url)
        assertEquals("https://example.com", processed)
    }
    
    @Test
    fun `test domain without protocol gets https`() {
        val url = "www.google.com"
        val processed = processUrl(url)
        assertEquals("https://www.google.com", processed)
    }
    
    /**
     * Simulates the URL processing logic from BrowserApplication.loadUrl
     */
    private fun processUrl(url: String): String {
        var finalUrl = url.trim()
        if (!finalUrl.startsWith("http://") && !finalUrl.startsWith("https://")) {
            finalUrl = "https://$finalUrl"
        }
        return finalUrl
    }
}
