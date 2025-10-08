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
}
