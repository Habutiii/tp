package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javafx.collections.transformation.FilteredList;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagMatchesAllPredicate;
import seedu.address.model.tag.Tag;

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

    private static final HashMap<String, String[]> BUSINESS_TAGS = new HashMap<>();

    private static final String MESSAGE_USAGE = COMMAND_WORD;

    private static final String MESSAGE_SUCCESS = "%1$s";

    private String computeStats(Model model) {
        requireNonNull(model);
        FilteredList<Person> filteredPersons =
                new FilteredList<>(model.getPersonListCopy()).filtered(PREDICATE_SHOW_ALL_PERSONS);
        this.initBusinessTags();
        ArrayList<String> tables = new ArrayList<>();

        for (String category : BUSINESS_TAGS.keySet()) {
            tables.add(this.getFieldStats(filteredPersons, category));
            tables.add("\n");
        }

        String summaryTables = String.join("\n", tables);
        String overview = "Total Number of Customers: " + model.getSize();

        return String.join("\n\n", overview, summaryTables);
    }

    private String getFieldStats(FilteredList<Person> filteredPersons, String category) {
        String[] tags = BUSINESS_TAGS.get(category);
        ArrayList<String> results = new ArrayList<>();

        // Find the longest tag for proper alignment
        int maxTagLength = 0;
        for (String tag : tags) {
            maxTagLength = Math.max(maxTagLength, tag.length());
        }
        maxTagLength = Math.max(maxTagLength, category.length());

        int padding = maxTagLength + 2;

        results.add(String.format("%-" + padding + "s |  Number of people", category));

        for (String tag : tags) {
            Set<Tag> set = new LinkedHashSet<>();
            set.add(new Tag(tag));
            requireNonNull(filteredPersons);
            filteredPersons.setPredicate(new TagMatchesAllPredicate(set));
            int total = filteredPersons.size();
            String stat = String.format("%-" + padding + "s |  %d", tag, total);
            results.add(stat);
        }
        return String.join("\n", results);
    }

    private void initBusinessTags() {
        BUSINESS_TAGS.put("Plan", new String[]{"A", "B", "C"});
        BUSINESS_TAGS.put("Gender", new String[]{"Male", "Female", "Other"});
    }

    @Override
    public CommandResult execute(Model model) {
        //this.computeStats(model)
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
