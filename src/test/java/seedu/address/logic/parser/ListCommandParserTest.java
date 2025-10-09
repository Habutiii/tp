package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagMatchesAllPredicate;
import seedu.address.model.tag.Tag;

class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();

    @Test
    void parse_noArgs_listsAll() throws Exception {
        ListCommand cmd = parser.parse("   "); // spaces ok
        // equals() in ListCommand treats both with null predicate as equal
        assertEquals(new ListCommand(), cmd);
    }

    @Test
    void parse_singleTag_buildsAllPredicate() throws Exception {
        ListCommand cmd = parser.parse(" t/friends");
        Set<Tag> expected = new LinkedHashSet<>();
        expected.add(new Tag("friends"));
        assertEquals(new ListCommand(new TagMatchesAllPredicate(expected)), cmd);
    }

    @Test
    void parse_multipleTags_buildsAllPredicate() throws Exception {
        ListCommand cmd = parser.parse(" t/friends t/colleagues");
        Set<Tag> expected = new LinkedHashSet<>();
        expected.add(new Tag("friends"));
        expected.add(new Tag("colleagues"));
        assertEquals(new ListCommand(new TagMatchesAllPredicate(expected)), cmd);
    }

    @Test
    void parse_invalidTag_throwsParseException() {
        // Non-alnum should fail your Tag validation
        assertThrows(ParseException.class, () -> parser.parse(" t/invalid!"));
    }
}
