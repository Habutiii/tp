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
        // 0) Pull a trailing lone "s" off the raw args first, so it doesn't get
        // glued to the last t/ value by the tokenizer.
        String raw = args == null ? "" : args.trim();
        boolean saveFolder = false;
        if (!raw.isEmpty() && raw.matches(".*\\s+s\\s*$")) {
            // ends with whitespace + 's' (and optional trailing whitespace)
            saveFolder = true;
            raw = raw.replaceFirst("\\s+s\\s*$", "");
        }

        // 1) Tokenize normally (now without the trailing 's')
        ArgumentMultimap map = ArgumentTokenizer.tokenize(raw, PREFIX_TAG);

        // 2) Collect tag values
        List<String> tagValues = map.getAllValues(PREFIX_TAG);

        if (tagValues.isEmpty()) {
            // no tags -> plain list-all; ignore 's'
            return new ListCommand();
        }

        // 3) Build predicate
        Set<Tag> required = new LinkedHashSet<>();
        for (String v : tagValues) {
            required.add(ParserUtil.parseTag(v));
        }

        // 4) Pass tags and the save flag to the command
        return new ListCommand(new TagMatchesAllPredicate(required), tagValues, saveFolder);
    }
}
