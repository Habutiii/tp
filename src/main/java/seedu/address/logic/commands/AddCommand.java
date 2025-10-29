package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a person to the address book. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "owesMoney";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  add — Adds a person to the address book.",
            "",
            "USAGE",
            "  add n/NAME p/PHONE e/EMAIL a/ADDRESS [t/TAG]…",
            "",
            "PARAMETERS",
            "  • NAME: non-empty string; all leading and trailing whitespace is trimmed",
            "  • PHONE: must be 3 to 15 digits to account for special and international numbers; "
                    + "only digits (0–9) are allowed, no spaces, letters, or symbols",
            "  • EMAIL: must follow standard email format (must contain '@')",
            "  • ADDRESS: non-empty string",
            "  • TAG (optional): alphanumeric, no spaces; up to 15 tags per person",
            "",
            "EXAMPLES",
            "  add n/John Doe p/98765432 e/john@example.com a/123, Main Street t/friend t/colleague",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#adding-a-person-add"
    );

    public static final String MESSAGE_SUCCESS = "New person added:\n%1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book";
    public static final String MESSAGE_UNDO_SUCCESS = "Removed last added person:\n%1$s";
    public static final String MESSAGE_UNDO_FAILED = "No person was added";

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.addPerson(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public String man() {
        return MANUAL;
    }

    public boolean isMutable() {
        return true;
    }

    /**
     * Undoes the add command by deleting the person that was added.
     * @param model
     * @return String message indicating the person that was removed.
     */
    @Override
    public String undo(Model model) {
        requireNonNull(model);
        if (!model.hasPerson(toAdd)) {
            throw new IllegalStateException(MESSAGE_UNDO_FAILED);
        }
        model.deletePerson(toAdd);
        return String.format(MESSAGE_UNDO_SUCCESS, toAdd);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
