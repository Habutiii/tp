package seedu.address.ui;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import seedu.address.model.tag.TagFolder;

/**
 * UI controller for a single Tag Folder row in the sidebar.
 * Binds a {@link TagFolder} to {@code TagFolderCard.fxml}.
 */
public class TagFolderCard {

    @FXML
    private HBox root;

    @FXML
    private Label nameLabel;

    @FXML
    private Label countBadge;

    /**
     * Populates this card with the given folder's data.
     */
    private TagFolder bound; // remember to unbind if reused

    public void setFolder(TagFolder folder) {
        if (bound != null) {
            countBadge.textProperty().unbind();
        }
        bound = folder;

        nameLabel.setText(folder.getName());
        countBadge.textProperty().unbind();
        countBadge.textProperty().bind(folder.countProperty().asString());
    }

    public HBox getRoot() {
        return root;
    }
}
