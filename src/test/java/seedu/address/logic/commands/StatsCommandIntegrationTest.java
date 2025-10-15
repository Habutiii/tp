package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
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
    }
    @Test
    public void execute_stats_success_integration() {
        StatsCommand statsCommand = new StatsCommand();
        String actualResult = "Total Number of Customers: 3"
                + "\n\n"
                + "Gender   |  Number of people\n"
                + "Male     |  1\n"
                + "Female   |  1\n"
                + "Other    |  0\n"
                + "\n\n"
                + "Plan   |  Number of people\n"
                + "A      |  2\n"
                + "B      |  0\n"
                + "C      |  0\n\n";
        assertEquals(new CommandResult(actualResult),
                statsCommand.execute(model));
    }
}
