package seedu.address.logic.parser;

import seedu.address.logic.commands.ManCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses input arguments and creates a new ManCommand object. */
public class ManCommandParser implements Parser<ManCommand> {
    @Override
    public ManCommand parse(String args) throws ParseException {
        if (args == null || args.trim().isEmpty()) {
            return new ManCommand(""); // no target
        }
        String[] parts = args.trim().split("\\s+");
        return new ManCommand(parts[0]); // first token only
    }
}
