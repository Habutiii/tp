package seedu.address.model;

import java.nio.file.Path;
import java.util.List;

import seedu.address.commons.core.GuiSettings;
import seedu.address.storage.SidebarFolderPrefs;

/**
 * Unmodifiable view of user prefs.
 */
public interface ReadOnlyUserPrefs {

    GuiSettings getGuiSettings();

    Path getAddressBookFilePath();

    List<SidebarFolderPrefs> getSavedSidebarFolders();
}
