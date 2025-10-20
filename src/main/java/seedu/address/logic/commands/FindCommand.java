package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.ClientMatchesPredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all clients whose "
            + "name, phone number, or email contain any of the specified keywords "
            + "(case-insensitive, partial match allowed), and displays them as a list "
            + "with index numbers.\n"
            + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
            + "Example: " + COMMAND_WORD + " alice 9123 gmail";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  find — Finds all persons whose names contain any of the given keywords.",
            "",
            "USAGE",
            "  find KEYWORD [MORE_KEYWORDS]",
            "",
            "PARAMETERS",
            "  • Keywords are case-insensitive; order doesn’t matter.",
            "  • Partial matches is now supported.",
            "",
            "EXAMPLES",
            "  find Alice",
            "  find Alice Bob",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#locating-persons-by-name-find"
    );

    private final ClientMatchesPredicate predicate;

    public FindCommand(ClientMatchesPredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public String man() {
        return MANUAL;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand otherFindCommand)) {
            return false;
        }

        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
