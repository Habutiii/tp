package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class EditPreviewBuilderTest {

    @Test
    public void createNamePreview_validAndDuplicate() {
        Person person = new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend"))));
        FieldPreview preview = EditPreviewBuilder.createNamePreview(person, "Bob", false);
        assertEquals("Name (n/):", preview.getLabel());
        assertEquals("Alice -> Bob", preview.getValue());
        assertTrue(preview.isValid());

        FieldPreview duplicatePreview = EditPreviewBuilder.createNamePreview(person, "Bob", true);
        assertEquals("Name (n/):", duplicatePreview.getLabel());
        assertEquals("Alice -> Bob (duplicate)", duplicatePreview.getValue());
        assertFalse(duplicatePreview.isValid());
    }

    @Test
    public void buildPreview_validEditCommand_success() {
        List<Person> personList = Arrays.asList(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend")))));
        String input = "edit 1 n/Bob p/98765432 e/bob@example.com a/456 Avenue t/colleague";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        assertEquals("Name (n/):", previews.get(0).getLabel());
        assertEquals("Alice -> Bob", previews.get(0).getValue());
        assertTrue(previews.get(0).isValid());

        assertEquals("Phone (p/):", previews.get(1).getLabel());
        assertEquals("91234567 -> 98765432", previews.get(1).getValue());
        assertTrue(previews.get(1).isValid());

        assertEquals("Email (e/):", previews.get(2).getLabel());
        assertEquals("alice@example.com -> bob@example.com", previews.get(2).getValue());
        assertTrue(previews.get(2).isValid());

        assertEquals("Address (a/):", previews.get(3).getLabel());
        assertEquals("123 Street -> 456 Avenue", previews.get(3).getValue());
        assertTrue(previews.get(3).isValid());

        assertEquals("Tags (t/):", previews.get(4).getLabel());
        assertEquals("friend -> colleague", previews.get(4).getValue());
        assertTrue(previews.get(4).isValid());
    }

    @Test
    public void buildPreview_invalidIndex_returnsErrorPreview() {
        List<Person> personList = Arrays.asList(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend")))));
        String input = "edit 2 n/Bob";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        assertEquals(1, previews.size());
        assertEquals("Edit Preview", previews.get(0).getLabel());
        assertEquals("Invalid index!", previews.get(0).getValue());
        assertFalse(previews.get(0).isValid());
    }

    @Test
    public void buildPreview_duplicateFields_invalidPreview() {
        List<Person> personList = Arrays.asList(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend")))));
        String input = "edit 1 n/Bob n/Bobby";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        assertEquals("Name (n/):", previews.get(0).getLabel());
        assertTrue(previews.get(0).getValue().contains("duplicate"));
        assertFalse(previews.get(0).isValid());
    }

    @Test
    public void buildPreview_tagPreview_invalidTag() {
        List<Person> personList = Arrays.asList(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend")))));
        String input = "edit 1 t/colleague t/";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        assertEquals("Tags (t/):", previews.get(4).getLabel());
        assertEquals("friend -> colleague, ", previews.get(4).getValue());
        assertFalse(previews.get(4).isValid());
        assertTrue(previews.get(4).getInvalidTagIndices().contains(1));
    }

    @Test
    public void createPhonePreview_validAndDuplicate() {
        Person person = new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend"))));
        FieldPreview preview = EditPreviewBuilder.createPhonePreview(person, "98765432", false);
        assertEquals("Phone (p/):", preview.getLabel());
        assertEquals("91234567 -> 98765432", preview.getValue());
        assertTrue(preview.isValid());

        FieldPreview duplicatePreview = EditPreviewBuilder.createPhonePreview(person, "98765432", true);
        assertEquals("Phone (p/):", duplicatePreview.getLabel());
        assertEquals("91234567 -> 98765432 (duplicate)", duplicatePreview.getValue());
        assertFalse(duplicatePreview.isValid());
    }

    @Test
    public void createEmailPreview_validAndDuplicate() {
        Person person = new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend"))));
        FieldPreview preview = EditPreviewBuilder.createEmailPreview(person, "bob@example.com", false);
        assertEquals("Email (e/):", preview.getLabel());
        assertEquals("alice@example.com -> bob@example.com", preview.getValue());
        assertTrue(preview.isValid());

        FieldPreview duplicatePreview = EditPreviewBuilder.createEmailPreview(person, "bob@example.com", true);
        assertEquals("Email (e/):", duplicatePreview.getLabel());
        assertEquals("alice@example.com -> bob@example.com (duplicate)", duplicatePreview.getValue());
        assertFalse(duplicatePreview.isValid());
    }

    @Test
    public void createAddressPreview_validAndDuplicate() {
        Person person = new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend"))));
        FieldPreview preview = EditPreviewBuilder.createAddressPreview(person, "456 Avenue", false);
        assertEquals("Address (a/):", preview.getLabel());
        assertEquals("123 Street -> 456 Avenue", preview.getValue());
        assertTrue(preview.isValid());

        FieldPreview duplicatePreview = EditPreviewBuilder.createAddressPreview(person, "456 Avenue", true);
        assertEquals("Address (a/):", duplicatePreview.getLabel());
        assertEquals("123 Street -> 456 Avenue (duplicate)", duplicatePreview.getValue());
        assertFalse(duplicatePreview.isValid());
    }

    @Test
    public void createTagsPreview_validAndInvalidTags() {
        Person person = new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend"))));
        List<String> newTags = Arrays.asList("colleague", "family");
        FieldPreview preview = EditPreviewBuilder.createTagsPreview(person, newTags);
        assertEquals("Tags (t/):", preview.getLabel());
        assertEquals("friend -> colleague, family", preview.getValue());
        assertTrue(preview.isValid());

        List<String> invalidTags = Arrays.asList("colleague", "");
        FieldPreview invalidPreview = EditPreviewBuilder.createTagsPreview(person, invalidTags);
        assertEquals("Tags (t/):", invalidPreview.getLabel());
        assertEquals("friend -> colleague, ", invalidPreview.getValue());
        assertFalse(invalidPreview.isValid());
    }
}
