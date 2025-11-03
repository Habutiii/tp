package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void isValidTagName_validNames_returnsTrue() {
        assertTrue(Tag.isValidTagName("hello"));
        assertTrue(Tag.isValidTagName("abc-123"));
        assertTrue(Tag.isValidTagName("Tag-Name"));
    }

    @Test
    public void isValidTagName_invalidNames_returnsFalse() {
        assertFalse(Tag.isValidTagName("")); // empty
        assertFalse(Tag.isValidTagName("hello_world")); // underscore not allowed
        assertFalse(Tag.isValidTagName("!invalid")); // special char
        assertFalse(Tag.isValidTagName("你好")); // non-English
        assertFalse(Tag.isValidTagName("tag name")); // space
    }

    @Test
    public void toString_normalizesToUppercase() {
        assertEquals("FRIEND", new Tag("Friend").toString());
        assertEquals("FRIEND", new Tag("FRIEND").toString());
    }

    @Test
    void isValidTagName_examples() {
        // valid
        assertTrue(Tag.isValidTagName("A"));
        assertTrue(Tag.isValidTagName("friends"));
        assertTrue(Tag.isValidTagName("class-mates"));
        assertTrue(Tag.isValidTagName("a1b2c3"));

        // invalid (empty / punctuation / begins/ends with '-')
        assertFalse(Tag.isValidTagName(""));
        assertFalse(Tag.isValidTagName("-bad"));
        assertFalse(Tag.isValidTagName("bad-"));
        assertFalse(Tag.isValidTagName("bad!"));
        assertFalse(Tag.isValidTagName("white space"));
    }

    @Test
    void ctor_rejectsInvalidNames() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
        assertThrows(IllegalArgumentException.class, () -> new Tag(""));
        assertThrows(IllegalArgumentException.class, () -> new Tag("!oops"));
    }
}

