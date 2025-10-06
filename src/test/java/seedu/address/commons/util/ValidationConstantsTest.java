package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValidationConstantsTest {
    @Test
    public void printableAsciiRegex_isAccessibleAndWorks() {
        // Should match a valid ASCII string
        assertTrue("Hello123!@#~ ".matches(ValidationConstants.PRINTABLE_ASCII_REGEX));
        // Should not match a string with non-ASCII
        assertFalse("你好".matches(ValidationConstants.PRINTABLE_ASCII_REGEX));
    }
}

