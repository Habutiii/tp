package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

/**
 * Tests for EditPreviewBuilder field preview logic.
 * Helper methods are now named createNamePreview, createPhonePreview, etc.
 */
public class EditPreviewBuilderTest {

    @Test
    public void editPreview_typicalFields_success() {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                new Address("123 Street"), new HashSet<>(Arrays.asList(new seedu.address.model.tag.Tag("friend")))));

        String input = "edit 1 n/Bob p/98765432";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        assertEquals("Name (n/):", previews.get(0).getLabel());
        assertEquals("Alice -> Bob", previews.get(0).getValue());
        assertTrue(previews.get(0).isValid());

        assertEquals("Phone (p/):", previews.get(1).getLabel());
        assertEquals("91234567 -> 98765432", previews.get(1).getValue());
        assertTrue(previews.get(1).isValid());
    }

    @Test
    public void editPreview_invalidIndex_errorPreview() {
        List<Person> personList = new ArrayList<>();
        String input = "edit 2 n/Bob";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        assertEquals(1, previews.size());
        assertEquals("Edit Preview", previews.get(0).getLabel());
        assertEquals("Invalid index!", previews.get(0).getValue());
        assertFalse(previews.get(0).isValid());
    }
}
