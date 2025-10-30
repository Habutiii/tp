package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Serializable DTO representing a user-saved sidebar folder.
 * Stored inside {@code UserPrefs}.
 */
public class SidebarFolderPrefs {

    /** Display name, e.g. "friends & colleagues". */
    private String name;

    /** Lower-cased query tags for this folder, e.g. ["friends", "colleagues"]. */
    private List<String> queryTags;

    /** Canonical ctor for programmatic creation. */
    @JsonCreator
    public SidebarFolderPrefs(
            @JsonProperty("name") String name,
            @JsonProperty("queryTags") List<String> queryTags) {
        this.name = (name == null) ? "" : name;
        this.queryTags = (queryTags == null) ? new ArrayList<>() : new ArrayList<>(queryTags);
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = (name == null) ? "" : name;
    }

    @JsonProperty("queryTags")
    public List<String> getQueryTags() {
        return queryTags;
    }
}
