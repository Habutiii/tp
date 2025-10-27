package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.DuplicateFieldChecker;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Utility class for handling edit preview live preview.
 */
public class EditPreviewBuilder {
    /**
     * Parses the given edit command input and generates a list of FieldPreview
     * objects
     * representing the live preview for each field (index, name, phone, email,
     * address, tags).
     *
     * @param input      The raw user input string for the add command.
     * @param personList The list of person to retrieve person at index i.
     * @return A list of FieldPreview objects for each field, with validation and
     *         duplicate status.
     *         Returns an empty list if input is empty or invalid.
     */
    public static List<FieldPreview> buildPreview(String input, List<Person> personList) {
        List<FieldPreview> fieldPreviews = new ArrayList<>();
        String args = input.substring(4).trim();
        String[] allTokens = args.split("\\s+");
        if (allTokens.length < 1) {
            return new ArrayList<>();
        }
        String indexStr = allTokens[0];
        int index = -1;
        try {
            index = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            fieldPreviews.add(createInvalidIndexPreview());
            return fieldPreviews;
        }
        if (index < 0 || index >= personList.size()) {
            fieldPreviews.add(createInvalidIndexPreview());
            return fieldPreviews;
        }
        Person person = personList.get(index);

        String flagArgs = args.substring(indexStr.length()).trim();
        if (!flagArgs.isEmpty() && !flagArgs.startsWith(" ")) {
            flagArgs = " " + flagArgs;
        }
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                flagArgs,
                CliSyntax.PREFIX_NAME,
                CliSyntax.PREFIX_PHONE,
                CliSyntax.PREFIX_EMAIL,
                CliSyntax.PREFIX_ADDRESS,
                CliSyntax.PREFIX_TAG,
                CliSyntax.PREFIX_ADDTAG,
                CliSyntax.PREFIX_DELETETAG);

