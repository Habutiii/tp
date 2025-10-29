package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.ValidationConstants.PRINTABLE_ASCII_REGEX;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "Names may only contain English letters (A–Z, a–z), spaces and these special characters: , ( ) / . @ - '\n"
                    + "Names must not contain numbers and must not start with a space. \n"
                    + "A name should also not exceed 100 characters.";

    /*
     * First char cannot be a space.
     * Only the allowed ASCII set is permitted. Digits are excluded.
     */
    // Allowed special characters need escaping in regex where applicable.
    public static final int MAX_LENGTH = 100;
    private static final String ALLOWED_FIRST_CHAR_CLASS = "[A-Za-z(),/\\.@\\-']";
    private static final String ALLOWED_REST_CLASS = "[A-Za-z(),/\\.@\\-' ]";

    public static final String VALIDATION_REGEX =
            "^" + ALLOWED_FIRST_CHAR_CLASS + ALLOWED_REST_CLASS + "*$";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
        fullName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.length() <= MAX_LENGTH
                && test.matches(PRINTABLE_ASCII_REGEX)
                && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
