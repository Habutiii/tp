package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Presents Statistics on Customers in {@code AddressBook}.
 */
public class StatsCommand extends Command {
    public static final String COMMAND_WORD = "stats";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  stats — Displays summary statistics of the address book.",
            "",
            "USAGE",
            "  stats",
            "",
            "DESCRIPTION",
            "  Shows an overview of the current data in the address book, such as:",
            "  • Total number of persons stored.",
            "  • Breakdown by tags (if applicable).",
            "  • Other relevant statistics implemented in your version.",
            "",
            "EXAMPLES",
            "  stats",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#viewing-summary-statistics-stats"
    );

    private static final String MESSAGE_USAGE = COMMAND_WORD;

    private static final String MESSAGE_SUCCESS = "%1$s";

    private String computeStats(Model model) {
        return "Number of Customers: " + model.getSize();
    }

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(this.computeStats(model));
    }

    @Override
    public String man() {
        return MANUAL;
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof StatsCommand);
    }
}