        fieldPreviews.add(createFieldPreview(
                "Name (n/):",
                person.getName().fullName,
                argMultimap.getValue(CliSyntax.PREFIX_NAME).orElse(null),
                DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_NAME),
                Name::isValidName));

        fieldPreviews.add(createFieldPreview(
                "Phone (p/):",
                person.getPhone().value,
                argMultimap.getValue(CliSyntax.PREFIX_PHONE).orElse(null),
                DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_PHONE),
                Phone::isValidPhone));

        fieldPreviews.add(createFieldPreview(
                "Email (e/):",
                person.getEmail().value,
                argMultimap.getValue(CliSyntax.PREFIX_EMAIL).orElse(null),
                DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_EMAIL),
                Email::isValidEmail));

        fieldPreviews.add(createFieldPreview(
                "Address (a/):",
                person.getAddress().value,
                argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).orElse(null),
                DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_ADDRESS),
                Address::isValidAddress));

        List<String> newTagsList = argMultimap.getAllValues(CliSyntax.PREFIX_TAG);
        List<String> addTagsList = argMultimap.getAllValues(CliSyntax.PREFIX_ADDTAG);
        List<String> deleteTagsList = argMultimap.getAllValues(CliSyntax.PREFIX_DELETETAG);

        String tags = String.join(", ", person.getTags().stream().map(tag -> tag.tagName).toArray(String[]::new));

        if (!newTagsList.isEmpty()) {
            fieldPreviews.add(createTagsPreview(person, newTagsList));
        } else if (!addTagsList.isEmpty()) {
            fieldPreviews.add(createAddTagsPreview(person, addTagsList));
        } else if (!deleteTagsList.isEmpty()) {
            fieldPreviews.add(createDeleteTagsPreview(person, deleteTagsList));
        } else {
            fieldPreviews.add(new FieldPreview("Tags (t/):", tags, true));
        }

        return fieldPreviews;
    }

    static FieldPreview createNamePreview(Person person, String newName, boolean duplicate) {
        String oldName = person.getName().fullName;
        if (duplicate) {
            return new FieldPreview("Name (n/):", oldName + " -> " + newName + " (duplicate)", false);
        }
        boolean isValid = Name.isValidName(newName);
        return new FieldPreview("Name (n/):", oldName + " -> " + newName, isValid);
    }

    static FieldPreview createPhonePreview(Person person, String newPhone, boolean duplicate) {
        String oldPhone = person.getPhone().value;
        if (duplicate) {
            return new FieldPreview("Phone (p/):", oldPhone + " -> " + newPhone + " (duplicate)", false);
        }
        boolean isValid = Phone.isValidPhone(newPhone);
        return new FieldPreview("Phone (p/):", oldPhone + " -> " + newPhone, isValid);
    }

    static FieldPreview createEmailPreview(Person person, String newEmail, boolean duplicate) {
        String oldEmail = person.getEmail().value;
        if (duplicate) {
            return new FieldPreview("Email (e/):", oldEmail + " -> " + newEmail + " (duplicate)", false);
        }
        boolean isValid = Email.isValidEmail(newEmail);
        return new FieldPreview("Email (e/):", oldEmail + " -> " + newEmail, isValid);
    }

    static FieldPreview createAddressPreview(Person person, String newAddress, boolean duplicate) {
        String oldAddress = person.getAddress().value;
        if (duplicate) {
            return new FieldPreview("Address (a/):", oldAddress + " -> " + newAddress + " (duplicate)", false);
        }
        boolean isValid = Address.isValidAddress(newAddress);
        return new FieldPreview("Address (a/):", oldAddress + " -> " + newAddress, isValid);
    }

    static FieldPreview createTagsPreview(Person person, List<String> newTagsList) {
        return createGenericTagsPreview(person, newTagsList, TagOperation.REPLACE);
    }

    static FieldPreview createAddTagsPreview(Person person, List<String> newTagsList) {
        return createGenericTagsPreview(person, newTagsList, TagOperation.ADD);
    }

    static FieldPreview createDeleteTagsPreview(Person person, List<String> newTagsList) {
        return createGenericTagsPreview(person, newTagsList, TagOperation.REMOVE);
    }

    private static FieldPreview createGenericTagsPreview(Person person, List<String> newTagsList, TagOperation op) {
        String oldTags = String.join(", ",
                person.getTags().stream().map(tag -> tag.tagName).toArray(String[]::new));

        StringBuilder tagsJoined = new StringBuilder();
        List<Integer> invalidTagIndices = new ArrayList<>();
        for (int i = 0; i < newTagsList.size(); i++) {
            String tag = newTagsList.get(i);
            if (!tag.isEmpty() && !Tag.isValidTagName(tag)) {
                invalidTagIndices.add(i);
            }
            if (op == TagOperation.REMOVE && !person.getTags().contains(new Tag(tag))) {
                invalidTagIndices.add(i);
            }
            tagsJoined.append(tag);
            if (i < newTagsList.size() - 1) {
                tagsJoined.append(", ");
            }
        }

        String newTags = tagsJoined.toString();
        String previewText;
        switch (op) {
        case REPLACE -> previewText = oldTags + " -> " + newTags;
        case ADD -> previewText = oldTags + " + " + newTags;
        case REMOVE -> previewText = oldTags + " - " + newTags;
        default -> previewText = oldTags + " -> " + newTags;
        }

        return new FieldPreview("Tags (t/):", previewText, invalidTagIndices);
    }

    private enum TagOperation {
        REPLACE, ADD, REMOVE
    }


    static FieldPreview createFieldPreview(
            String label,
            String oldValue,
            String newValue,
            boolean isDuplicate,
            java.util.function.Predicate<String> validator) {
        if (newValue != null) {
            boolean isValid = !newValue.isEmpty() && validator.test(newValue) && !isDuplicate;
            String preview = oldValue + " -> " + newValue + (isDuplicate ? " (duplicate)" : "");
            return new FieldPreview(label, preview, isValid);
        } else {
            boolean isValid = !oldValue.isEmpty() && validator.test(oldValue);
            return new FieldPreview(label, oldValue, isValid);
        }
    }

    private static FieldPreview createInvalidIndexPreview() {
        return new FieldPreview("Edit Preview", "Invalid index!", false);
    }
}
