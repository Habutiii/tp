package seedu.address.model.person;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import seedu.address.model.tag.Tag;

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
