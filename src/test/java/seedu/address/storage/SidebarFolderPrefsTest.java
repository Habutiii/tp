package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SidebarFolderPrefsTest {

    @Test
    void nullQueryTagsBecomesEmptyList() {
        SidebarFolderPrefs p = new SidebarFolderPrefs("X", null);
        assertNotNull(p.getQueryTags());
        assertTrue(p.getQueryTags().isEmpty());
    }

    @Test
    void canUpdateNameViaSetterUsedByJson() {
        SidebarFolderPrefs p = new SidebarFolderPrefs("Old", List.of("a"));
        p.setName("New");
        assertEquals("New", p.getName());
    }
}
