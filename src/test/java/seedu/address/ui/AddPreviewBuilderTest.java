package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AddPreviewBuilderTest {

    @Test
    public void addPreview_typicalFields_success() {
        String input = "add n/Alice p/91234567 e/alice@example.com a/123 Street t/friend";
        List<FieldPreview> previews = AddPreviewBuilder.buildPreview(input);

        assertEquals(5, previews.size());
        assertEquals("Name (n/):", previews.get(0).getLabel());
        assertEquals("Alice", previews.get(0).getValue());
        assertTrue(previews.get(0).isValid());

        assertEquals("Phone (p/):", previews.get(1).getLabel());
        assertEquals("91234567", previews.get(1).getValue());
        assertTrue(previews.get(1).isValid());

        assertEquals("Email (e/):", previews.get(2).getLabel());
        assertEquals("alice@example.com", previews.get(2).getValue());
        assertTrue(previews.get(2).isValid());

        assertEquals("Address (a/):", previews.get(3).getLabel());
        assertEquals("123 Street", previews.get(3).getValue());
        assertTrue(previews.get(3).isValid());

        assertEquals("Tags (t/):", previews.get(4).getLabel());
        assertEquals("friend", previews.get(4).getValue());
    }

    @Test
    public void addPreview_duplicateName_invalid() {
        String input = "add n/Alice n/Bob";
        List<FieldPreview> previews = AddPreviewBuilder.buildPreview(input);

        assertEquals("Name (n/):", previews.get(0).getLabel());
        assertTrue(previews.get(0).getValue().contains("duplicate"));
        assertFalse(previews.get(0).isValid());
    }

    @Test
    public void addPreview_duplicatePhone_invalid() {
        String input = "add p/91234567 p/98765432";
        List<FieldPreview> previews = AddPreviewBuilder.buildPreview(input);

        assertEquals("Phone (p/):", previews.get(1).getLabel());
        assertTrue(previews.get(1).getValue().contains("duplicate"));
        assertFalse(previews.get(1).isValid());
    }

    @Test
    public void addPreview_duplicateEmail_invalid() {
        String input = "add e/alice@example.com e/bob@example.com";
        List<FieldPreview> previews = AddPreviewBuilder.buildPreview(input);

        assertEquals("Email (e/):", previews.get(2).getLabel());
        assertTrue(previews.get(2).getValue().contains("duplicate"));
        assertFalse(previews.get(2).isValid());
    }

    @Test
    public void addPreview_duplicateAddress_invalid() {
        String input = "add a/123 Street a/456 Avenue";
        List<FieldPreview> previews = AddPreviewBuilder.buildPreview(input);

        assertEquals("Address (a/):", previews.get(3).getLabel());
        assertTrue(previews.get(3).getValue().contains("duplicate"));
        assertFalse(previews.get(3).isValid());
    }

    @Test
    public void addPreview_duplicateTags_invalid() {
        String input = "add t/friend t/friend";
        List<FieldPreview> previews = AddPreviewBuilder.buildPreview(input);

        assertEquals("Tags (t/):", previews.get(4).getLabel());
        assertEquals("friend", previews.get(4).getValue()); // Only one friend shown after new update
        assertTrue(previews.get(4).getInvalidTagIndices().isEmpty() || previews.get(4).isValid());
    }
}
