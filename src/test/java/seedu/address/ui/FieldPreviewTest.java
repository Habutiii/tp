package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FieldPreviewTest {

    @Test
    public void test_fieldPreview_constructor() {
        FieldPreview fp = new FieldPreview("Name (n/):", "Alice", true);
        assertEquals("Name (n/):", fp.getLabel());
        assertEquals("Alice", fp.getValue());
        assertTrue(fp.isValid());
    }

    @Test
    public void test_invalid_field() {
        FieldPreview fp = new FieldPreview("Phone (p/):", "123", false);
        assertFalse(fp.isValid());
    }

    @Test
    public void test_valid_name() {
        FieldPreview fp = new FieldPreview("Name (n/):", "Bob", true);
        assertTrue(fp.isValid());
        assertEquals("Name (n/):", fp.getLabel());
        assertEquals("Bob", fp.getValue());
    }

    @Test
    public void test_valid_phone() {
        FieldPreview fp = new FieldPreview("Phone (p/):", "12345678", true);
        assertTrue(fp.isValid());
        assertEquals("Phone (p/):", fp.getLabel());
        assertEquals("12345678", fp.getValue());
    }

    @Test
    public void test_valid_email() {
        FieldPreview fp = new FieldPreview("Email (e/):", "abc@hotmail.com", true);
        assertTrue(fp.isValid());
        assertEquals("Email (e/):", fp.getLabel());
        assertEquals("abc@hotmail.com", fp.getValue());
    }
}
