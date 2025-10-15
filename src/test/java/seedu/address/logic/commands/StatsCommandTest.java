package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains unit tests for StatsCommand.
 */
public class StatsCommandTest {
    @Test
    public void execute_stats_success() {
        StatsCommand statsCommand = new StatsCommand();
        String actualResult = "Total Number of Customers: 10"
                + "\n\n"
                + "Gender   |  Number of people\n"
                + "Male     |  3\n"
                + "Female   |  3\n"
                + "Other    |  3\n"
                + "\n\n"
                + "Plan   |  Number of people\n"
                + "A      |  3\n"
                + "B      |  3\n"
                + "C      |  3\n\n";
        assertEquals(new CommandResult(actualResult),
                statsCommand.execute(new ModelStub()));

    }

    @Test
    public void man_returnsManualString() {
        StatsCommand cmd = new StatsCommand();
        String manual = cmd.man();
        assertTrue(manual.contains("stats"));
        assertTrue(manual.contains("SEE MORE"));
    }


    /**
     * A Stub for Model class for unit testing of StatsCommand.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {

        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return null;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {

        }

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {

        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {

        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return null;
        }

        @Override
        public boolean hasPerson(Person person) {
            return false;
        }

        @Override
        public void deletePerson(Person target) {

        }

        @Override
        public void addPerson(Person person) {

        }

        @Override
        public void insertPerson(Index index, Person person) {
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {

        }

        @Override
        public int getSize() {
            return 10;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return new PersonListStub();
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {

        }

        @Override
        public void pushMutableCommandHistory(Command command) {
        }

        @Override
        public Optional<Command> popLastMutableCommand() {
            return Optional.empty();
        }

        @Override
        public void pushUndoCommandHistory(Command command) {
        }

        @Override
        public Optional<Command> popLastUndoCommand() {
            return Optional.empty();
        }
    }

    /**
     * Stub for ObservableList<Person> Object in ModelStub for unit testing.
     */
    private class PersonListStub implements ObservableList<Person> {

        @Override
        public void addListener(ListChangeListener<? super Person> listener) {

        }

        @Override
        public void removeListener(ListChangeListener<? super Person> listener) {

        }

        @Override
        public boolean addAll(Person... elements) {
            return false;
        }

        @Override
        public boolean setAll(Person... elements) {
            return false;
        }

        @Override
        public boolean setAll(Collection<? extends Person> col) {
            return false;
        }

        @Override
        public boolean removeAll(Person... elements) {
            return false;
        }

        @Override
        public boolean retainAll(Person... elements) {
            return false;
        }

        @Override
        public void remove(int from, int to) {

        }

        @Override
        public int size() {
            return 3;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Person> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Person person) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Person> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, Collection<? extends Person> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Person get(int index) {
            return null;
        }

        @Override
        public Person set(int index, Person element) {
            return null;
        }

        @Override
        public void add(int index, Person element) {

        }

        @Override
        public Person remove(int index) {
            return null;
        }

        @Override
        public int indexOf(Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(Object o) {
            return 0;
        }

        @Override
        public ListIterator<Person> listIterator() {
            return null;
        }

        @Override
        public ListIterator<Person> listIterator(int index) {
            return null;
        }

        @Override
        public List<Person> subList(int fromIndex, int toIndex) {
            return List.of();
        }

        @Override
        public void addListener(InvalidationListener listener) {

        }

        @Override
        public void removeListener(InvalidationListener listener) {

        }
    }
}

