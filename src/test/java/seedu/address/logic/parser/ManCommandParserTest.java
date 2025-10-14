package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ManCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersons;

/**
 * Contains tests for {@code ManCommandParser}.
 */
public class ManCommandParserTest {

    private final ManCommandParser parser = new ManCommandParser();
    private final AddressBookParser addressBookParser = new AddressBookParser();

    @Test
    public void parse_noArgs_returnsIndex() throws Exception {
        Command cmd = parser.parse("   ");
        assertNotNull(cmd);
    }

    @Test
    public void parse_singleToken_returnsTargetedManual() throws Exception {
        Command cmd = parser.parse("find");
        assertNotNull(cmd);
    }

    @Test
    public void parseCommand_manWithExtraTokens_usesFirstTokenOnly() throws Exception {
        Command c = addressBookParser.parseCommand("man edit phone");
        assertTrue(c instanceof ManCommand);

        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        String msg = ((ManCommand) c).execute(model).getFeedbackToUser();

        assertTrue(msg.contains("USAGE"));
        assertTrue(msg.toLowerCase().contains("edit"));
    }

}
