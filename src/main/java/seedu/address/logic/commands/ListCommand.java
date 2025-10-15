package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all persons, or only those with the given tag.\n"
            + "Examples:\n"
            + "  list\n"
            + "  list t/policyholder";
    public static final String MESSAGE_LIST_BY_TAG_PREFIX = "Listed persons who ";
    public static final String MESSAGE_SUCCESS = "Listed all persons";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  list — Lists all persons in the address book.",
            "",
            "USAGE",
            "  list",
            "  list t/TAG",
            "",
            "EXAMPLES",
            "  list",
            "  list t/TAG",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#listing-all-persons--list"
    );

    private final Predicate<Person> predicate; // null => list all

    public ListCommand() {
        this.predicate = null;
    }

    public ListCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        if (predicate == null) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            model.updateFilteredPersonList(predicate);
            return new CommandResult(MESSAGE_LIST_BY_TAG_PREFIX + predicate);
        }
    }

    @Override
    public String man() {
        return MANUAL;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListCommand
                && ((this.predicate == null && ((ListCommand) other).predicate == null)
                || (this.predicate != null && this.predicate.equals(((ListCommand) other).predicate))));
    }
}
