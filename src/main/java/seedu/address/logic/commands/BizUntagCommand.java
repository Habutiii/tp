package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FIELD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class BizUntagCommand extends Command {
    public static final String COMMAND_WORD = "unbiz";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undeclares Fields, and their Tags as Categories from Statistics.\n"
            + "Parameters: "
            + "[" + PREFIX_FIELD + "FIELD]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_FIELD + "Plan "
            + PREFIX_FIELD + "Gender";
    @Override
    public CommandResult execute(Model model) throws CommandException {
        return null;
    }

    @Override
    public String man() {
        return "";
    }
}
