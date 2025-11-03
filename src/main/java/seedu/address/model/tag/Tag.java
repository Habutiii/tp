package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.ValidationConstants.PRINTABLE_ASCII_REGEX;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS =
            "A tag name should contain only English letters, digits, or '-'(dash) but a trailing '-' is rejected."
                    + "It must start and end with a letter or digit, mo whitespaces and must not exceed 40 characters.";

    public static final String VALIDATION_REGEX = "^[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?$";
    public static final int MAX_LENGTH = 40;

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
        this.tagName = tagName;
    }

    /**
     * Returns true if a given string is a valid tag names.
     */
    public static boolean isValidTagName(String test) {
        return test.length() <= MAX_LENGTH
                && test.matches(VALIDATION_REGEX)
                && test.matches(PRINTABLE_ASCII_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equalsIgnoreCase(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.toLowerCase().hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return tagName;
    }

}
