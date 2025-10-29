package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Clears the address book.
 */
public class ClearCommand extends Command {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Address book has been cleared!";
    public static final String MESSAGE_UNDO_SUCCESS = "Restored address book to previous state.";
    public static final String MESSAGE_UNDO_FAILED = "No previous state to restore.";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  clear — Clears all entries from the address book.",
            "",
            "USAGE",
            "  clear",
            "",
            "DESCRIPTION",
            "  • Removes every person currently stored in the address book.",
            "  • This action can now be undone using the 'undo' command.",
            "",
            "EXAMPLES",
            "  clear",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#clearing-all-entries--clear"
    );

    // to keep the current status of
    private ReadOnlyAddressBook currentAddressBook;

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        // Store a copy of current address book for undo functionality
        currentAddressBook = new AddressBook(model.getAddressBook().getPersonList());

        // Clear the address book
        model.setAddressBook(new AddressBook());
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public String man() {
        return MANUAL;
    }

    public boolean isMutable() {
        return true;
    }

    @Override
    public String undo(Model model) {
        requireNonNull(model);
        if (currentAddressBook != null) {
            model.setAddressBook(currentAddressBook);
            return MESSAGE_UNDO_SUCCESS;
        } else {
            throw new IllegalStateException(MESSAGE_UNDO_FAILED);
        }
    }
}
