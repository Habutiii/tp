package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Serializable DTO representing a user-saved sidebar folder.
 * Stored inside {@code UserPrefs}.
 */
public class SidebarFolderPrefs {


    /** Lower-cased query tags for this folder, e.g. ["friends", "colleagues"]. */
    private List<String> queryTags;

    /** Canonical ctor for programmatic creation. */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public SidebarFolderPrefs(List<String> queryTags) {
        this.queryTags = (queryTags == null) ? new ArrayList<>() : new ArrayList<>(queryTags);
    }

    @JsonValue
    public List<String> getQueryTags() {
        return queryTags;
    }
}
