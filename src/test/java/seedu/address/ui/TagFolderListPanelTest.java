package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import seedu.address.model.tag.TagFolder;

/**
 * Basic tests for TagFolderListPanel using only assertTrue/assertFalse/assertNotNull.
 */
public class TagFolderListPanelTest {

    @BeforeAll
    static void initFx() throws Exception {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(() -> {
                latch.countDown();
            });
            assertTrue(latch.await(5, TimeUnit.SECONDS));
        } catch (IllegalStateException alreadyStarted) {
            // JavaFX runtime was started by another test/class; that's fine.
            assertTrue(true);
        }
    }

    /**
     * Runs code on the JavaFX thread and waits for it to complete.
     */
    private static void fxRun(Runnable runnable) throws Exception {
        CountDownLatch done = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                runnable.run();
            } finally {
                done.countDown();
            }
        });
        assertTrue(done.await(5, TimeUnit.SECONDS));
    }

    /**
     * Reflectively gets the private folderListView field.
     */
    @SuppressWarnings("unchecked")
    private static ListView<TagFolder> getListView(TagFolderListPanel panel) throws Exception {
        Field field = TagFolderListPanel.class.getDeclaredField("folderListView");
        field.setAccessible(true);
        return (ListView<TagFolder>) field.get(panel);
    }

    @Test
    void constructor_withCallback_initializes() throws Exception {
        ObservableList<TagFolder> folders = FXCollections.observableArrayList(
                new TagFolder("friends", 2),
                new TagFolder("colleagues", 1)
        );
        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        TagFolderListPanel[] panelRef = new TagFolderListPanel[1];

        fxRun(() -> {
            panelRef[0] = new TagFolderListPanel(folders, sel -> callbackCalled.set(true));
        });

        assertNotNull(panelRef[0]);
        assertFalse(callbackCalled.get());
        assertTrue(folders.size() == 2);
    }

    @Test
    void constructor_withoutCallback_initializes() throws Exception {
        ObservableList<TagFolder> folders = FXCollections.observableArrayList(
                new TagFolder("alpha", 0),
                new TagFolder("beta", 1)
        );
        TagFolderListPanel[] panelRef = new TagFolderListPanel[1];

        fxRun(() -> {
            panelRef[0] = new TagFolderListPanel(folders, null);
        });

        assertNotNull(panelRef[0]);
        assertTrue(folders.size() == 2);
    }

    @Test
    void selection_triggersCallback_withSnapshot() throws Exception {
        ObservableList<TagFolder> folders = FXCollections.observableArrayList(
                new TagFolder("one", 1),
                new TagFolder("two", 2),
                new TagFolder("three", 3)
        );

        AtomicBoolean callbackCalled = new AtomicBoolean(false);
        int[] receivedSize = new int[1];
        TagFolderListPanel[] panelRef = new TagFolderListPanel[1];

        fxRun(() -> {
            panelRef[0] = new TagFolderListPanel(
                    folders,
                    sel -> {
                        callbackCalled.set(true);
                        receivedSize[0] = sel.size();
                    }
            );
        });

        fxRun(() -> {
            ListView<TagFolder> listView = null;
            try {
                listView = getListView(panelRef[0]);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            listView.getSelectionModel().clearSelection();
            listView.getSelectionModel().selectIndices(0, 2);
        });

        Thread.sleep(100); // let listener fire
        assertTrue(callbackCalled.get());
        assertTrue(receivedSize[0] == 2);
    }
}
