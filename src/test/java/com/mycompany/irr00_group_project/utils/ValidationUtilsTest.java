package com.mycompany.irr00_group_project.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ValidationUtils}
 *
 * @author Alexandra Varapai
 */
public class ValidationUtilsTest {

    @Test
    @DisplayName("isNotBlank() returns true for normal string")
    void isNotBlankNormal() {
        assertTrue(ValidationUtils.isNotBlank("Skirt"));
    }

    @Test
    @DisplayName("isNotBlank() returns false for null")
    void isNotBlankNull() {
        assertFalse(ValidationUtils.isNotBlank(null));
    }

    @Test
    @DisplayName("isNotBlank() returns false for empty string")
    void isNotBlankEmpty() {
        assertFalse(ValidationUtils.isNotBlank(""));
    }

    @Test
    @DisplayName("isNotBlank() returns false for whitespace-only string")
    void isNotBlankWhitespace() {
        assertFalse(ValidationUtils.isNotBlank("   "));
    }

    @Test
    @DisplayName("isNotBlank() returns true for string with surrounding spaces")
    void isNotBlankWithSpaces() {
        assertTrue(ValidationUtils.isNotBlank("  Skirt  "));
    }
}
