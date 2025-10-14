package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Address Book as requested ...";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  exit — Exits the application.",
            "",
            "USAGE",
            "  exit",
            "",
            "DESCRIPTION",
            "  Closes the Address App window and terminates the program safely.",
            "  Any unsaved changes are automatically written to storage before exiting.",
            "",
            "EXAMPLES",
            "  exit",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#exiting-the-program--exit"
    );

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
    }

    @Override
    public String man() {
        return MANUAL;
    }

}
