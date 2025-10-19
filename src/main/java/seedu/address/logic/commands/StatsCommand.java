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

    private static final String MESSAGE_USAGE = COMMAND_WORD;

    private static final String MESSAGE_SUCCESS = "%1$s";

    private String computeStats(Model model) {
        requireNonNull(model);
        HashMap<FeatureTag, Set<Tag>> bizTags = model.getBizTags();

        FilteredList<Person> filteredPersons =
                new FilteredList<>(model.getPersonListCopy());
        filteredPersons.setPredicate(PREDICATE_SHOW_ALL_PERSONS);

        ArrayList<String> tables = new ArrayList<>();

        for (Tag category : bizTags.keySet()) {
            tables.add(this.getFieldStats(filteredPersons, bizTags, category));
            tables.add("\n");
        }

        String summaryTables = String.join("\n", tables);
        String overview = "Total Number of Customers: " + model.getSize();

        return String.join("\n\n", overview, summaryTables);
    }

    private String getFieldStats(
            FilteredList<Person> filteredPersons, HashMap<FeatureTag, Set<Tag>> bizTags, Tag category) {
        Set<Tag> tags = bizTags.get(category);
        ArrayList<String> results = new ArrayList<>();

        // Find the longest tag for proper alignment
        int maxTagLength = 0;
        for (Tag tag : tags) {
            maxTagLength = Math.max(maxTagLength, tag.toString().length());
        }
        maxTagLength = Math.max(maxTagLength, category.toString().length());

        int padding = maxTagLength + 2;

        results.add(String.format("%-" + padding + "s |  Number of people", category));

        for (Tag tag : tags) {
            Set<Tag> set = new LinkedHashSet<>();
            set.add(tag);
            requireNonNull(filteredPersons);
            filteredPersons.setPredicate(new TagMatchesAllPredicate(set));
            int total = filteredPersons.size();
            String stat = String.format("%-" + padding + "s |  %d", tag, total);
            results.add(stat);
        }
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
