package seedu.address.logic.commands;

import seedu.address.model.Model;

public class StatsCommand extends Command {
    public static final String COMMAND_WORD = "stats";

    private static final String MESSAGE_USAGE = COMMAND_WORD;

    private static final String MESSAGE_SUCCESS = "%1$s";

    private String computeStats(Model model) {
        return "Number of Customers: " + model.getSize();
    }

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(this.computeStats(model));
    }
}
