package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SidebarFolderPrefsTest {

    @Test
    void null_queryTags_becomes_empty_list() {
        SidebarFolderPrefs p = new SidebarFolderPrefs("X", null);
        assertNotNull(p.getQueryTags());
        assertTrue(p.getQueryTags().isEmpty());
    }

    @Test
    void can_update_name_via_setter_used_by_json() {
        SidebarFolderPrefs p = new SidebarFolderPrefs("Old", List.of("a"));
        p.setName("New");
        assertEquals("New", p.getName());
    }
}