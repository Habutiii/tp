package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.TagFolder;

public class ClearCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_emptyAddressBook_success() {
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void undoCommand_clearCommand_restoresOriginalAddressBook() {
        Model originalModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        ClearCommand clearCommand = new ClearCommand();
        clearCommand.execute(model);
        clearCommand.undo(model);
        assertEquals(originalModel.getAddressBook(), model.getAddressBook());
    }

    @Test
    public void undoCommand_failed_whenNotExecuted() {
        ClearCommand clearCommand = new ClearCommand();
        assertThrows(IllegalStateException.class, () -> clearCommand.undo(model));
    }

    @Test
    public void is_mutable() {
        ClearCommand clearCommand = new ClearCommand();
        assertTrue(clearCommand.isMutable());
    }

    @Test
    public void man_returnsManualString() {
        ClearCommand cmd = new ClearCommand();
        String manual = cmd.man();
        assertTrue(manual.contains("clear"));
        assertTrue(manual.contains("USAGE"));
    }

    @Test
    public void undo_restoresOnlyTagFolders_whenAddressBookNull() throws Exception {
        // Prepare model with some folders
        Model localModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        ClearCommand clearCommand = new ClearCommand();

        // store tag folders copy
        ObservableList<TagFolder> storedFolders = localModel.getActiveTagFoldersCopy();

        // set currentAddressBook to null and currentTagFolders to storedFolders via reflection
        Field addrField = ClearCommand.class.getDeclaredField("currentAddressBook");
        addrField.setAccessible(true);
        addrField.set(clearCommand, null);

        Field tagField = ClearCommand.class.getDeclaredField("currentTagFolders");
        tagField.setAccessible(true);
        tagField.set(clearCommand, storedFolders);

        // clear model's active tag folders
        localModel.setActiveTagFolders(null);
        assertTrue(localModel.getActiveTagFolders().isEmpty());

        // perform undo which should restore tag folders only
        clearCommand.undo(localModel);
        assertEquals(storedFolders, localModel.getActiveTagFolders());
    }

    @Test
    public void undo_restoresOnlyAddressBook_whenTagFoldersNull() throws Exception {
        Model localModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // prepare stored address book
        AddressBook storedBook = new AddressBook(localModel.getAddressBook().getPersonList());

        ClearCommand clearCommand = new ClearCommand();

        // set currentAddressBook to storedBook and currentTagFolders to null via reflection
        Field addrField = ClearCommand.class.getDeclaredField("currentAddressBook");
        addrField.setAccessible(true);
        addrField.set(clearCommand, storedBook);

        Field tagField = ClearCommand.class.getDeclaredField("currentTagFolders");
        tagField.setAccessible(true);
        tagField.set(clearCommand, null);

        // clear the model's address book
        localModel.setAddressBook(new AddressBook());
        assertEquals(0, localModel.getSize());

        // perform undo which should restore address book only
        clearCommand.undo(localModel);
        assertEquals(storedBook, localModel.getAddressBook());
    }
}
