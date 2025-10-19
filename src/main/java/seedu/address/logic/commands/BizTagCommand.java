package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_DECLARED_BIZ_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FIELD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Declares Fields, and Tags to be used as Categories.
 */
public class BizTagCommand extends Command {
    public static final String COMMAND_WORD = "biz";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Declares Fields, and Tags as Categories for Statistics.\n"
            + "Parameters: "
            + PREFIX_FIELD + "FIELD "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_FIELD + "Plan "
            + PREFIX_TAG + "A "
            + PREFIX_TAG + "B";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  biz — Declares Fields, and Tags as Categories for Statistics.",
            "",
            "USAGE",
            "  biz f/FIELD [t/TAG]…",
            "",
            "PARAMETERS",
            "  • FIELD: non-empty string, may contain spaces but not leading/trailing spaces",
            "  • TAG: alphanumeric, no spaces; you may have multiple t/TAG categories for the FIELD",
            "",
            "EXAMPLES",
            "  biz f/Plan t/A t/B",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#adding-a-person-add"
    );

    public static final String UNDO_SUCCESS = "The following Field(s) have been undeclared:\n%s";

    private final Tag field;
    private final Set<Tag> tags;

    /**
     * Creates a BizTagCommand to declare the specified {@code Field} and {@code Categories}
     * @param field Field
     * @param tags to be used as Categories in statistics
     */
    public BizTagCommand(Tag field, Set<Tag> tags) {
        requireNonNull(field);
        requireNonNull(tags);
        this.field = field;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BizTagCommand)) {
            return false;
        }
        BizTagCommand e = (BizTagCommand) other;
        return field.equals(e.field)
                && tags.equals(e.tags);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.addBizTags(field, tags);
        return new CommandResult(String.format(MESSAGE_DECLARED_BIZ_TAGS, field.toString(), tags.toString()));
    }

    @Override
    public String man() {
        return MANUAL;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public String undo(Model model) {
        requireNonNull(model);
        model.removeBizField(field);
        return String.format(UNDO_SUCCESS, field.toString());
    }
}
