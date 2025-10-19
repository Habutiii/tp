package seedu.address.logic.commands;

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
import seedu.address.model.tag.Tag;

public class BizUntagCommandTest {
    private final Tag field = new Tag(VALID_TAG_CATEGORY);
    private final Tag category = new Tag(VALID_TAG_CATEGORY);

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BizUntagCommand(null));
    }

    @Test
    public void execute_validFields_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Set<Tag> categories = new HashSet<>();
        categories.add(category);
        model.addBizTags(field, categories);

        Set<Tag> fields = new HashSet<>();
        fields.add(field);
        BizUntagCommand command = new BizUntagCommand(fields);

        StringBuilder unTaggedFields = new StringBuilder();
        for (Tag f : fields) {
            unTaggedFields.append(field.toString()).append(" ");
        }

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        String expectedMessage = String.format(
                String.join("\n", BizUntagCommand.MESSAGE_SUCCESS, unTaggedFields.toString()));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidFields_throwsCommandException() {
        Set<Tag> fields = new HashSet<>();
        fields.add(field);
        BizUntagCommand command = new BizUntagCommand(fields);
        Model  model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_UndoCommand_unbizCommand_successfulUndo() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Set<Tag> categories = new HashSet<>();
        categories.add(category);
        model.addBizTags(field, categories);

        Set<Tag> fields = new HashSet<>();
        fields.add(field);

        BizUntagCommand command = new BizUntagCommand(fields);
        command.execute(model);
        assertFalse(model.isBizField(field));
        command.undo(model);
        assertTrue(model.isBizField(field));
    }

    @Test
    public void equals() {
        Tag diffField = new Tag(VALID_FIELD + "diff");

        Set<Tag> fields = new HashSet<>();
        fields.add(field);
        Set<Tag> diffFields = new HashSet<>();
        diffFields.add(diffField);

        BizUntagCommand firstCommand = new BizUntagCommand(fields);
        BizUntagCommand secondCommand = new BizUntagCommand(fields);
        BizUntagCommand thirdCommand = new BizUntagCommand(diffFields);

        // same object -> returns true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> returns true
        assertTrue(firstCommand.equals(secondCommand));

        // different types -> returns false
        assertFalse(firstCommand.equals(1));

        // null -> returns false
        assertFalse(firstCommand.equals(null));

        // different Fields -> returns false
        assertFalse(firstCommand.equals(thirdCommand));



    }
}
