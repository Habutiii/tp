package seedu.address.ui;

/**
 * Class for handling field preview with its label, value, and validity status.
 */
public class FieldPreview {
    private final String label;
    private final String value;
    private final boolean isValid;

    /**
     * Creates a FieldPreview with the specified label, value, and validity.
     *
     * @param label   The field label (e.g., "Name (n/):")
     * @param value   The field value entered by the user
     * @param isValid Whether the field value is valid
     */
    public FieldPreview(String label, String value, boolean isValid) {
        this.label = label;
        this.value = value;
        this.isValid = isValid;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public boolean isValid() {
        return isValid;
    }
}
