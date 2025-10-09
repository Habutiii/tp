package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.StatsCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class StatsCommandParserTest {
    private final StatsCommandParser statsCommandParser = new StatsCommandParser();

    @Test
    public void parse_validArgs_returnsStatsCommand() throws ParseException {
        StatsCommand expectedStatsCommand = new StatsCommand();
        assertParseSuccess(statsCommandParser,
                "//potential future args", expectedStatsCommand);
    }

    // No ParseExceptions yet as no args yet
    @Test
    public void parse_invalidArgs_throwsParseException() {

    }
}
