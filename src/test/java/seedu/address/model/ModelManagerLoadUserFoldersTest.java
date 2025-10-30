package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.tag.TagFolder;
import seedu.address.storage.SidebarFolderPrefs;

public class ModelManagerLoadUserFoldersTest {

    /** Helper: find a folder by (case-insensitive) display name. */
    private static TagFolder find(ObservableList<TagFolder> folders, String name) {
        return folders.stream()
                .filter(f -> f.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    @Test
    void loadsSingleTagFromTagsListAsUserSingle() {
        AddressBook ab = new AddressBook();
        UserPrefs up = new UserPrefs();
        up.setSavedSidebarFolders(List.of(
                new SidebarFolderPrefs("friends", List.of("friends"))));

        ModelManager mm = new ModelManager(ab, up);

        TagFolder f = find(mm.getActiveTagFolders(), "friends");
        assertNotNull(f, "friends folder should be created");
        assertTrue(f.isUserCreated(), "should be marked user-created");
        assertEquals(List.of("friends"), f.getQueryTags(), "queryTags should be lower-cased single");
    }

    @Test
    void loads_compositeTags_sortsAndJoinsDisplay() {
        AddressBook ab = new AddressBook();
        UserPrefs up = new UserPrefs();
        // Order in prefs is reversed; loader should normalise and sort.
        up.setSavedSidebarFolders(List.of(
                new SidebarFolderPrefs("friends & colleagues", List.of("friends", "colleagues"))));

        ModelManager mm = new ModelManager(ab, up);

        // Display is built from sorted(norm) => "colleagues & friends"
        TagFolder f = find(mm.getActiveTagFolders(), "colleagues & friends");
        assertNotNull(f, "composite folder should be created with sorted display");
        assertTrue(f.isUserCreated());
        assertEquals(List.of("colleagues", "friends"), f.getQueryTags());
    }

    @Test
    void ignores_blankOrNullAnd_duplicates() {
        AddressBook ab = new AddressBook();
        UserPrefs up = new UserPrefs();
        up.setSavedSidebarFolders(List.of(
                // blank name + null tags -> ignored
                new SidebarFolderPrefs("   ", null),
                // composite (will become "a & b")
                new SidebarFolderPrefs(null, List.of("b", "a", "a", "  ")),
                new SidebarFolderPrefs("b & a", List.of("a", "b"))
        ));

        ModelManager mm = new ModelManager(ab, up);

        // Only one composite should exist
        assertNotNull(find(mm.getActiveTagFolders(), "a & b"));
        long countAandB = mm.getActiveTagFolders().stream()
                .filter(f -> f.getName().equalsIgnoreCase("a & b"))
                .count();
        assertEquals(1, countAandB, "duplicate composite should be suppressed");
    }

    @Test
    void usesNameWhenQueryTagsIsNullOrEmpty() {
        AddressBook ab = new AddressBook();
        UserPrefs up = new UserPrefs();

        up.setSavedSidebarFolders(List.of(
                new SidebarFolderPrefs("Custom Named", null),
                new SidebarFolderPrefs("Another", List.of())
        ));

        ModelManager mm = new ModelManager(ab, up);

        assertNotNull(find(mm.getActiveTagFolders(), "Custom Named"));
        assertNotNull(find(mm.getActiveTagFolders(), "Another"));
    }
}
