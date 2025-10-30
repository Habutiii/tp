package seedu.address.model.tag;

/**
 * Represents a Field, also known as a Feature in the {@code Stats} class.
 */
public class FeatureTag extends Tag {
    public static final String MESSAGE_CONSTRAINTS =
            Tag.MESSAGE_CONSTRAINTS.replace("tag name", "feature name");
    // Field is known as Feature Names for better understanding for the User.

    /**
     * Constructs a {@code FeatureTag}
     * @param tagName A valid feature name.
     */
    public FeatureTag(String tagName) {
        super(tagName);
    }
}
