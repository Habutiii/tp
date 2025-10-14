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
    public void addPreview_duplicateFields_invalid() {
        String input = "add n/Alice n/Bob";
        List<FieldPreview> previews = AddPreviewBuilder.buildPreview(input);

        assertEquals("Name (n/):", previews.get(0).getLabel());
        assertTrue(previews.get(0).getValue().contains("duplicate"));
        assertFalse(previews.get(0).isValid());
    }
}
