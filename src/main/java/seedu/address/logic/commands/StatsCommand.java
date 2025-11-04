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
import seedu.address.model.tag.FeatureTag;
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
            "  • Breakdown of 'Number of people' by predefined Features and their Tags:",
            "  \t•{Gender : Male, Female, Other}",
            "  \t•{Plan : A, B, C}",
            "  • Average, Max Tag and Min Tag per Feature.",
            "  • User will be able to define their own Fields and Categories in the future.",
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
        requireNonNull(model);
        HashMap<FeatureTag, Set<Tag>> bizTags = model.getBizTags();

        FilteredList<Person> filteredPersons =
                new FilteredList<>(model.getPersonListCopy());
        filteredPersons.setPredicate(PREDICATE_SHOW_ALL_PERSONS);

        ArrayList<String> tables = new ArrayList<>();

        String barOutput = "==========";
        String barTable = "----------";

        for (FeatureTag category : bizTags.keySet()) {
            tables.add(this.getFeatureStats(filteredPersons, bizTags, category));
            tables.add(barTable);
            tables.add("\n");
        }

        String summaryTables = String.join("\n", tables);
        String overview = "Total Number of Customers in Address Book: " + model.getSize() + "\n\n" + barOutput;

        return String.join("\n\n", overview, summaryTables);
    }

    private String getFeatureStats(
            FilteredList<Person> filteredPersons, HashMap<FeatureTag, Set<Tag>> bizTags, FeatureTag feature) {
        Set<Tag> tags = bizTags.get(feature);
        assert tags != null;
        ArrayList<String> results = new ArrayList<>();

        // Find the longest tag for proper alignment
        int maxTagLength = 0;
        for (Tag tag : tags) {
            maxTagLength = Math.max(maxTagLength, tag.toString().length());
        }
        maxTagLength = Math.max(maxTagLength, feature.toString().length());

        int padding = maxTagLength;

        results.add(String.format("%-" + padding + "s | Number of people", feature));

        int catTotal = 0;
        int catCount = 0;
        int catMax = 0;
        int catMin = filteredPersons.size();

        StringBuilder catMaxTag = new StringBuilder();
        StringBuilder catMinTag = new StringBuilder();

        for (Tag tag : tags) {
            Set<Tag> set = new LinkedHashSet<>();
            set.add(tag);
            requireNonNull(filteredPersons);
            filteredPersons.setPredicate(new TagMatchesAllPredicate(set));
            int total = filteredPersons.size();
            catTotal += total;
            catCount++;

            if (total == catMax) {
                catMaxTag.append(catMaxTag.isEmpty() ? tag : ", " + tag);
            } else if (total > catMax) {
                catMax = total;
                catMaxTag = new StringBuilder(tag.toString());
            }

            if (total == catMin) {
                catMinTag.append(catMinTag.isEmpty() ? tag : ", " + tag);
            } else if (total < catMin) {
                catMin = total;
                catMinTag = new StringBuilder(tag.toString());
            }

            String stat = String.format("%-" + padding + "s | %d", tag, total);
            results.add(stat);
        }

        float mean = catCount > 0 ? (float) catTotal / catCount : -1;

        String perTag = " per tag)";
        String catSummary = String.join("\n",
                "\nTotal for Feature: " + catTotal,
                "Average: " + (mean == -1 ? "N/A" : String.format("%.2f", mean)),
                "Max Tag: " + catMaxTag + " (" + catMax + String.format(" %s", catMax != 1 ? "people" : "person")
                        + perTag,
                "Min Tag: " + catMinTag + " (" + catMin + String.format(" %s", catMin != 1 ? "people" : "person")
                        + perTag
        );

        results.add(String.join("\n", catSummary));

        return String.join("\n", results);
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
