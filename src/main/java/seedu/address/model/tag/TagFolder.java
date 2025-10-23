package seedu.address.model.tag;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Lightweight view model representing a tag “folder” with a name and a count.
 * <p>
 * Equality is case-insensitive on the {@code name}, so {@code friends} and {@code Friends}
 * are treated as the same folder for comparison and hashing.
 */
public final class TagFolder {

    private final String name;
    private IntegerProperty count = new SimpleIntegerProperty();

    /**
     * Creates a {@code TagFolder}.
     *
     * @param name  display name of the tag folder (as typed by user)
     * @param count number of persons in this folder
     */
    public TagFolder(String name, int count) {
        this.name = name;
        this.count.set(count);
    }

    /**
     * Returns the display name of this folder.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of persons contained in this folder.
     */
    public int getCount() {
        return count.get();
    }

    /** Update the badge count. */
    public void setCount(int value) {
        count.set(value);
    }

    public IntegerProperty countProperty() {
        return count;
    }

    @Override
    public String toString() {
        return name + " (" + count + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TagFolder)) {
            return false;
        }
        TagFolder that = (TagFolder) other;
        return this.name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }
}
