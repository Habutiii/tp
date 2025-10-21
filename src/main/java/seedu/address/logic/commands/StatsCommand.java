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
            "  • Breakdown of 'Number of people' by predefined Fields and their Categories:",
            "  \t•{Gender : Male, Female, Other}",
            "  \t•{Plan : A, B, C}",
            "  • User will be able to define their own Fields and Categories in the future.",
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
                new FilteredList<>(model.getPersonListCopy());
        filteredPersons.setPredicate(PREDICATE_SHOW_ALL_PERSONS);
        this.initBusinessTags();
        ArrayList<String> tables = new ArrayList<>();

        String barOutput = "==================================";
        String barTable = "------------------------------------------------";

        for (String category : BUSINESS_TAGS.keySet()) {
            tables.add(this.getFieldStats(filteredPersons, category));
            tables.add(barTable);
            tables.add("\n");
        }

        String summaryTables = String.join("\n", tables);
        String overview = "Total Number of Customers in AddressBook: " + model.getSize() + "\n\n" + barOutput;

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

        int catTotal = 0;
        int catCount = 0;
        int catMax = 0;
        int catMin = filteredPersons.size();

        StringBuilder catMaxTag = new StringBuilder();
        StringBuilder catMinTag = new StringBuilder();

        for (String tag : tags) {
            Set<Tag> set = new LinkedHashSet<>();
            set.add(new Tag(tag));
            requireNonNull(filteredPersons);
            filteredPersons.setPredicate(new TagMatchesAllPredicate(set));
            int total = filteredPersons.size();
            catTotal += total;
            catCount++;

            if (total == catMax) {
                catMaxTag.append(catMaxTag.isEmpty() ? tag : " & " + tag);
            }

            else if (total > catMax) {
                catMax = total;
                catMaxTag = new StringBuilder(tag);
            }

            else if (total == catMin) {
                catMinTag.append(catMinTag.isEmpty() ? tag : " & " + tag);
            }

            else if (total < catMin) {
                catMin = total;
                catMinTag = new StringBuilder(tag);
            }

            String stat = String.format("%-" + padding + "s |  %d", tag, total);
            results.add(stat);
        }

        float mean = catCount > 0 ? (float) catTotal / catCount : -1;

        String catSummary = String.join("\n",
                "\nTotal for Feature: " + catTotal,
                "Average: " + (mean >= 0 ? String.format("%.2f", mean) : "N/A"),
                "Max Tag: " + catMaxTag + " (" + catMax + String.format(" %s)", catMax != 1 ? "people" : "person"),
                "Min Tag: " + catMinTag + " (" + catMin + String.format(" %s)", catMin != 1 ? "people" : "person")
        );

        results.add(String.join("\n", catSummary));

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
