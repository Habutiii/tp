package seedu.address.model.tag;

public class FieldTag extends  Tag {
    public static final String MESSAGE_CONSTRAINTS =
            "Feature names should only contain English letters, digits, or '-' (dash), and must not be empty.";

    public FieldTag(String tagName) {
        super(tagName);
    }
}
