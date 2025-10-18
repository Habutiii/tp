package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
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
        model.addBizTags(new Tag("Plan"), plans);
        Set<Tag> genders = new HashSet<>();
        genders.add(new Tag("Male"));
        genders.add(new Tag("Female"));
        genders.add(new Tag("Other"));
        model.addBizTags(new Tag("Gender"), genders);
    }
    @Test
    public void execute_statsIntegration_success() {
        StatsCommand statsCommand = new StatsCommand();
        String actualResult = "Total Number of Customers: 3"
                + "\n\n"
                + "Gender   |  Number of people\n"
                + "Other    |  0\n"
                + "Female   |  1\n"
                + "Male     |  1\n"
                + "\n\n"
                + "Plan   |  Number of people\n"
                + "A      |  2\n"
                + "B      |  0\n"
                + "C      |  0\n\n";
        assertEquals(new CommandResult(actualResult),
                statsCommand.execute(model));
    }
}
