package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all persons, or only those with the given tag.\n"
            + "Examples:\n"
            + "  list\n"
            + "  list t/policyholder"
            + "  list t/friends t/colleagues s/";
    public static final String MESSAGE_SUCCESS = "Listed all persons";
    public static final String MESSAGE_FOLDER_EXISTS = "Folder \"%s\" already exists";
    public static final String MESSAGE_FOLDER_CREATED = "Created folder \"%s\" and listed matching persons";
    public static final String MESSAGE_LISTED_MATCHING = "Listed persons with all of: %s";
    public static final String MESSAGE_FOLDER_DELETED = "Deleted folder \"%s\"";
    public static final String MESSAGE_FOLDER_MISSING = "Folder \"%s\" not found. You cannot delete a folder "
            + "that does not exist";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  list — Lists all persons in the address book.",
            "  t/   — Syntax for tag",
            "  s/   — To create and save a custom folder",
            "  d/   — To delete a folder",
            "",
            "USAGE",
            "  list",
            "  list t/TAG",
            "  list t/TAG[t/TAG...] [s/]",
            "  list t/TAG[t/TAG...] [d/]",
            "",
            "EXAMPLES",
            "  list",
            "  list t/TAG",
            "  list t/friends /colleagues s/",
            "  list t/friends /colleagues d/",
            "",
            "Please note the output of save folder function will be the combination of you selected tags",
            " Example: ",
            " Input: list t/friends t/travel s/",
            " Output: Folder 'friends & travel' created",
            "As for the delete folder function, respective folder will be deleted as long as tags are selected",
            " Input: list t/friends t/travel d/ OR ;ist t/travel t/friends d/",
            " Output: Folder 'friends & travel' has been deleted",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#listing-all-persons--list"
    );

    private final Predicate<Person> predicate; // null => list all
    private final List<String> tagNamesForSidebar;
    private final boolean saveFolder;
    private final boolean deleteFolder;

    private String folderNameForUndo;
    private seedu.address.model.tag.TagFolder deletedSnapshot;

    /** List all. */
    public ListCommand() {
        this.predicate = null;
        this.tagNamesForSidebar = Collections.emptyList();
        this.saveFolder = false;
        this.deleteFolder = false;
    }

    /** Old ctor (no sidebar updates). */
    public ListCommand(Predicate<Person> predicate) {
        this.predicate = predicate;
        this.tagNamesForSidebar = Collections.emptyList();
        this.saveFolder = false;
        this.deleteFolder = false;
    }

    /** Predicate + tags + whether to save/delete a folder. */
    public ListCommand(Predicate<Person> predicate, List<String> tagNames,
                       boolean saveFolder, boolean deleteFolder) {
        this.predicate = predicate;
        this.tagNamesForSidebar = (tagNames == null) ? Collections.emptyList() : tagNames;
        this.saveFolder = saveFolder;
        this.deleteFolder = deleteFolder;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (predicate == null) {
            model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(MESSAGE_SUCCESS);
        }

        // filter list
        model.updateFilteredPersonList(predicate);

        // ---------- DELETE FLOW ----------
        if (deleteFolder) {
            final String folderName = deriveFolderName(tagNamesForSidebar);
            folderNameForUndo = folderName;

            if (!model.hasTagFolder(folderName)) {
                throw new CommandException(String.format(MESSAGE_FOLDER_MISSING, folderName));
            }

            // Take a snapshot of the folder we're going to remove so we can restore it on undo
            deletedSnapshot = model.getActiveTagFolders().stream()
                    .filter(f -> f.getName().equalsIgnoreCase(folderName))
                    .findFirst()
                    .map(f -> new seedu.address.model.tag.TagFolder(
                            f.getName(), f.getCount(), f.getQueryTags()))
                    .orElse(null);

            boolean removed = model.removeTagFolderByName(folderName);
            if (!removed) {
                throw new CommandException(String.format(MESSAGE_FOLDER_MISSING, folderName));
            }

            return new CommandResult(String.format(MESSAGE_FOLDER_DELETED, folderName));
        }

        // ---------- SAVE FLOW ----------
        if (saveFolder && !tagNamesForSidebar.isEmpty()) {
            final String folderName = deriveFolderName(tagNamesForSidebar);
            folderNameForUndo = folderName;

            // Guard against duplicates
            if (model.hasTagFolder(folderName)) {
                throw new CommandException(String.format(MESSAGE_FOLDER_EXISTS, folderName));
            }

            // Create the folder using your existing API
            if (tagNamesForSidebar.size() == 1) {
                model.addActiveTagFoldersFromUser(tagNamesForSidebar);
            } else {
                model.addCompositeTagFolderFromUser(tagNamesForSidebar);
            }

            return new CommandResult(String.format(MESSAGE_FOLDER_CREATED, folderName));
        }

        // No save requested → just report what we listed
        return new CommandResult(String.format(
                MESSAGE_LISTED_MATCHING, String.join(", ", tagNamesForSidebar)));
    }

    /** Build a stable folder name like "family & friends" independent of tag order. */
    private static String deriveFolderName(List<String> tags) {
        // normalize: trim, lowercase, de-duplicate, sort
        java.util.List<String> norm = tags.stream()
                .map(s -> s == null ? "" : s.trim().toLowerCase())
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted()
                .toList();

        return String.join(" & ", norm);
    }

    @Override
    public String man() {
        return MANUAL;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof ListCommand
                && ((this.predicate == null && ((ListCommand) other).predicate == null)
                || (this.predicate != null && this.predicate.equals(((ListCommand) other).predicate))));
    }

    @Override
    public boolean isMutable() {
        return saveFolder || deleteFolder;
    }

    @Override
    public String undo(Model model) throws UnsupportedOperationException {
        requireNonNull(model);

        if (!isMutable()) {
            return "Nothing to undo.";
        }

        // Undo a SAVE → remove the folder we just created
        if (saveFolder) {
            if (folderNameForUndo == null) {
                return "Nothing to undo.";
            }
            boolean removed = model.removeTagFolderByName(folderNameForUndo);
            model.refreshActiveTagFolderCounts();
            return removed
                    ? "Undid: removed folder \"" + folderNameForUndo + "\""
                    : "Undo had nothing to remove for \"" + folderNameForUndo + "\"";
        }

        // Undo a DELETE → restore the deleted folder from its snapshot
        if (deleteFolder) {
            if (deletedSnapshot == null || deletedSnapshot.getQueryTags().isEmpty()) {
                return "Nothing to undo.";
            }
            var tags = deletedSnapshot.getQueryTags();
            if (tags.size() == 1) {
                model.addActiveTagFoldersFromUser(tags);
            } else {
                model.addCompositeTagFolderFromUser(tags);
            }
            model.refreshActiveTagFolderCounts();
            return "Undid: restored folder \"" + deletedSnapshot.getName() + "\"";
        }

        return "Nothing to undo.";
    }
}
