package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.TagMatchesAllPredicate;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new {@code ListCommand} object.
 * <p>
 * This parser supports both:
 * <ul>
 *   <li>Listing all persons when no tag prefix is provided (e.g., {@code list})</li>
 *   <li>Listing persons who have <em>all</em> of the specified tags when one or more
 *       {@code t/} prefixes are provided (e.g., {@code list t/friends t/colleagues})</li>
 * </ul>
 *
 * <p>Example usages:
 * <pre>
 *     list
 *     list t/friends
 *     list t/friends t/colleagues
 * </pre>
 *
 * The parser tokenizes the input by the {@code PREFIX_TAG} and converts each tag
 * value into a {@link seedu.address.model.tag.Tag} object. These are collected
 * into a {@link java.util.Set}, and a {@link seedu.address.model.person.TagMatchesAllPredicate}
 * is created to filter the person list.
 */
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
