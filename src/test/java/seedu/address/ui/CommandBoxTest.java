package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CommandBoxTest {
    @Test
    public void public_createNamePreview_validName() {
        FieldPreview fp = CommandBox.createNamePreview("Alice", false);
        assertEquals("Alice", fp.getValue());
        assertTrue(fp.isValid());
    }

    @Test
    public void public_createNamePreview_invalidName() {
        FieldPreview fp = CommandBox.createNamePreview("!@#", false);
        assertFalse(fp.isValid());
    }

    @Test
    public void public_createNamePreview_duplicateName() {
        FieldPreview fp = CommandBox.createNamePreview("Alice", true);
        assertFalse(fp.isValid());
        assertTrue(fp.getValue().contains("duplicate"));
    }

    @Test
    public void public_createPhonePreview_validPhone() {
        FieldPreview fp = CommandBox.createPhonePreview("12345678", false);
        assertEquals("12345678", fp.getValue());
        assertTrue(fp.isValid());
    }

    @Test
    public void public_createPhonePreview_invalidPhone() {
        FieldPreview fp = CommandBox.createPhonePreview("abc", false);
        assertFalse(fp.isValid());
    }

    @Test
    public void public_createPhonePreview_duplicatePhone() {
        FieldPreview fp = CommandBox.createPhonePreview("12345678", true);
        assertFalse(fp.isValid());
        assertTrue(fp.getValue().contains("duplicate"));
    }

    @Test
    public void public_createEmailPreview_validEmail() {
        FieldPreview fp = CommandBox.createEmailPreview("test@email.com", false);
        assertEquals("test@email.com", fp.getValue());
        assertTrue(fp.isValid());
    }

    @Test
    public void public_createEmailPreview_invalidEmail() {
        FieldPreview fp = CommandBox.createEmailPreview("invalid@", false);
        assertFalse(fp.isValid());
    }

    @Test
    public void public_createEmailPreview_duplicateEmail() {
        FieldPreview fp = CommandBox.createEmailPreview("test@email.com", true);
        assertFalse(fp.isValid());
        assertTrue(fp.getValue().contains("duplicate"));
    }

    @Test
    public void public_createAddressPreview_validAddress() {
        FieldPreview fp = CommandBox.createAddressPreview("123 Main St", false);
        assertEquals("123 Main St", fp.getValue());
        assertTrue(fp.isValid());
    }

    @Test
    public void public_createAddressPreview_invalidAddress() {
        FieldPreview fp = CommandBox.createAddressPreview("", false);
        assertTrue(fp.isValid());
    }

    @Test
    public void public_createAddressPreview_duplicateAddress() {
        FieldPreview fp = CommandBox.createAddressPreview("123 Main St", true);
        assertFalse(fp.isValid());
        assertTrue(fp.getValue().contains("duplicate"));
    }
}
