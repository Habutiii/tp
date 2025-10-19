package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FEATURE;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.logic.commands.BizUntagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.FeatureTag;

/**
 * Parses input arguments and creates a new BizUntagCommand object
 */
public class BizUntagCommandParser implements Parser<BizUntagCommand> {
    @Override
    public BizUntagCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_FEATURE);

        if (!arePrefixesPresent(argMultimap, PREFIX_FEATURE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(
                    MESSAGE_INVALID_COMMAND_FORMAT, BizUntagCommand.MESSAGE_USAGE));
        }

        Set<FeatureTag> features = ParserUtil.parseFeatureTags(argMultimap.getAllValues(PREFIX_FEATURE));

        return new BizUntagCommand(features);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
