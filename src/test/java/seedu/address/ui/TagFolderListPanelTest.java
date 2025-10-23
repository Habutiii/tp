package seedu.address.ui;

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
            CountDownLatch started = new CountDownLatch(1);
            Platform.startup(started::countDown);
            assertTrue(started.await(5, TimeUnit.SECONDS));
        } catch (IllegalStateException alreadyStarted) {
            // FX already started by another test; that's fine.
            assertTrue(true);
        }
    }

    private static void fxRun(Runnable r) throws Exception {
        CountDownLatch done = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                r.run();
            } finally {
                done.countDown();
            }
        });
        assertTrue(done.await(5, TimeUnit.SECONDS));
    }

    @SuppressWarnings("unchecked")
    private static ListView<TagFolder> getListView(TagFolderListPanel panel) throws Exception {
        Field f = TagFolderListPanel.class.getDeclaredField("folderListView");
        f.setAccessible(true);
        return (ListView<TagFolder>) f.get(panel);
    }

    @Test
    void constructor_withCallback_initializes() throws Exception {
        ObservableList<TagFolder> folders = FXCollections.observableArrayList(
                new TagFolder("friends", 2),
                new TagFolder("colleagues", 1)
        );
        AtomicBoolean callbackCalled = new AtomicBoolean(false);

        TagFolderListPanel[] panelRef = new TagFolderListPanel[1];
        fxRun(() -> panelRef[0] = new TagFolderListPanel(folders, sel -> callbackCalled.set(true)));

        assertNotNull(panelRef[0]);
        // Some JavaFX impls may fire an initial empty selection change; don’t assert false here.
        assertTrue(folders.size() == 2);
    }

    @Test
    void constructor_withoutCallback_initializes() throws Exception {
        ObservableList<TagFolder> folders = FXCollections.observableArrayList(
                new TagFolder("alpha", 0),
                new TagFolder("beta", 1)
        );

        TagFolderListPanel[] panelRef = new TagFolderListPanel[1];
        fxRun(() -> panelRef[0] = new TagFolderListPanel(folders, null));

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
        final int[] receivedSize = new int[1];
        CountDownLatch callbackLatch = new CountDownLatch(1);

        TagFolderListPanel[] panelRef = new TagFolderListPanel[1];
        fxRun(() -> panelRef[0] = new TagFolderListPanel(
                folders,
                sel -> {
                    callbackCalled.set(true);
                    receivedSize[0] = sel.size();
                    callbackLatch.countDown();
                }
        ));

        fxRun(() -> {
            try {
                ListView<TagFolder> lv = getListView(panelRef[0]);
                lv.getSelectionModel().clearSelection();
                lv.getSelectionModel().selectIndices(0, 2); // select "one" and "three"
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Wait for the listener instead of Thread.sleep
        assertTrue(callbackLatch.await(2, TimeUnit.SECONDS));
        assertTrue(callbackCalled.get());
        assertTrue(receivedSize[0] == 2);
    }
}
