package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class FieldPreviewTest {

    @Test
    public void getInvalidTagIndices_returnsCorrectIndices() {
        List<Integer> invalidIndices = Arrays.asList(1, 3);
        FieldPreview preview = new FieldPreview("Tags (t/):", "tag1, , tag3, ", invalidIndices);
        assertEquals(invalidIndices, preview.getInvalidTagIndices());
        assertTrue(preview.getInvalidTagIndices().contains(1));
        assertTrue(preview.getInvalidTagIndices().contains(3));
    }
}
