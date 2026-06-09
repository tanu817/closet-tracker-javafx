package com.mycompany.irr00_group_project.utils;

/**
 * Utility class for input validation helpers.
 *
 * @author Group 21
 */
public final class ValidationUtils {

    private ValidationUtils() {
        // utility class — not instantiated
    }

    /**
     * Returns {@code true} when the value is non-null and not blank.
     *
     * @param value the string to check
     * @return {@code true} if non-null and non-blank
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
