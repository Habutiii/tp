package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.FIELD_DESC;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_CATEGORY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_FIELD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FIELD;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.BizTagCommand;
import seedu.address.model.tag.FieldTag;
import seedu.address.model.tag.Tag;

public class BizTagCommandParserTest {
    private BizTagCommandParser parser = new BizTagCommandParser();
    @Test
    public void parse_allFieldsPresent_success() {
        FieldTag field = new FieldTag(VALID_FIELD);
        Tag category = new Tag(VALID_TAG_CATEGORY);
        Set<Tag> categories = new HashSet<>();
        categories.add(category);

        // whitespace only preamble
        assertParseSuccess(
                parser, PREAMBLE_WHITESPACE
                        + FIELD_DESC
                        + TAG_DESC_CATEGORY, new BizTagCommand(field, categories));


        // multiple tags - all accepted
        categories.add(category);
        assertParseSuccess(parser,
                 FIELD_DESC + TAG_DESC_CATEGORY + TAG_DESC_CATEGORY,
                new BizTagCommand(field, categories));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        Tag category = new Tag(VALID_TAG_CATEGORY);
        Set<Tag> categories = new HashSet<>();
        categories.add(category);

        // Missing Field
        assertParseFailure(parser, TAG_DESC_CATEGORY,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, BizTagCommand.MESSAGE_USAGE));

        // Missing Tags
        assertParseFailure(parser, FIELD_DESC,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, BizTagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedField_failure() {
        assertParseFailure(parser, FIELD_DESC + FIELD_DESC + TAG_DESC_CATEGORY,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_FIELD));
    }
}
