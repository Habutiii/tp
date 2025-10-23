package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FIELD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_CATEGORY;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.ClientMatchesPredicate;
import seedu.address.model.tag.FeatureTag;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.AddressBookBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void manipulate_bizTags_successful() throws CommandException {
        modelManager.addPerson(ALICE);
        FeatureTag feature = new FeatureTag(VALID_FIELD);
        Tag category = new Tag(VALID_TAG_CATEGORY);
        Set<Tag> tags = new HashSet<>(List.of(category));
        modelManager.addBizTags(feature, tags);

        assertTrue(modelManager.isBizFeature(feature));
        assertEquals(tags, modelManager.getBizTags().get(feature));

        modelManager.removeBizFeature(feature);
        assertFalse(modelManager.isBizFeature(feature));
        assertNull(modelManager.getBizTags().get(feature));
    }

    @Test
    public void checkSize_emptyList_returnsTrue() {
        assertTrue(modelManager.getSize() == 0);
    }

    @Test
    public void checkSize_onePerson_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.getSize() == 1);
    }

    @Test
    public void notMutableCommand_notPushedToStack() {
        // an not mutable command
        Command notMutableCommand = new Command() {
            @Override
            public CommandResult execute(Model model) {
                return null;
            }

            @Override
            public String man() {
                return "";
            }
        };

        // push it to the history stack
        modelManager.pushMutableCommandHistory(notMutableCommand);
        assertTrue(modelManager.popLastMutableCommand().isEmpty());

        // push it to the undo stack
        modelManager.pushUndoCommandHistory(notMutableCommand);
        assertTrue(modelManager.popLastUndoCommand().isEmpty());
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new ClientMatchesPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }

    @Test
    void folderKey_sortsAndLowersCorrectly() throws Exception {
        var list = java.util.List.of("B", "a", "C");
        java.lang.reflect.Method m = ModelManager.class.getDeclaredMethod("folderKey", java.util.List.class);
        m.setAccessible(true);
        String key = (String) m.invoke(null, list);
        assertEquals("a|b|c", key);
    }

    @Test
    void getActiveTagFoldersndSetActiveTagFolders_noop() {
        ModelManager m = new ModelManager();
        assertNotNull(m.getActiveTagFolders());
        int sizeBefore = m.getActiveTagFolders().size();

        // setActiveTagFolders does nothing; should not throw
        m.setActiveTagFolders(java.util.List.of("x", "y"));

        assertEquals(sizeBefore, m.getActiveTagFolders().size());
        assertThrows(UnsupportedOperationException.class, () ->
                m.getActiveTagFolders().add(new seedu.address.model.tag.TagFolder("bad", 1))
        );
    }

    @Test
    void addActiveTagFolders_handlesNullEmptyAndAdds() {
        ModelManager m = new ModelManager();

        m.addActiveTagFolders(null);
        m.addActiveTagFolders(java.util.Collections.emptyList());
        assertTrue(m.getActiveTagFolders().isEmpty());

        m.addActiveTagFolders(java.util.List.of("friends", "colleagues"));
        assertTrue(m.getActiveTagFolders().size() == 2);
    }

    @Test
    void addCompositeTagFolder_coversAllBranches() {
        ModelManager m = new ModelManager();

        // null → return
        m.addCompositeTagFolder(null);
        // empty → return
        m.addCompositeTagFolder(java.util.Collections.emptyList());

        // valid add
        m.addCompositeTagFolder(java.util.List.of("Friends", " Colleagues ", "friends"));
        assertTrue(m.getActiveTagFolders().size() == 1);

        // duplicate key path triggers refresh (no add)
        m.addCompositeTagFolder(java.util.List.of("colleagues", "friends"));
        assertTrue(m.getActiveTagFolders().size() == 1);
    }

    @Test
    void ensureFoldersExistForTags_handlesNullAndAddsNewFolders() throws Exception {
        ModelManager m = new ModelManager();

        java.lang.reflect.Method method = ModelManager.class.getDeclaredMethod(
                "ensureFoldersExistForTags", java.util.Collection.class);
        method.setAccessible(true);

        // null → return
        method.invoke(m, (Object) null);
        assertTrue(m.getActiveTagFolders().isEmpty());

        // add new tags
        var tags = java.util.List.of(new seedu.address.model.tag.Tag("alpha"), new seedu.address.model.tag.Tag("beta"));
        method.invoke(m, tags);
        assertTrue(m.getActiveTagFolders().size() == 2);
    }

    @Test
    void bootstrapAllTags_populatesFoldersFromPeople() throws Exception {
        ModelManager m = new ModelManager();
        java.lang.reflect.Method bootstrap = ModelManager.class.getDeclaredMethod("bootstrapAllTags");
        bootstrap.setAccessible(true);
        bootstrap.invoke(m);
        assertNotNull(m.getActiveTagFolders());
    }

}
