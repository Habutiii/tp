package seedu.address.model.person;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests whether a {@code Person}'s fields (name, phone, email)
 * contain ANY of the given keywords. Matching is case-insensitive and allows partial matches.
 *
 * Defensive behaviors:
 * 1) Ignores blank keywords.
 * 2) Null-safe field access.
 */
public final class ClientMatchesPredicate implements Predicate<Person> {
    private final List<String> keywords;

    /**
     * @param keywords list of search keywords (non-null). Caller ensures tokens are trimmed/lowercased.
     */
    public ClientMatchesPredicate(List<String> keywords) {
        this.keywords = Objects.requireNonNull(keywords);
    }

    @Override
    public boolean test(Person person) {
        Objects.requireNonNull(person);

        if (keywords.isEmpty()) {
            return false;
        }

        final String name = safeLower(person.getName() == null ? null : person.getName().toString());
        final String phone = safeLower(person.getPhone() == null ? null : person.getPhone().toString());
        final String email = safeLower(person.getEmail() == null ? null : person.getEmail().toString());

        for (String kw : keywords) {
            if (kw.isBlank()) {
                continue;
            }
            final String k = kw.toLowerCase();
            if (contains(name, k) || contains(phone, k) || contains(email, k)) {
                return true;
            }
        }
        return false;
    }

    private static boolean contains(String field, String kw) {
        return field != null && !kw.isEmpty() && field.contains(kw);
    }

    private static String safeLower(String s) {
        return s == null ? null : s.toLowerCase();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClientMatchesPredicate otherClientMatchesPredicate)) {
            return false;
        }

        return keywords.equals(otherClientMatchesPredicate.keywords);
    }

    @Override
    public int hashCode() {
        return keywords.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
