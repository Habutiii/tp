package seedu.address.ui;

import java.util.Collections;
import java.util.List;

/**
 * Class for handling field preview with its label, value, validity status, and
 * optional invalid tag indices.
 */
public class FieldPreview {
    private final String label;
    private final String value;
    private final boolean isValid;
    private final List<Integer> invalidTagIndices;

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
        this.invalidTagIndices = Collections.emptyList();
    }

    /**
     * Creates a FieldPreview for tags, with invalid tag indices for UI
     * highlighting.
     *
     * @param label             The field label (e.g., "Tags (t/):")
     * @param value             The joined tag string
     * @param invalidTagIndices List of indices of invalid tags
     */
    public FieldPreview(String label, String value, List<Integer> invalidTagIndices) {
        this.label = label;
        this.value = value;
        this.isValid = invalidTagIndices.isEmpty();
        this.invalidTagIndices = invalidTagIndices;
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

    public List<Integer> getInvalidTagIndices() {
        return invalidTagIndices;
    }
}
