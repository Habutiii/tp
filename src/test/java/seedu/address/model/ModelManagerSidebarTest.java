package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.model.person.Person;
import seedu.address.model.tag.FeatureTag;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

class ModelManagerSidebarTest {
    private Person p(String name, String... tagNames) {
        Set<Tag> tags = new LinkedHashSet<>();
        for (String t : tagNames) {
            tags.add(new Tag(t));
        }
        // note: if your PersonBuilder expects Set<Tag>, not String, use .withTags(tags)
        return new PersonBuilder().withName(name).withTags(String.valueOf(tags)).build();
    }

    private AddressBook ab(Person... people) {
        AddressBook ab = new AddressBook();
        for (Person p : people) {
            ab.addPerson(p);
        }
        return ab;
    }


    @Test
    void bizFeature_map_addRemoveAndCopy() {
        ModelManager mm = new ModelManager(new AddressBook(), new UserPrefs());
        FeatureTag feature = new FeatureTag("biz");
        Set<Tag> tags = Set.of(new Tag("t1"), new Tag("t2"));

        assertFalse(mm.isBizFeature(feature));
        mm.addBizTags(feature, tags);
        assertTrue(mm.isBizFeature(feature));

        // deep copy semantics
        Map<FeatureTag, Set<Tag>> copy = mm.getBizTags();
        assertNotSame(copy, mm.getBizTags());
        assertEquals(copy.get(feature), mm.getBizTags().get(feature));

        mm.removeBizFeature(feature);
        assertFalse(mm.isBizFeature(feature));
    }

    @Test
    void userPrefs_pathsAndGuiSettings_roundTrip() {
        ModelManager mm = new ModelManager(new AddressBook(), new UserPrefs());
        Path p = Paths.get("ab.json");
        mm.setAddressBookFilePath(p);
        assertEquals(p, mm.getAddressBookFilePath());

        GuiSettings gs = new GuiSettings(800, 600, 10, 20);
        mm.setGuiSettings(gs);
        assertEquals(gs, mm.getGuiSettings());
    }

    @Test
    void undoRedoStacks_onlyMutablePushedAndPopped() {
        ModelManager mm = new ModelManager(new AddressBook(), new UserPrefs());
        Command immutable = new Command() {
            @Override public CommandResult execute(Model model) {
                return null;
            }

            @Override
            public String man() {
                return "";
            }

            @Override public boolean isMutable() {
                return false;
            }
        };
        Command mutable = new Command() {
            @Override public CommandResult execute(Model model) {
                return null;
            }

            @Override
            public String man() {
                return "";
            }

            @Override public boolean isMutable() {
                return true;
            }
        };

        mm.pushMutableCommandHistory(immutable); // should be ignored
        mm.pushMutableCommandHistory(mutable); // pushed to undo
        assertTrue(mm.popLastMutableCommand().isPresent());
        assertTrue(mm.popLastMutableCommand().isEmpty()); // stack empty

        mm.pushUndoCommandHistory(mutable); // goes to redo
        assertTrue(mm.popLastUndoCommand().isPresent());
        assertTrue(mm.popLastUndoCommand().isEmpty());
    }
}
