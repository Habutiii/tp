package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.tag.FeatureTag;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersons;

public class StatsCommandIntegrationTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(new AddressBook(), new UserPrefs());
        Person alice = new PersonBuilder(TypicalPersons.ALICE).withTags("A", "Female").build();
        Person benson = new PersonBuilder(TypicalPersons.BENSON).withTags("A", "Male").build();
        model.addPerson(alice);
        model.addPerson(benson);
        model.addPerson(TypicalPersons.CARL);
        Set<Tag> plans = new HashSet<>();
        plans.add(new Tag("A"));
        plans.add(new Tag("B"));
        plans.add(new Tag("C"));
        model.addBizTags(new FeatureTag("Plan"), plans);
        Set<Tag> genders = new HashSet<>();
        genders.add(new Tag("Male"));
        genders.add(new Tag("Female"));
        genders.add(new Tag("Other"));
        model.addBizTags(new FeatureTag("Gender"), genders);
    }
    @Test
    public void execute_statsIntegration_success() {
        StatsCommand statsCommand = new StatsCommand();
        String barOutput = "==================================";
        String barTable = "------------------------------------------------";
        String actualResult = "Total Number of Customers in AddressBook: 3"
                + "\n\n" + barOutput + "\n\n"
                + "Gender   |  Number of people\n"
                + "Male     |  1\n"
                + "Female   |  1\n"
                + "Other    |  0\n\n"
                + "Total for Feature: 2\n"
                + "Average: 0.67\n"
                + "Max Tag: Male & Female (1 person)\n"
                + "Min Tag: Other (0 people)\n"
                + barTable + "\n\n\n"
                + "Plan   |  Number of people\n"
                + "A      |  2\n"
                + "B      |  0\n"
                + "C      |  0\n\n"
                + "Total for Feature: 2\n"
                + "Average: 0.67\n"
                + "Max Tag: A (2 people)\n"
                + "Min Tag: B & C (0 people)\n"
                + barTable + "\n\n";
        assertEquals(new CommandResult(actualResult),
                statsCommand.execute(model));
    }
}
