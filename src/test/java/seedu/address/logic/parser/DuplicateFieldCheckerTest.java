package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class DuplicateFieldCheckerTest {

    @Test
    public void isDuplicateField_noDuplicates_returnsFalse() {
        ArgumentMultimap argMultimap = new ArgumentMultimap();
        Prefix prefix = new Prefix("n/");
        argMultimap.put(prefix, "Alice");
        assertFalse(DuplicateFieldChecker.isDuplicateField(argMultimap, prefix));
    }

    @Test
    public void isDuplicateField_oneDuplicate_returnsTrue() {
        ArgumentMultimap argMultimap = new ArgumentMultimap();
        Prefix prefix = new Prefix("n/");
        argMultimap.put(prefix, "Alice");
        argMultimap.put(prefix, "Bob");
        assertTrue(DuplicateFieldChecker.isDuplicateField(argMultimap, prefix));
    }

    @Test
    public void isDuplicateField_multipleDuplicates_returnsTrue() {
        ArgumentMultimap argMultimap = new ArgumentMultimap();
        Prefix prefix = new Prefix("n/");
        argMultimap.put(prefix, "Alice");
        argMultimap.put(prefix, "Bob");
        argMultimap.put(prefix, "Charlie");
        assertTrue(DuplicateFieldChecker.isDuplicateField(argMultimap, prefix));
    }

    @Test
    public void isDuplicateField_emptyValues_returnsFalse() {
        ArgumentMultimap argMultimap = new ArgumentMultimap();
        Prefix prefix = new Prefix("n/");
        assertFalse(DuplicateFieldChecker.isDuplicateField(argMultimap, prefix));
    }

    @Test
    public void constructor_canInstantiate() {
        DuplicateFieldChecker checker = new DuplicateFieldChecker();
        assertNotNull(checker);
    }
}
