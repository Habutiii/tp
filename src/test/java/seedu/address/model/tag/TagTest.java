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
    public void equals_caseInsensitiveTags_returnsTrue() {
        assertEquals(new Tag("Friend"), new Tag("friend"));
    }

    @Test
    public void toString_preservesOriginalCase() {
        assertEquals("Friend", new Tag("Friend").toString());
    }

    @Test
    public void exceedsMaxLength_returnsFalse() {
        // 41 characters (should fail)
        String longTag = "A".repeat(41);
        assertFalse(Tag.isValidTagName(longTag));
        assertThrows(IllegalArgumentException.class, () -> new Tag(longTag));
    }

    @Test
    public void atMaxLength_returnsTrue() {
        // 40 characters (should pass)
        String validLongTag = "A".repeat(40);
        assertTrue(Tag.isValidTagName(validLongTag));
        Tag tag = new Tag(validLongTag);
        assertEquals("[" + validLongTag + "]", tag.toString());
    }

    @Test
    public void equals_allBranchesCovered() {
        Tag tagA = new Tag("Important");
        Tag tagB = new Tag("Important");
        Tag tagC = new Tag("important"); // same letters, different cases
        Tag tagD = new Tag("Optional"); // different content

        // 1. same object -> true
        assertTrue(tagA.equals(tagA));

        // 2. same name, same case -> true
        assertTrue(tagA.equals(tagB));

        // 3. same name, different case -> true (equalsIgnoreCase)
        assertTrue(tagA.equals(tagC));

        // 4. null -> false
        assertFalse(tagA.equals(null));

        // 5. different type -> false
        assertFalse(tagA.equals("Important"));

        // 6. different tag name -> false
        assertFalse(tagA.equals(tagD));
    }

    @Test
    public void hashCode_consistencyAndCaseInsensitiveEquality() {
        Tag upper = new Tag("Work");
        Tag lower = new Tag("work");
        Tag other = new Tag("Home");

        // equal ignoring case -> equal hashCode
        org.junit.jupiter.api.Assertions.assertEquals(upper.hashCode(), lower.hashCode());

        // different tagName -> different hashCode
        org.junit.jupiter.api.Assertions.assertNotEquals(upper.hashCode(), other.hashCode());

        // consistency
        int initial = upper.hashCode();
        org.junit.jupiter.api.Assertions.assertEquals(initial, upper.hashCode());
    }
}

