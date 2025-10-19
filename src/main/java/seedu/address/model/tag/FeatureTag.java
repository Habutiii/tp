package seedu.address.model.tag;

/**
 * Represents a Field, also known as a Feature in the {@code Stats} class.
 */
public class FeatureTag extends  Tag {
    public static final String MESSAGE_CONSTRAINTS =
            "Feature names should only contain English letters, digits, or '-' (dash), and must not be empty.";
    // Field is known as Feature Names for better understanding for the User.

    /**
     * Constructs a {@code FieldTag}
     * @param tagName A valid field name.
     */
    public FeatureTag(String tagName) {
        super(tagName);
    }
}
