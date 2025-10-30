package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.FeatureTag;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagFolder;

/** Focused coverage for ListCommand.undo(Model). */
public class ListCommandUndoTest {

    /** Simple programmable stub for ListCommand undo tests. */
    private static class StubModel implements Model {

        private final ObservableList<TagFolder> active = FXCollections.observableArrayList();

        private boolean removeReturn = true;
        private boolean hasFolder = false;

        private boolean addActiveCalled = false;
        private boolean addCompositeCalled = false;

        // --- Methods used by ListCommand paths ---
        @Override public void updateFilteredPersonList(Predicate<Person> predicate) {
        }

        @Override public boolean hasTagFolder(String name) {
            return hasFolder;
        }

        @Override public boolean removeTagFolderByName(String name) {
            return removeReturn;
        }

        @Override public ObservableList<TagFolder> getActiveTagFolders() {
            return active;
        }

        @Override
        public ObservableList<TagFolder> getActiveTagFoldersCopy() {
            return FXCollections.observableArrayList();
        }

        @Override
        public void setActiveTagFolders(ObservableList<TagFolder> newTagFolders) {
        }

        @Override public void addActiveTagFoldersFromUser(List<String> tagNames) {
            addActiveCalled = true;
        }

        @Override public void addCompositeTagFolderFromUser(List<String> tagNames) {
            addCompositeCalled = true;
        }

        @Override public void refreshActiveTagFolderCounts() {
        }

        // --- Harmless no-ops to satisfy Model ---
        @Override public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        }
        @Override public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }
        @Override public GuiSettings getGuiSettings() {
            return null;
        }
        @Override public void setGuiSettings(GuiSettings guiSettings) {
        }
        @Override public Path getAddressBookFilePath() {
            return null;
        }
        @Override public void setAddressBookFilePath(Path addressBookFilePath) {
        }
        @Override public void setAddressBook(ReadOnlyAddressBook addressBook) {
        }
        @Override public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
        @Override public boolean hasPerson(Person person) {
            return false;
        }
        @Override public void deletePerson(Person target) {
        }
        @Override public void addPerson(Person person) {
        }
        @Override public void insertPerson(Index index, Person person) {
        }
        @Override public void setPerson(Person target, Person editedPerson) {
        }
        @Override public int getSize() {
            return 0;
        }
        @Override public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList();
        }
        @Override public ObservableList<Person> getPersonListCopy() {
            return FXCollections.observableArrayList();
        }
        @Override public boolean isBizFeature(FeatureTag feature) {
            return false;
        }
        @Override public void addBizTags(FeatureTag feature, Set<Tag> tags) {
        }
        @Override public void removeBizFeature(FeatureTag feature) {
        }
        @Override public java.util.HashMap<FeatureTag, Set<Tag>> getBizTags() {
            return new java.util.HashMap<>();
        }
        @Override public void pushMutableCommandHistory(Command command) {
        }
        @Override public Optional<Command> popLastMutableCommand() {
            return Optional.empty();
        }
        @Override public void pushUndoCommandHistory(Command command) {
        }
        @Override public Optional<Command> popLastUndoCommand() {
            return Optional.empty();
        }
        @Override public void addActiveTagFolders(List<String> tagNames) {
        }
        @Override public void addCompositeTagFolder(List<String> tagNames) {
        }
    }

    // -------- non-mutable path --------
    @Test
    void undo_nonMutable_returnsMessage() {
        StubModel model = new StubModel();
        ListCommand list = new ListCommand();
        String msg = list.undo(model);
        assertTrue(msg.toLowerCase().contains("nothing to undo"));
    }

    // -------- save-folder paths --------
    @Test
    void undoSaveNoFolderNameReturnsMessage() {
        StubModel model = new StubModel();
        // save=true, but we do NOT call execute() -> folderNameForUndo remains null
        ListCommand list = new ListCommand(p -> true, List.of("friends"), true, false);

        String msg = list.undo(model);
        assertTrue(msg.toLowerCase().contains("nothing to undo"));
    }

    @Test
    void undoSaveRemovedTrueConfirmsRemoval() throws CommandException {
        StubModel model = new StubModel();
        // Ensure execute() sets folderNameForUndo, then undo() removes it successfully.
        ListCommand list = new ListCommand(p -> true, List.of("friends"), true, false);
        // execute will call addActiveTagFoldersFromUser; we just need it to complete
        list.execute(model);

        model.removeReturn = true;
        String msg = list.undo(model);
        assertTrue(msg.toLowerCase().contains("removed folder"));
    }

    @Test
    void undoSaveRemovedFalseReportsNothingRemoved() throws CommandException {
        StubModel model = new StubModel();
        ListCommand list = new ListCommand(p -> true, List.of("friends"), true, false);
        list.execute(model);

        model.removeReturn = false;
        String msg = list.undo(model);
        assertTrue(msg.toLowerCase().contains("nothing to remove"));
    }

    // -------- delete-folder paths --------
    @Test
    void undoDeleteSnapshotNullReturnsMessage() {
        StubModel model = new StubModel();
        // delete=true, but we do NOT call execute() -> deletedSnapshot remains null
        ListCommand list = new ListCommand(p -> true, List.of("friends"), false, true);

        String msg = list.undo(model);
        assertTrue(msg.toLowerCase().contains("nothing to undo"));
    }

    @Test
    void undoDeleteRestoreSingleCallsAddActive() throws CommandException {
        StubModel model = new StubModel();
        // Prepare active folders so execute(delete) can build deletedSnapshot
        model.active.add(new TagFolder("friends", 1, List.of("friends")));
        model.hasFolder = true;
        model.removeReturn = true;

        ListCommand list = new ListCommand(p -> true, List.of("friends"), false, true);
        list.execute(model);

        String msg = list.undo(model);
        assertTrue(model.addActiveCalled);
        assertFalse(model.addCompositeCalled);
        assertTrue(msg.toLowerCase().contains("restored folder"));
    }

    @Test
    void undoDeleteRestoreMultiCallsAddComposite() throws CommandException {
        StubModel model = new StubModel();
        // Name must match the normalized "colleagues & friends"
        model.active.add(new TagFolder("colleagues & friends", 1, List.of("friends", "colleagues")));
        model.hasFolder = true;
        model.removeReturn = true;

        ListCommand list = new ListCommand(p -> true, List.of("friends", "colleagues"), false, true);
        list.execute(model);

        String msg = list.undo(model);
        assertFalse(model.addActiveCalled);
        assertTrue(model.addCompositeCalled);
        assertTrue(msg.toLowerCase().contains("restored folder"));
    }
}
