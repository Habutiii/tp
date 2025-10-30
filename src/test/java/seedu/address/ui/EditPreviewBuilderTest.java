package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditCommand;
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
        assertTrue(previews.get(4).isValid()); // Now valid if t/ is empty
    }

    @Test
    public void buildPreview_emptyTag_valid() {
        List<Person> personList = Arrays.asList(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend")))));
        String input = "edit 1 t/";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        assertEquals("Tags (t/):", previews.get(4).getLabel());
        assertEquals("friend -> ", previews.get(4).getValue());
        assertTrue(previews.get(4).isValid());
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

        List<String> invalidTags = Arrays.asList("colleague", "!!!");
        FieldPreview invalidPreview = EditPreviewBuilder.createTagsPreview(person, invalidTags);
        assertEquals("Tags (t/):", invalidPreview.getLabel());
        assertEquals("friend -> colleague, !!!", invalidPreview.getValue());
        assertFalse(invalidPreview.isValid());
    }

    @Test
    public void buildPreview_addTags_success() {
        List<Person> personList = Arrays.asList(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend")))));

        // add new tags
        String input = "edit 1 at/colleague at/family";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        // last field should be the addTags preview
        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertEquals("Tags (t/):", tagPreview.getLabel());
        assertEquals("friend + colleague, family", tagPreview.getValue());
        assertTrue(tagPreview.isValid());
    }

    @Test
    public void buildPreview_addTags_existingTagomittedFromPreview() {
        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"),
                        new HashSet<>(Arrays.asList(new Tag("friend"), new Tag("colleague")))));

        // 'colleague' already exists -> should be omitted
        String input = "edit 1 at/colleague at/family";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        FieldPreview tagPreview = previews.get(previews.size() - 1);

        assertEquals("Tags (t/):", tagPreview.getLabel());
        // 'colleague' should be skipped, only 'family' shown
        assertEquals("friend, colleague + family", tagPreview.getValue());
        assertTrue(tagPreview.isValid());
    }


    @Test
    public void buildPreview_deleteTags_success() {
        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"),
                        new HashSet<>(Arrays.asList(new Tag("friend"), new Tag("colleague")))));

        // delete existing tag
        String input = "edit 1 dt/friend";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertEquals("Tags (t/):", tagPreview.getLabel());
        assertEquals("friend, colleague - friend", tagPreview.getValue());
        assertTrue(tagPreview.isValid());
    }

    @Test
    public void buildPreview_addTags_existingTagcontinueExecuted() {
        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"),
                        new HashSet<>(Arrays.asList(new Tag("friend"), new Tag("colleague")))));

        // add existing tag 'friend' → should trigger `continue`
        String input = "edit 1 at/friend";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertEquals("Tags (t/):", tagPreview.getLabel());
        // nothing new appended, since 'friend' already exists
        assertEquals("friend, colleague + ", tagPreview.getValue());
        assertTrue(tagPreview.isValid()); // confirm branch didn’t invalidate
    }

    @Test
    public void buildPreview_addTags_invalidTagNameinvalid() {
        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"),
                        new HashSet<>(Arrays.asList(new Tag("friend")))));

        String input = "edit 1 at/!!!"; // invalid tag name
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);
        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertFalse(tagPreview.isValid());
    }



    @Test
    public void buildPreview_deleteTagsexistingTag_valid() {
        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"),
                        new HashSet<>(Arrays.asList(new Tag("friend"), new Tag("colleague")))));

        // remove existing tag 'friend' -> valid, not added to invalidTagIndices
        String input = "edit 1 dt/friend";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertEquals("Tags (t/):", tagPreview.getLabel());
        assertEquals("friend, colleague - friend", tagPreview.getValue());
        assertTrue(tagPreview.isValid()); // ensures skip branch executed
    }

    @Test
    public void buildPreview_deleteNonexistentValidTag_invalid() {
        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"),
                        new HashSet<>(Arrays.asList(new Tag("friend"))))
        );

        String input = "edit 1 dt/family"; // 'family' valid but not present
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);
        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertFalse(tagPreview.isValid());
    }


    @Test
    public void buildPreview_deleteTags_invalidTagNameinvalid() {
        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"),
                        new HashSet<>(Arrays.asList(new Tag("friend")))));

        String input = "edit 1 dt/!!!"; // invalid tag name
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);
        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertFalse(tagPreview.isValid());
    }



    @Test
    public void buildPreview_addTags_invalidTag() {
        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend")))));

        String input = "edit 1 at/!!!";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertEquals("Tags (t/):", tagPreview.getLabel());
        assertEquals("friend + !!!", tagPreview.getValue());
        assertFalse(tagPreview.isValid());
    }

    @Test
    public void createAddTagsPreview_validAndInvalid() {
        Person person = new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend"))));
        List<String> newTags = Arrays.asList("colleague");
        FieldPreview preview = EditPreviewBuilder.createAddTagsPreview(person, newTags);
        assertEquals("friend + colleague", preview.getValue());
        assertTrue(preview.isValid());
    }

    @Test
    public void createDeleteTagsPreview_missingTag_invalid() {
        Person person = new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend"))));
        List<String> deleteTags = List.of("family"); // not present
        FieldPreview preview = EditPreviewBuilder.createDeleteTagsPreview(person, deleteTags);
        assertFalse(preview.isValid());
    }

    @Test
    public void buildPreview_multipleTagPrefixes_invalidPreview() {
        List<Person> personList = Arrays.asList(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"), new HashSet<>(Arrays.asList(new Tag("friend")))));

        // Use t/ and at/ together -> invalid combination
        String input = "edit 1 t/colleague at/family";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        FieldPreview tagPreview = previews.get(previews.size() - 1);

        assertEquals("Tags (t/):", tagPreview.getLabel());
        assertEquals(EditCommand.MESSAGE_TOO_MANY_TAG_PREFIXES, tagPreview.getValue());
        assertFalse(tagPreview.isValid());
    }

    @Test
    public void buildPreview_exceedingMaxTags_invalidPreview() {
        // Prepare person with MAX_TAGS_PER_PERSON tags
        int maxTags = Person.MAX_TAGS_PER_PERSON;
        HashSet<Tag> tags = new HashSet<>();
        for (int i = 1; i <= maxTags; i++) {
            tags.add(new Tag("tag" + i));
        }

        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"), tags));

        // Try to add another tag
        String input = "edit 1 at/overflowTag";
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertEquals("Tags (t/):", tagPreview.getLabel());
        assertEquals(String.format(EditCommand.MESSAGE_EXCEEDING_MAX_TAGS, Person.MAX_TAGS_PER_PERSON,
                tags.size() + 1), tagPreview.getValue());
        assertFalse(tagPreview.isValid());
    }

    @Test
    public void buildPreview_normalTagOperation_validTagNoAddOrRemove() {
        List<Person> personList = List.of(
                new Person(new Name("Alice"), new Phone("91234567"), new Email("alice@example.com"),
                        new Address("123 Street"),
                        new HashSet<>(Arrays.asList(new Tag("friend")))));

        // Normal tag prefix (t/) with valid tag
        String input = "edit 1 t/colleague"; // not at/ or dt/, so TagOperation is NORMAL
        List<FieldPreview> previews = EditPreviewBuilder.buildPreview(input, personList);

        FieldPreview tagPreview = previews.get(previews.size() - 1);
        assertEquals("Tags (t/):", tagPreview.getLabel());
        assertEquals("friend -> colleague", tagPreview.getValue());
        assertTrue(tagPreview.isValid());
    }

}
