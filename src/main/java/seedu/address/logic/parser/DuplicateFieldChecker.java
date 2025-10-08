// Utility class to check for duplicate fields in ArgumentMultimap
package seedu.address.logic.parser;

/**
 * Utility class to check for duplicate fields in ArgumentMultimap
 */
public class DuplicateFieldChecker {
    /**
     * Returns true if the given prefix appears more than once in the
     * ArgumentMultimap.
     */
    public static boolean isDuplicateField(ArgumentMultimap argMultimap, Prefix prefix) {
        return argMultimap.getAllValues(prefix).size() > 1;
    }
}
