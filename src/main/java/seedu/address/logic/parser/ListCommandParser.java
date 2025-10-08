package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagMatchesAllPredicate;
import seedu.address.model.tag.Tag;

public class ListCommandParser implements Parser<ListCommand> {
    @Override
    public ListCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        // Collect ALL occurrences of t/
        List<String> tagValues = argMultimap.getAllValues(PREFIX_TAG);

        if (tagValues.isEmpty()) {
            return new ListCommand(); // no tag -> list all
        }

        Set<Tag> required = new LinkedHashSet<>();
        for (String v : tagValues) {
            required.add(ParserUtil.parseTag(v));
        }

        return new ListCommand(new TagMatchesAllPredicate(required));
    }
}
