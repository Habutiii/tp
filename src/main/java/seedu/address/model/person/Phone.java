package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.ValidationConstants.PRINTABLE_ASCII_REGEX;

/**
 * Represents a Person's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {



    public static final String MESSAGE_CONSTRAINTS =
            "Phone numbers should be 3 to 15 digits.";
    /*
     * The phone number must be between 3 and 15 digits long.
     * Shortcodes are usually 3 to 5 digits long.
     * International numbers are usually up to 15 digits long by ITU-T E.164 standard.
     * Thus, we set the minimum length to 3 and maximum length to 15.
     * The phone number should only contain digits.
     */
    public static final String VALIDATION_REGEX = "\\d{3,15}";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        checkArgument(isValidPhone(phone), MESSAGE_CONSTRAINTS);
        value = phone;
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {

        return test.matches(PRINTABLE_ASCII_REGEX)
                && test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
