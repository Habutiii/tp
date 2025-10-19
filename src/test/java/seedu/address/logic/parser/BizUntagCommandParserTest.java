package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.FIELD_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FIELD;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.BizUntagCommand;
    import seedu.address.model.tag.FeatureTag;

public class BizUntagCommandParserTest {
            private static final FeatureTag FIELD = new FeatureTag(VALID_FIELD);
            private static final FeatureTag DIFF_FIELD = new FeatureTag(VALID_FIELD + "diff");

    private BizUntagCommandParser parser = new BizUntagCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
            Set<FeatureTag> features = new HashSet<>();
        features.add(FIELD);

        // Single field
        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE
                        + FIELD_DESC, new BizUntagCommand(features));

        // Multiple features
        features.add(DIFF_FIELD);

        assertParseSuccess(parser,
                PREAMBLE_WHITESPACE
                + FIELD_DESC + FIELD_DESC + "diff", new BizUntagCommand(features));

    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        assertParseFailure(parser, PREAMBLE_WHITESPACE,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, BizUntagCommand.MESSAGE_USAGE));
    }
}
