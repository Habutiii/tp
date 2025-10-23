package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }


    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }


    @Test
    public void isValidAddress() {
        // null address
        assertThrows(NullPointerException.class, () -> Address.isValidAddress(null));


        // invalid addresses
        assertFalse(Address.isValidAddress("")); // empty string
        assertFalse(Address.isValidAddress(" ")); // spaces only
        assertFalse(Address.isValidAddress("你好")); //  contains non-ASCII
        assertFalse(Address.isValidAddress(" Blk 456, Den Road, #01-355")); // Starts with space
        assertFalse(Address.isValidAddress("Blk 123 Clementi Ave 3 "));


        // valid addresses
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidAddress("-")); // one character
        assertTrue(Address.isValidAddress("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address

    }

    @Test
    public void equals() {
        Address address = new Address("Valid Address");


        // same values -> returns true
        assertTrue(address.equals(new Address("Valid Address")));


        // same object -> returns true
        assertTrue(address.equals(address));


        // null -> returns false
        assertFalse(address.equals(null));


        // different types -> returns false
        assertFalse(address.equals(5.0f));


        // different values -> returns false
        assertFalse(address.equals(new Address("Other Valid Address")));
    }


    @Test
    public void isValidAddress_nonPrintableAsciiControlChar_false() {
        // Contains a TAB -> not printable ASCII, should fail first predicate
        assertFalse(Address.isValidAddress("Unit\t12"));
    }


    @Test
    public void isValidAddress_nonAsciiButNoLeadingSpace_false() {
        // Non-ASCII, but doesn't start with whitespace -> VALIDATION_REGEX would be true,
        // first predicate false (PRINTABLE_ASCII), ensures short-circuit path is covered
        assertFalse(Address.isValidAddress("你好街道12号"));
    }


    @Test
    public void toString_returnsOriginalValue() {
        Address a = new Address("123 Main St #05-05");
        assertTrue(a.toString().equals("123 Main St #05-05"));
    }


    @Test
    public void hashCode_consistency() {
        Address a1 = new Address("123 Main St #05-05");
        Address a2 = new Address("123 Main St #05-05");
        // equals implies same hashCodes
        assertTrue(a1.equals(a2));
        org.junit.jupiter.api.Assertions.assertEquals(a1.hashCode(), a2.hashCode());
    }


    @Test
    public void exceedsMaxLength_returnsFalse() {
        String overMax = "A".repeat(Address.MAX_LENGTH + 1);
        assertFalse(Address.isValidAddress(overMax));
        assertThrows(IllegalArgumentException.class, () -> new Address(overMax));
    }


    @Test
    public void atMaxLength_returnsTrue() {
        String atMax = "A".repeat(Address.MAX_LENGTH);
        assertTrue(Address.isValidAddress(atMax));
        Address addr = new Address(atMax);
        assertTrue(addr.toString().equals(atMax));
    }


    @Test
    public void complexContent_paddedToMax_returnsTrue() {
        String base = "123 Main St, Unit #05-05; Singapore 123456";
        int pad = Address.MAX_LENGTH - base.length();
        String atMaxComplex = base + "A".repeat(Math.max(0, pad));
        assertTrue(Address.isValidAddress(atMaxComplex));
        Address addr = new Address(atMaxComplex);
        assertTrue(addr.toString().equals(atMaxComplex));
    }
}
