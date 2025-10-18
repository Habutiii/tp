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
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

public class BizTagCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BizTagCommand(null, null));
    }

    @Test
    public void execute_validFieldAndTags_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Tag field = new Tag(VALID_FIELD);
        Tag category = new Tag(VALID_TAG_CATEGORY);

        Set<Tag> categories = new HashSet<>();
        categories.add(category);

        BizTagCommand command = new BizTagCommand(field, categories);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addBizTags(field, categories);

        String expectedMessage = String.format(
                Messages.MESSAGE_DECLARED_BIZ_TAGS, field.toString(), categories.toString());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        Tag field = new Tag(VALID_FIELD);
        Tag diffField = new Tag(VALID_FIELD + "diff");
        Tag category = new Tag(VALID_TAG_CATEGORY);
        Tag diffCategory = new Tag(VALID_TAG_CATEGORY + "diff");

        Set<Tag> categories = new HashSet<>();
        categories.add(category);

        Set<Tag> diffCategories = new HashSet<>();
        diffCategories.add(category);
        diffCategories.add(diffCategory);


        BizTagCommand firstCommand = new BizTagCommand(field, categories);
        BizTagCommand secondCommand = new BizTagCommand(field, categories);
        BizTagCommand thirdCommand = new BizTagCommand(diffField, categories);
        BizTagCommand fourthCommand = new BizTagCommand(field, diffCategories);

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
        Tag field = new Tag(VALID_FIELD);
        Tag category = new Tag(VALID_TAG_CATEGORY);
        Set<Tag> categories = new HashSet<>();

        categories.add(category);
        BizTagCommand cmd = new BizTagCommand(field, categories);
        String manual = cmd.man();
        assertTrue(manual.contains("biz"));
        assertTrue(manual.contains("EXAMPLES"));
    }
}
