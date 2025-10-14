package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ManCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.TypicalPersons;

/**
 * Integration tests for parsing the {@code man} command from the main AddressBookParser.
 */
public class AddressBookParserManTest {

    private final AddressBookParser parser = new AddressBookParser();

    private Model getModel() {
        return new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void parseCommand_noArgs_showsIndex() throws Exception {
        Command cmd = parser.parseCommand("man");
        assertTrue(cmd instanceof ManCommand);
        String msg = ((ManCommand) cmd).execute(getModel()).getFeedbackToUser();
        assertTrue(msg.contains("Manual index"));
    }

    @Test
    public void parseCommand_withArg_showsTargetedManual() throws Exception {
        Command cmd = parser.parseCommand("man add");
        assertTrue(cmd instanceof ManCommand);
        String msg = ((ManCommand) cmd).execute(getModel()).getFeedbackToUser();
        assertTrue(msg.contains("NAME"));
        assertTrue(msg.toLowerCase().contains("add"));
    }

    @Test
    public void parseCommand_withWhitespace_trimsCorrectly() throws Exception {
        Command cmd = parser.parseCommand("   man    find   ");
        assertTrue(cmd instanceof ManCommand);
        String msg = ((ManCommand) cmd).execute(getModel()).getFeedbackToUser();
        assertTrue(msg.toLowerCase().contains("find"));
    }

    @Test
    public void parseCommand_withUnknownCommand_showsError() throws Exception {
        Command cmd = parser.parseCommand("man doesnotexist");
        assertTrue(cmd instanceof ManCommand);
        String msg = ((ManCommand) cmd).execute(getModel()).getFeedbackToUser();
        assertTrue(msg.toLowerCase().contains("unknown command"));
        assertTrue(msg.toLowerCase().contains("run `man`"));
    }
}
