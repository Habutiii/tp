package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class TagFolderTest {

    @Test
    void ctor_singleTag_setsNameCountAndQueryTags() {
        TagFolder f = new TagFolder("friends", 3);
        assertEquals("friends", f.getName());
        assertEquals(3, f.getCount());
        assertEquals(List.of("friends"), f.getQueryTags());
        assertEquals("friends (3)", f.toString());
    }

    @Test
    void composite_factory_usesDisplayNameAndQueryTags() {
        TagFolder f = TagFolder.composite("friends & colleagues", List.of("friends", "colleagues"));
        assertEquals("friends & colleagues", f.getName());
        assertEquals(0, f.getCount()); // factory starts at 0
        assertEquals(List.of("friends", "colleagues"), f.getQueryTags());
    }

    @Test
    void countProperty_setterUpdatesGetCount() {
        TagFolder f = new TagFolder("x", 0);
        f.setCount(7);
        assertEquals(7, f.getCount());
        assertNotNull(f.countProperty()); // property exists (for bindings in UI)
    }

    @Test
    void equals_isCaseInsensitiveOnName_only() {
        TagFolder a = new TagFolder("Friends", 1);
        TagFolder b = new TagFolder("friends", 99);
        TagFolder c = TagFolder.composite("friends & colleagues", List.of("friends", "colleagues"));
        assertTrue(a.equals(b));
        assertTrue(a.hashCode() == b.hashCode());
        assertNotSame(a, c); // different names → not equal
    }
}
