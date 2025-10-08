package seedu.address.logic.parser;

import seedu.address.logic.commands.StatsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class StatsCommandParser implements Parser<StatsCommand>{
    public StatsCommand parse(String args) throws ParseException {
        return new StatsCommand();
    }
}
