package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.model.person.TagMatchesAllPredicate;
import seedu.address.model.tag.Tag;

/**
 * Unit tests for {@code ListCommandParser}.
 */
public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_noArgs_listsAll() {
        // no tag prefix -> list all persons
        assertParseSuccess(parser, "   ", new ListCommand());
    }

    @Test
    public void parse_singleTag_success() {
        Set<Tag> expected = new LinkedHashSet<>();
        expected.add(new Tag("friends"));
        ListCommand expectedCommand = new ListCommand(new TagMatchesAllPredicate(expected));

        assertParseSuccess(parser, " t/friends", expectedCommand);
    }

    @Test
    public void parse_multipleTags_success() {
        Set<Tag> expected = new LinkedHashSet<>();
        expected.add(new Tag("friends"));
        expected.add(new Tag("colleagues"));
        ListCommand expectedCommand = new ListCommand(new TagMatchesAllPredicate(expected));

        assertParseSuccess(parser, " t/friends t/colleagues", expectedCommand);
    }

    @Test
    public void parse_invalidTag_failure() {
        // invalid tag name violates Tag constraints
        assertParseFailure(parser, " t/invalid!", Tag.MESSAGE_CONSTRAINTS);
    }
}
