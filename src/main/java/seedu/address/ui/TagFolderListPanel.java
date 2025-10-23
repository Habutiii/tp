package seedu.address.ui;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.tag.TagFolder;

/**
 * A UI component that displays a list of {@link TagFolder} objects in the sidebar.
 * Each tag folder represents a logical grouping of persons who share the same tag,
 * along with the count of persons in that group.
 */
public class TagFolderListPanel extends UiPart<Region> {
    private static final String FXML = "TagFolderListPanel.fxml";

    @FXML
    private ListView<TagFolder> folderListView;

    public TagFolderListPanel(ObservableList<TagFolder> folders) {
        this(folders, null);
    }

    /** Supplies a callback with ALL selected tag names. */
    public TagFolderListPanel(ObservableList<TagFolder> folders,
                              java.util.function.Consumer<java.util.List<String>> onSelectMany) {
        super(FXML);
        folderListView.setItems(folders);
        folderListView.setCellFactory(list -> new FolderCell());
        folderListView.getSelectionModel().setSelectionMode(javafx.scene.control.SelectionMode.MULTIPLE);

        if (onSelectMany != null) {
            folderListView.getSelectionModel().getSelectedItems().addListener(
                    (javafx.collections.ListChangeListener<TagFolder>) c -> {
                        var selected = folderListView.getSelectionModel().getSelectedItems()
                                .stream().map(TagFolder::getName).toList();
                        onSelectMany.accept(selected);
                    });
        }
    }

    /**
     * Custom {@link ListCell} for displaying each {@link TagFolder} in the sidebar list.
     */
    private static class FolderCell extends ListCell<TagFolder> {
        @Override
        protected void updateItem(TagFolder folder, boolean empty) {
            super.updateItem(folder, empty);
            if (empty || folder == null) {
                setGraphic(null);
                setText(null);
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader(
                        FolderCell.class.getResource("/view/TagFolderCard.fxml"));
                Node node = loader.load();
                TagFolderCard controller = loader.getController();
                controller.setFolder(folder);
                setGraphic(node);
                setText(null);
            } catch (IOException e) {
                setText(folder.getName() + " (" + folder.getCount() + ")");
                setGraphic(null);
            }
        }
    }
}
