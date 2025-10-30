package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SidebarFolderPrefsTest {

    @Test
    void nullQueryTagsBecomesEmptyList() {
        SidebarFolderPrefs p = new SidebarFolderPrefs(null);
        assertNotNull(p.getQueryTags());
        assertTrue(p.getQueryTags().isEmpty());
    }
}
