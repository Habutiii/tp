package seedu.address.model.person;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import seedu.address.model.tag.Tag;

/**
 * Tests whether a {@code Person} has <em>all</em> of a specified set of {@code Tag}s.
 * This predicate returns {@code true} only if every tag in the {@code required} set
 * is present in the person's {@code getTags()} collection.
 * Typical use: filtering the person list to show only those who match multiple tags,
 * such as in a {@code list t/friends t/colleagues} command.
 */
public class TagMatchesAllPredicate implements Predicate<Person> {
    private final Set<Tag> required;

    public TagMatchesAllPredicate(Set<Tag> required) {
        this.required = Objects.requireNonNull(required);
    }

    @Override
    public boolean test(Person person) {
        // Person must contain ALL required tags
        return person.getTags().containsAll(required);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof TagMatchesAllPredicate
                && required.equals(((TagMatchesAllPredicate) other).required));
    }

    @Override
    public String toString() {
        return "has ALL tags: " + required;
    }
}
