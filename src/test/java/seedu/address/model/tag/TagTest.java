package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
        // Valid: matches both VALIDATION_REGEX and PRINTABLE_ASCII_REGEX
        assertTrue(Tag.isValidTagName("friends"));
        // Invalid format: matches ASCII but not VALIDATION_REGEX (e.g., starts with space)
        assertFalse(Tag.isValidTagName(" friends"));
        // Invalid ASCII: matches VALIDATION_REGEX but contains non-ASCII (e.g., Chinese)
        assertFalse(Tag.isValidTagName("你好"));
    }
}
