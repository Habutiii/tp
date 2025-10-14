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
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Utility class for handling add preview live preview.
 */
public class AddPreviewBuilder {

    /**
     * Parses the given add command input and generates a list of FieldPreview
     * objects
     * representing the live preview for each field (name, phone, email, address,
     * tags).
     *
     * @param input The raw user input string for the add command.
     * @return A list of FieldPreview objects for each field, with validation and
     *         duplicate status.
     *         Returns an empty list if input is empty or invalid.
     */
    public static List<FieldPreview> buildPreview(String input) {
        List<FieldPreview> fieldPreviews = new ArrayList<>();
        String args = input.substring(3).trim();
        if (!args.isEmpty() && !args.startsWith(" ")) {
            args = " " + args;
        }
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                CliSyntax.PREFIX_NAME,
                CliSyntax.PREFIX_PHONE,
                CliSyntax.PREFIX_EMAIL,
                CliSyntax.PREFIX_ADDRESS,
                CliSyntax.PREFIX_TAG);

        String name = argMultimap.getValue(CliSyntax.PREFIX_NAME).orElse("");
        boolean isDuplicateName = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_NAME);

        String phone = argMultimap.getValue(CliSyntax.PREFIX_PHONE).orElse("");
        boolean isDuplicatePhone = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_PHONE);

        String email = argMultimap.getValue(CliSyntax.PREFIX_EMAIL).orElse("");
        boolean isDuplicateEmail = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_EMAIL);

        String address = argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).orElse("");
        boolean isDuplicateAddress = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_ADDRESS);

        List<String> tagList = argMultimap.getAllValues(CliSyntax.PREFIX_TAG);

        fieldPreviews.add(createNamePreview(name, isDuplicateName));
        fieldPreviews.add(createPhonePreview(phone, isDuplicatePhone));
        fieldPreviews.add(createEmailPreview(email, isDuplicateEmail));
        fieldPreviews.add(createAddressPreview(address, isDuplicateAddress));
        fieldPreviews.add(createTagsPreview(tagList));
        return fieldPreviews;
    }

    private static FieldPreview createTagsPreview(List<String> tagList) {
        StringBuilder tagsJoined = new StringBuilder();
        List<Integer> invalidTagIndices = new ArrayList<>();
        for (int i = 0; i < tagList.size(); i++) {
            String tag = tagList.get(i);
            if (tag.isEmpty() || !Tag.isValidTagName(tag)) {
                invalidTagIndices.add(i);
            }
            tagsJoined.append(tag);
            if (i < tagList.size() - 1) {
                tagsJoined.append(", ");
            }
        }
        return new FieldPreview("Tags (t/):", tagsJoined.toString(), invalidTagIndices);
    }

    private static FieldPreview createNamePreview(String name, boolean duplicate) {
        if (duplicate) {
            return new FieldPreview("Name (n/):", name + " (duplicate)", false);
        }
        boolean isValid = !name.isEmpty() && Name.isValidName(name);
        if (name.isEmpty()) {
            return new FieldPreview("Name (n/):", name, false);
        }
        return new FieldPreview("Name (n/):", name, isValid);
    }

    private static FieldPreview createPhonePreview(String phone, boolean duplicate) {
        if (duplicate) {
            return new FieldPreview("Phone (p/):", phone + " (duplicate)", false);
        }
        boolean isValid = !phone.isEmpty() && Phone.isValidPhone(phone);
        if (phone.isEmpty()) {
            return new FieldPreview("Phone (p/):", phone, false);
        }
        return new FieldPreview("Phone (p/):", phone, isValid);
    }

    private static FieldPreview createEmailPreview(String email, boolean duplicate) {
        if (duplicate) {
            return new FieldPreview("Email (e/):", email + " (duplicate)", false);
        }
        boolean isValid = !email.isEmpty() && Email.isValidEmail(email);
        if (email.isEmpty()) {
            return new FieldPreview("Email (e/):", email, false);
        }
        return new FieldPreview("Email (e/):", email, isValid);
    }

    private static FieldPreview createAddressPreview(String address, boolean duplicate) {
        if (duplicate) {
            return new FieldPreview("Address (a/):", address + " (duplicate)", false);
        }
        boolean isValid = !address.isEmpty() && Address.isValidAddress(address);
        if (address.isEmpty()) {
            return new FieldPreview("Address (a/):", address, false);
        }
        return new FieldPreview("Address (a/):", address, isValid);
    }
}
