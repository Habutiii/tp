package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  clear — Clears all entries from the address book.",
            "",
            "USAGE",
            "  clear",
            "",
            "DESCRIPTION",
            "  Removes every person currently stored in the address book.",
            "  This action cannot be undone.",
            "",
            "EXAMPLES",
            "  clear",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#clearing-all-entries--clear"
    );

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public String man() {
        return MANUAL;
    }
}
