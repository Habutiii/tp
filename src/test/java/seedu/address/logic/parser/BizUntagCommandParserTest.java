package seedu.address.logic.parser;

import static seedu.address.logic.commands.CommandTestUtil.VALID_FIELD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_CATEGORY;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

public class BizUntagCommandParserTest {
    private static final Tag FIELD = new Tag(VALID_FIELD);
    private static final Tag CATEGORY = new Tag(VALID_TAG_CATEGORY);

    private BizUntagCommandParser parser = new BizUntagCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {

    }
}
