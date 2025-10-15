package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // ---------- invalid ----------
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // less than 8 digits
        assertFalse(Phone.isValidPhone("1234567")); // 7 digits
        assertFalse(Phone.isValidPhone("123456789")); // 9 digits
        assertFalse(Phone.isValidPhone("9312 1534")); // spaces inside
        assertFalse(Phone.isValidPhone("phone")); // letters
        assertFalse(Phone.isValidPhone("9011p041")); // alphanumeric
        assertFalse(Phone.isValidPhone("+6593121534")); // plus sign, country code
        assertFalse(Phone.isValidPhone("一二三四五六七八")); // non-ASCII

        // ---------- valid ----------
        assertTrue(Phone.isValidPhone("93121534")); // valid SG number
        assertTrue(Phone.isValidPhone("81234567")); // another valid SG number
        assertTrue(Phone.isValidPhone("69998888")); // valid SG number
    }

    @Test
    public void equals() {
        Phone phone = new Phone("99999999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("99999999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("88888888")));
    }
}
