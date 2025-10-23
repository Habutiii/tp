package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.tag.TagFolder;

public class TagFolderCardTest {

    private static void fxRun(Runnable r) throws Exception {
        CountDownLatch done = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                r.run();
            } finally {
                done.countDown();
            }
        });
        if (!done.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("FX action timed out");
        }
    }

    private static void setPrivate(Object target, String fieldName, Object value) throws Exception {
        Field f = TagFolderCard.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    private static <T> T getPrivate(Object target, String fieldName, Class<T> type) throws Exception {
        Field f = TagFolderCard.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        return type.cast(f.get(target));
    }

    @Test
    void setFolder_bindsAndRebindsCountandGetRootWorks() throws Exception {
        TagFolderCard card = new TagFolderCard();

        // Inject @FXML fields (we're not loading FXML here)
        HBox root = new HBox();
        Label nameLabel = new Label();
        Label countBadge = new Label();
        setPrivate(card, "root", root);
        setPrivate(card, "nameLabel", nameLabel);
        setPrivate(card, "countBadge", countBadge);

        // First folder: bind name + count
        TagFolder f1 = new TagFolder("friends", 3);
        fxRun(() -> card.setFolder(f1));

        assertEquals("friends", nameLabel.getText());
        assertEquals("3", countBadge.getText());

        // Update first folder (binding should update the label)
        fxRun(() -> f1.setCount(4));
        assertEquals("4", countBadge.getText());

        // Rebind to a different folder; old binding must be removed
        TagFolder f2 = new TagFolder("vip", 1);
        fxRun(() -> card.setFolder(f2));
        assertEquals("vip", nameLabel.getText());
        assertEquals("1", countBadge.getText());

        // Changing old folder should NOT affect the badge now
        fxRun(() -> f1.setCount(99));
        assertEquals("1", countBadge.getText());

        // Changing new folder should update the badge
        fxRun(() -> f2.setCount(7));
        assertEquals("7", countBadge.getText());

        // getRoot() returns injected root
        HBox gotRoot = getPrivate(card, "root", HBox.class);
        assertNotNull(gotRoot);
        assertEquals(root, gotRoot);
    }
}
