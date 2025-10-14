package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import seedu.address.model.Model;

/**
 * Shows manual for a command.
 */
public class ManCommand extends Command {

    public static final String COMMAND_WORD = "man";

    public static final String MESSAGE_USAGE = String.join("\n",
            "Usage:",
            "  man                Shows a list of all available commands.",
            "  man <command>      Shows detailed manual for the command.",
            "Examples:",
            "  man",
            "  man add",
            "  man find"
    );

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  man — Show inline manuals for commands.",
            "",
            "USAGE",
            "  man                # show an index of commands",
            "  man <command>      # show manual for the command",
            "",
            "EXAMPLES",
            "  man",
            "  man add",
            "  man find",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#viewing-manuals--man"
    );

    private final String target; // empty string means "no target"

    /**
     * Creates a ManCommand to show manual for the specified command.
     */
    public ManCommand(String target) {
        requireNonNull(target);
        this.target = target.trim().toLowerCase();
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Map<String, String> manuals = manualIndex();

        // Case 1: no command word provided -> show index
        if (target.isEmpty()) {
            StringBuilder sb = new StringBuilder("Manual index (use `man <command>` for details):\n\n");
            manuals.forEach((cmd, man) -> {
                String firstLine = man.lines()
                        .filter(l -> l.startsWith("  ")) // find synopsis line
                        .findFirst()
                        .orElse("  " + cmd + " — no description available");
                sb.append(firstLine.trim()).append("\n");
            });
            return new CommandResult(sb.toString());
        }

        // Case 2: specific command manual
        String man = manuals.get(target);
        return new CommandResult(Objects.requireNonNullElseGet(man, () -> "Unknown command: '" + target + "'.\n"
                + "Run `man` to see the list of available commands."));

    }

    @Override
    public String man() {
        return MANUAL;
    }

    /**
     * Central index of command → manual text.
     *
     * @return a map of command words to their manual text.
     */
    private static Map<String, String> manualIndex() {
        Map<String, String> m = new LinkedHashMap<>();
        m.put(ManCommand.COMMAND_WORD, ManCommand.MANUAL);
        m.put(HelpCommand.COMMAND_WORD, HelpCommand.MANUAL);
        m.put(AddCommand.COMMAND_WORD, AddCommand.MANUAL);
        m.put(EditCommand.COMMAND_WORD, EditCommand.MANUAL);
        m.put(DeleteCommand.COMMAND_WORD, DeleteCommand.MANUAL);
        m.put(ListCommand.COMMAND_WORD, ListCommand.MANUAL);
        m.put(FindCommand.COMMAND_WORD, FindCommand.MANUAL);
        m.put(ClearCommand.COMMAND_WORD, ClearCommand.MANUAL);
        m.put(StatsCommand.COMMAND_WORD, StatsCommand.MANUAL);
        m.put(RedoCommand.COMMAND_WORD, RedoCommand.MANUAL);
        m.put(UndoCommand.COMMAND_WORD, UndoCommand.MANUAL);
        m.put(ExitCommand.COMMAND_WORD, ExitCommand.MANUAL);
        return m;
    }
}
