package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.model.person.Person;
import seedu.address.model.tag.FeatureTag;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagFolder;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final HashMap<FeatureTag, Set<Tag>> bizTags;

    // Stacks for undo and redo functionality
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    // Sidebar state
    private final ObservableList<TagFolder> activeFolders =
            FXCollections.observableArrayList();
    private final LinkedHashMap<String, Integer> folderIndex =
            new LinkedHashMap<>();

    // --- constructors must come before any methods ---
    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        this.bizTags = new HashMap<>();

        requireAllNonNull(addressBook, userPrefs);
        // call this at the end of ModelManager constructor after addressBook is set:
        bootstrapAllTags();
        loadUserSavedFoldersFromPrefs();
        refreshActiveTagFolderCounts();
        sortFolders();
    }

    // --- methods below constructors ---
    private static String folderKey(java.util.List<String> tags) {
        return tags.stream()
                .map(String::toLowerCase)
                .sorted()
                .collect(java.util.stream.Collectors.joining("|"));
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        activeFolders.clear();
        folderIndex.clear();
        bootstrapAllTags();
        refreshActiveTagFolderCounts();
        persistUserFoldersToPrefs();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        refreshActiveTagFolderCounts();
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        ensureFoldersExistForTags(person.getTags());
        refreshActiveTagFolderCounts();
    }

    @Override
    public void insertPerson(Index index, Person person) {
        requireAllNonNull(index, person);
        addressBook.insertPerson(index.getZeroBased(), person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        ensureFoldersExistForTags(person.getTags());
        refreshActiveTagFolderCounts();
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        addressBook.setPerson(target, editedPerson);
        ensureFoldersExistForTags(editedPerson.getTags());
        refreshActiveTagFolderCounts();
    }

    @Override
    public int getSize() {
        return addressBook.getSize();
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public ObservableList<Person> getPersonListCopy() {
        return addressBook.getPersonList();
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean isBizFeature(FeatureTag field) {
        return bizTags.containsKey(field);
    }

    @Override
    public void addBizTags(FeatureTag field, Set<Tag> tags) {
        this.bizTags.put(field, tags);
    }

    @Override
    public void removeBizFeature(FeatureTag field) {
        this.bizTags.remove(field);
    }

    @Override
    public HashMap<FeatureTag, Set<Tag>> getBizTags() {
        HashMap<FeatureTag, Set<Tag>> deepCopy = new HashMap<>();
        for (Map.Entry<FeatureTag, Set<Tag>> entry : bizTags.entrySet()) {
            deepCopy.put(entry.getKey(), new LinkedHashSet<>(entry.getValue()));
        }
        return deepCopy;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

    //=========== Undo/Redo ==================================================================================
    @Override
    public void pushMutableCommandHistory(Command command) {
        if (command.isMutable()) {
            undoStack.push(command);
            redoStack.clear(); // Clear redo stack on new command
        }
    }

    @Override
    public Optional<Command> popLastMutableCommand() {
        if (undoStack.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(undoStack.pop());
    }

    @Override
    public void pushUndoCommandHistory(Command command) {
        if (command.isMutable()) {
            redoStack.push(command);
        }
    }

    @Override
    public Optional<Command> popLastUndoCommand() {
        if (redoStack.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(redoStack.pop());
    }

    // =========== Tag Folders (sidebar) ==================================================

    @Override
    public ObservableList<TagFolder> getActiveTagFolders() {
        return FXCollections.unmodifiableObservableList(activeFolders);
    }

    private void copyTagFolderList(ObservableList<TagFolder> source,
                                      ObservableList<TagFolder> target) {
        target.clear();
        for (TagFolder f : source) {
            // copy query tags (shallow copy of String elements)
            List<String> q = new ArrayList<>(f.getQueryTags());

            TagFolder c;
            if (f.isUserCreated()) {
                // preserve user-created flag using user factory methods
                // single/composite based on query tag count
                c = (q.size() < 2)
                        ? TagFolder.userSingle(f.getName())
                        : TagFolder.userComposite(f.getName(), q);
            } else {
                // non-user folders: use constructors/factory for composite/single
                c = (q.size() < 2)
                        ? new TagFolder(f.getName(), f.getCount())
                        : TagFolder.composite(f.getName(), q);
            }

            // preserve the count
            c.setCount(f.getCount());
            target.add(c);
        }
    }

    @Override
    public ObservableList<TagFolder> getActiveTagFoldersCopy() {
        ObservableList<TagFolder> copy =
                FXCollections.observableArrayList();

        copyTagFolderList(activeFolders, copy);

        return FXCollections.unmodifiableObservableList(copy);
    }


    @Override
    public void setActiveTagFolders(ObservableList<TagFolder> newTagFolders) {
        activeFolders.clear();
        if (newTagFolders == null || newTagFolders.isEmpty()) {
            refreshActiveTagFolderCounts();
            sortFolders();
            persistUserFoldersToPrefs();
            return;
        }

        copyTagFolderList(newTagFolders, activeFolders);

        refreshActiveTagFolderCounts();
        sortFolders();
        persistUserFoldersToPrefs();
    }

    @Override
    public void addActiveTagFolders(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }
        boolean added = false;
        for (String raw : tagNames) {
            String display = raw == null ? "" : raw.trim();
            if (display.isEmpty()) {
                continue;
            }

            // Use display name to check duplicates
            if (hasTagFolder(display)) {
                continue;
            }

            // Start with count 0; we'll recompute counts below
            activeFolders.add(new TagFolder(display, 0)); // single-tag folder
            added = true;
        }

        if (added) {
            refreshActiveTagFolderCounts();
            sortFolders(); // rebuilds folderIndex keyed by display names
        }
    }

    @Override
    public void refreshActiveTagFolderCounts() {
        var people = getAddressBook().getPersonList();

        for (TagFolder tagFolder : activeFolders) {
            int count = (int) people.stream()
                    .filter(p -> tagFolder.getQueryTags().stream()
                            .allMatch(qt -> p.getTags().stream()
                                    .anyMatch(t -> t.tagName.equalsIgnoreCase(qt))))
                    .count();
            tagFolder.setCount(count);
        }

        // Remove folders with zero count
        activeFolders.removeIf(folder -> !folder.isUserCreated() && folder.getCount() == 0);
    }

    @Override
    public void addCompositeTagFolder(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

        // Normalise: trim, lower-case, distinct, sorted (stable display ordering)
        List<String> norm = tagNames.stream()
                .map(s -> s == null ? "" : s.trim().toLowerCase())
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted()
                .toList();

        if (norm.isEmpty()) {
            return;
        }

        // Display name like "friends & colleagues"
        String display = String.join(" & ", norm);

        if (hasTagFolder(display)) {
            // already saved; just refresh counts and return
            refreshActiveTagFolderCounts();
            return;
        }

        // TagFolder must carry query tags of a composite
        TagFolder folder = TagFolder.composite(display, norm);
        activeFolders.add(folder);

        refreshActiveTagFolderCounts();
        sortFolders(); // rebuild folderIndex keyed by display names
    }

    private void bootstrapAllTags() {
        java.util.Set<String> all = new java.util.HashSet<>();
        getAddressBook().getPersonList().forEach(p ->
                p.getTags().forEach(t -> all.add(t.tagName)));
        addActiveTagFolders(new java.util.ArrayList<>(all));
    }

    private void loadUserSavedFoldersFromPrefs() {
        var saved = userPrefs.getSavedSidebarFolders();
        for (var sf : saved) {
            List<String> tags = (sf.getQueryTags() == null)
                    ? java.util.List.<String>of()
                    : sf.getQueryTags();

            List<String> norm = tags.stream()
                    .map(s -> s == null ? "" : s.trim().toLowerCase())
                    .filter(s -> !s.isEmpty())
                    .distinct()
                    .sorted()
                    .toList();

            if (norm.isEmpty()) {
                continue;
            }

            String display = String.join(" & ", norm);

            if (display.isEmpty() || hasTagFolder(display)) {
                continue;
            }

            TagFolder folder = (norm.size() <= 1)
                    ? TagFolder.userSingle(display)
                    : TagFolder.userComposite(display, norm);

            activeFolders.add(folder);
        }
    }


    // Ensures every tag has a corresponding TagFolder.
    private void ensureFoldersExistForTags(java.util.Collection<? extends seedu.address.model.tag.Tag> tags) {
        if (tags == null) {
            return;
        }
        for (seedu.address.model.tag.Tag t : tags) {
            String key = t.tagName.toLowerCase();
            if (!folderIndex.containsKey(key)) {
                activeFolders.add(new TagFolder(t.tagName, 0));
                folderIndex.put(key, activeFolders.size() - 1);
            }
        }
        sortFolders();
    }

    private void sortFolders() {
        FXCollections.sort(activeFolders);
        folderIndex.clear();
        for (int i = 0; i < activeFolders.size(); i++) {
            folderIndex.put(activeFolders.get(i).getName().toLowerCase(), i);
        }
    }

    @Override
    public boolean hasTagFolder(String name) {
        if (name == null) {
            return false;
        }
        String target = name.trim().toLowerCase();
        for (TagFolder f : activeFolders) {
            if (f.getName().toLowerCase().equals(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addActiveTagFoldersFromUser(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

        boolean added = false;
        for (String raw : tagNames) {
            String display = raw == null ? "" : raw.trim();
            if (display.isEmpty()) {
                continue;
            }
            if (hasTagFolder(display)) {
                continue;
            }
            activeFolders.add(TagFolder.userSingle(display));
            added = true;
        }
        if (added) {
            refreshActiveTagFolderCounts();
            sortFolders();
            persistUserFoldersToPrefs();
        }
    }

    @Override
    public void addCompositeTagFolderFromUser(List<String> tagNames) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

        List<String> norm = tagNames.stream()
                .map(s -> s == null ? "" : s.trim().toLowerCase())
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted()
                .toList();
        if (norm.isEmpty()) {
            return;
        }

        String display = String.join(" & ", norm);
        if (hasTagFolder(display)) {
            refreshActiveTagFolderCounts();
            return;
        }

        activeFolders.add(TagFolder.userComposite(display, norm));
        refreshActiveTagFolderCounts();
        sortFolders();
        persistUserFoldersToPrefs();
    }

    @Override
    public boolean removeTagFolderByName(String name) {
        if (name == null) {
            return false;
        }
        String target = name.trim().toLowerCase();

        for (int i = 0; i < activeFolders.size(); i++) {
            if (activeFolders.get(i).getName().toLowerCase().equals(target)) {
                activeFolders.remove(i);
                sortFolders();
                persistUserFoldersToPrefs();
                return true;
            }
        }
        return false;
    }

    private void persistUserFoldersToPrefs() {
        var saved = activeFolders.stream()
                .filter(TagFolder::isUserCreated)
                .map(f -> new seedu.address.storage.SidebarFolderPrefs(f.getQueryTags()))
                .toList();
        userPrefs.setSavedSidebarFolders(saved);
    }
}
