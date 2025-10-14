package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Redoes the last undone command, if it is mutable.
 */
public class RedoCommand extends Command {
    public static final String COMMAND_WORD = "redo";
    public static final String MESSAGE_EMPTY = "No commands to redo.";
    public static final String MESSAGE_NON_MUTABLE = "Last command is not mutable, cannot redo.";
    public static final String MESSAGE_SUCCESS = "Redo successful.\n%1$s";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  redo — Reapplies the most recently undone mutable action.",
            "",
            "USAGE",
            "  redo",
            "",
            "DESCRIPTION",
            "  Reapplies the last undone command from the undo history.",
            "  Only actions that were previously undone using 'undo' can be redone.",
            "  Performing a new mutable action clears the redo history.",
            "",
            "PARAMETERS",
            "  This command does not take any parameters.",
            "",
            "EXAMPLES",
            "  redo",
            "    Reapplies the most recently undone add, delete, edit, or clear command.",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#redoing-the-last-undone-action--redo"
    );


    /**
     * Executes the redo command, which re-executes the last undone command if it is mutable.
     * This method interacts with the model to retrieve and execute the last command from the undo stack.
     *
     * @param model {@code Model} which the command should operate on.
     * @return
     * @throws CommandException
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // get the last command from the undo stack and execute its undo method
        Optional<Command> lastCommand = model.popLastUndoCommand();

        if (lastCommand.isPresent()) {
            Command command = lastCommand.get();
            // Check if the command is mutable before redoing
            if (command.isMutable()) {
                CommandResult result = command.execute(model);
                // After redoing, add the command back to the undo stack
                model.pushMutableCommandHistory(command);
                return new CommandResult(String.format(MESSAGE_SUCCESS, result.getFeedbackToUser()));
            }

            throw new CommandException(MESSAGE_NON_MUTABLE);
        }

        throw new CommandException(MESSAGE_EMPTY);
    }

    @Override
    public String man() {
        return MANUAL;
    }
}
