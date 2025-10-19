package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_MISSING_BIZ_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FIELD;

import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

public class BizUntagCommand extends Command {
    public static final String COMMAND_WORD = "unbiz";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undeclares Fields, and their Tags as Categories from Statistics.\n"
            + "Parameters: "
            + "[" + PREFIX_FIELD + "FIELD]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_FIELD + "Plan "
            + PREFIX_FIELD + "Gender";
    public static final String MESSAGE_SUCCESS = "The following Fields have been undeclared:\n";
    public static final String MANUAL = String.join("\n",
            "NAME",
            "  unbiz — Undeclares Field(s), and their Tags as Categories from Statistics.",
            "",
            "USAGE",
            "  biz [f/FIELD] ...",
            "",
            "PARAMETERS",
            "  • FIELD: non-empty string, may contain spaces but not leading/trailing spaces",
            "",
            "EXAMPLES",
            "  unbiz f/Plan f/Gender",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#adding-a-person-add"
    );

    private final Set<Tag> fields;

    /**
     * Creates an BizUntagCommand to undeclare specified {@code fields} in Statistics.
     * @param fields Fields currently declared in Statistics
     */
    public BizUntagCommand(Set<Tag> fields) {
        this.fields = fields;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        StringBuilder missingFields = new StringBuilder();
        for (Tag field : fields) {
            if (!model.isBizField(field)) {
                missingFields.append(field.toString()).append(" ");
            }
        }

        if (!missingFields.isEmpty()) {
            throw new CommandException(
                    String.format(MESSAGE_INVALID_MISSING_BIZ_TAGS,
                            "[" + missingFields + "]"));
        }

        StringBuilder unTaggedFields = new StringBuilder();
        for (Tag field : fields) {
            model.removeBizField(field);
            unTaggedFields.append(field.toString()).append(" ");
        }

        return new CommandResult(
                String.join("\n", MESSAGE_SUCCESS, unTaggedFields.toString()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BizUntagCommand)) {
            return false;
        }
        BizUntagCommand e = (BizUntagCommand) other;
        return fields.equals(e.fields);
    }

    @Override
    public String man() {
        return MANUAL;
    }
}
