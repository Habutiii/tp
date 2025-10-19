package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FIELD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_CATEGORY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.FeatureTag;
import seedu.address.model.tag.Tag;

public class BizTagCommandTest {
    private final FeatureTag feature = new FeatureTag(VALID_FIELD);
    private final Tag category = new Tag(VALID_TAG_CATEGORY);

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BizTagCommand(null, null));
    }

    @Test
    public void execute_validFieldAndTags_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Set<Tag> categories = new HashSet<>();
        categories.add(category);

        BizTagCommand command = new BizTagCommand(feature, categories);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addBizTags(feature, categories);

        String expectedMessage = String.format(
                    BizTagCommand.MESSAGE_DECLARED_BIZ_TAGS,
                String.join(" ", feature.toString(), categories.toString()));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void undoCommand_bizCommand_successfulUndo() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        // Undoing for Non-overwrite
        Set<Tag> categories = new HashSet<>();
        categories.add(category);
        BizTagCommand command = new BizTagCommand(feature, categories);
        command.execute(model);
        assertTrue(model.isBizFeature(feature));
        command.undo(model);
        assertFalse(model.isBizFeature(feature));

        // Undoing for overwrite
        Set<Tag> newCategories = new HashSet<>();
        Tag newCategory = new Tag(VALID_TAG_CATEGORY + "different");
        newCategories.add(newCategory);
        newCategories.add(category);
        model.addBizTags(feature, newCategories);
        command.execute(model);
        assertTrue(model.isBizFeature(feature));
        assertEquals(model.getBizTags().get(feature), categories);
        command.undo(model);
        assertTrue(model.isBizFeature(feature));
        assertEquals(model.getBizTags().get(feature), newCategories);
    }

    @Test
    public void equals() {
        FeatureTag diffField = new FeatureTag(VALID_FIELD + "diff");
        Tag diffCategory = new Tag(VALID_TAG_CATEGORY + "diff");

        Set<Tag> categories = new HashSet<>();
        categories.add(category);

        Set<Tag> diffCategories = new HashSet<>();
        diffCategories.add(category);
        diffCategories.add(diffCategory);


        BizTagCommand firstCommand = new BizTagCommand(feature, categories);
        BizTagCommand secondCommand = new BizTagCommand(feature, categories);
        BizTagCommand thirdCommand = new BizTagCommand(diffField, categories);
        BizTagCommand fourthCommand = new BizTagCommand(feature, diffCategories);

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        assertTrue(firstCommand.equals(secondCommand));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different Field and Tags -> returns false
        assertFalse(firstCommand.equals(thirdCommand));
        assertFalse(firstCommand.equals(fourthCommand));
        assertFalse(thirdCommand.equals(fourthCommand));
    }

    @Test
    public void man_returnsManualString() {
        Set<Tag> categories = new HashSet<>();

        categories.add(category);
        BizTagCommand cmd = new BizTagCommand(feature, categories);
        String manual = cmd.man();
        assertTrue(manual.contains("biz"));
        assertTrue(manual.contains("EXAMPLES"));
    }
}
