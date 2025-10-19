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

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.FeatureTag;
import seedu.address.model.tag.Tag;

public class BizUntagCommandTest {
    private static final FeatureTag FEATURE = new FeatureTag(VALID_TAG_CATEGORY);
    private static final Tag CATEGORY = new Tag(VALID_TAG_CATEGORY);

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new BizUntagCommand(null));
    }

    @Test
    public void execute_validFields_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        Set<Tag> categories = new HashSet<>();
        categories.add(CATEGORY);
        model.addBizTags(FEATURE, categories);

        Set<FeatureTag> features = new HashSet<>();
        features.add(FEATURE);
        BizUntagCommand command = new BizUntagCommand(features);

        StringBuilder unTaggedFields = new StringBuilder();
        for (Tag f : features) {
            unTaggedFields.append(f.toString()).append(" ");
        }

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        String expectedMessage = String.format(
                String.join("\n", BizUntagCommand.MESSAGE_SUCCESS, unTaggedFields.toString()));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidFields_throwsCommandException() {
        Set<FeatureTag> features = new HashSet<>();
        features.add(FEATURE);
        BizUntagCommand command = new BizUntagCommand(features);
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void execute_undoCommandUnbizCommand_success() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Set<Tag> categories = new HashSet<>();
        categories.add(CATEGORY);
        model.addBizTags(FEATURE, categories);

        Set<FeatureTag> features = new HashSet<>();
        features.add(FEATURE);

        BizUntagCommand command = new BizUntagCommand(features);
        command.execute(model);
        assertFalse(model.isBizFeature(FEATURE));
        command.undo(model);
        assertTrue(model.isBizFeature(FEATURE));
    }

    @Test
    public void equals() {
        FeatureTag diffField = new FeatureTag(VALID_FIELD + "diff");

        Set<FeatureTag> features = new HashSet<>();
        features.add(FEATURE);
        Set<FeatureTag> diffFields = new HashSet<>();
        diffFields.add(diffField);

        BizUntagCommand firstCommand = new BizUntagCommand(features);
        BizUntagCommand secondCommand = new BizUntagCommand(features);
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
