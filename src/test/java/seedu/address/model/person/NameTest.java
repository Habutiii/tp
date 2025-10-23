package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {


    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }


    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }


    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));


        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Name.isValidName("peter*")); // contains non-alphanumeric characters
        assertFalse(Name.isValidName("你好")); // trailing space
        assertFalse(Name.isValidName(" John")); // VALIDATION_REGEX false, PRINTABLE_ASCII_REGEX true
        assertFalse(Name.isValidName("John你好")); // VALIDATION_REGEX true, PRINTABLE_ASCII_REGEX false
        assertFalse(Name.isValidName(" 你好")); // both false
        assertFalse(Name.isValidName("12345")); // numbers only
        assertFalse(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertFalse(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
        assertFalse(Name.isValidName("qqqqqqqqqqqqqqqqqqqwwwwwwwwwwweeeeeeeeeeeeeeeeeerrrr"
                + "iiiiiiiiiiiiiiiiiiiiiisdccccccccccccccccccccccccjjjjjjjjjjjjjjjwfeeeeeeewwf"
                + "wkcmmmmmmmmmmmmmmmmmmmfionvalllllllllllllllllllllllllllllllllioqweeeeeeeiiiii"
                + "wfoecjjjjjjjjjjjjjjjjjqeopfffffffffffffffffffffffffvncwjeeeeeeeeeeeeeeeeee"));


        // valid name
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("O'Connor")); // apostrophe
        assertTrue(Name.isValidName("Jean-Luc")); // hyphen
        assertTrue(Name.isValidName("Tan, Mei Ling")); // comma
        assertTrue(Name.isValidName("Mary (Ann)")); // parentheses
        assertTrue(Name.isValidName("John / Mary")); // slash
        assertTrue(Name.isValidName("Dr. Ray")); // period
        assertTrue(Name.isValidName("Alex @ Home")); //@
        assertTrue(Name.isValidName("A.B (C-D), E/F @ G-H' I")); // stress test with allowed chars
    }


    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
    @Test
    public void exceedsMaxLength_returnsFalse() {
        String overMax = "A".repeat(Name.MAX_LENGTH + 1); // 101 chars
        assertFalse(Name.isValidName(overMax));
        assertThrows(IllegalArgumentException.class, () -> new Name(overMax));
    }

    @Test
    public void atMaxLength_returnsTrue() {
        String atMax = "A".repeat(Name.MAX_LENGTH); // 100 characters
        assertTrue(Name.isValidName(atMax));
        Name n = new Name(atMax); // should not throw
        assertTrue(n.toString().equals(atMax));
    }

    @Test
    public void withAllowedPunctuation_returnsTrue() {
        // Build a realistic name using only allowed characters, then pad to exactly MAX
        String base = "Dr. Jean-Luc O'Connor (Team Lead) @ Project Alpha";
        int pad = Name.MAX_LENGTH - base.length();
        String atMax = base + (pad > 0 ? "A".repeat(pad) : "");
        assertTrue(atMax.length() == Name.MAX_LENGTH);
        assertTrue(Name.isValidName(atMax));
        new Name(atMax); // should not throw
    }

    @Test
    public void toString_returnsOriginalValue() {
        Name n = new Name("Alice Smith");
        org.junit.jupiter.api.Assertions.assertEquals("Alice Smith", n.toString());
    }

    @Test
    public void hashCode_consistencyAndEquality() {
        Name a = new Name("Charlie Day");
        Name b = new Name("Charlie Day");
        Name c = new Name("Charlie May");

        // equal objects -> equal hash
        org.junit.jupiter.api.Assertions.assertEquals(a.hashCode(), b.hashCode());

        // different value -> very likely different hash
        org.junit.jupiter.api.Assertions.assertNotEquals(a.hashCode(), c.hashCode());
    }

    @Test
    public void equals_caseSensitivity_check() {
        Name lower = new Name("david lee");
        Name upper = new Name("David Lee"); // different by case, equals() is case-sensitive
        assertFalse(lower.equals(upper));
    }
}

