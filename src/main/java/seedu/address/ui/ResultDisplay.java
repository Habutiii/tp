package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * A ui for the status bar that is displayed at the header of the application.
 */
public class ResultDisplay extends UiPart<Region> {

    private static final String FXML = "ResultDisplay.fxml";

    @FXML
    private TextArea resultDisplay;

    @FXML
    private ScrollPane livePreviewScrollPane;

    @FXML
    private TextFlow livePreviewDisplay;

    public ResultDisplay() {
        super(FXML);
    }

    public void setFeedbackToUser(String feedbackToUser) {
        requireNonNull(feedbackToUser);
        resultDisplay.setText(feedbackToUser);
        resultDisplay.setVisible(true);
        resultDisplay.setManaged(true);
        livePreviewScrollPane.setVisible(false);
        livePreviewScrollPane.setManaged(false);
    }

    /**
     * Method for handling live preview for add command.
     * A separate method required to handle changing of text color based on
     * validity.
     *
     * @param fieldPreviews The list of field previews to display
     */
    public void setLivePreviewFeedback(List<FieldPreview> fieldPreviews) {
        requireNonNull(fieldPreviews);
        livePreviewDisplay.getChildren().clear();

        // If empty list, hide live preview and show normal TextArea
        if (fieldPreviews.isEmpty()) {
            resultDisplay.setVisible(true);
            resultDisplay.setManaged(true);
            livePreviewScrollPane.setVisible(false);
            livePreviewScrollPane.setManaged(false);
            return;
        }

        for (int i = 0; i < fieldPreviews.size(); i++) {
            FieldPreview field = fieldPreviews.get(i);

            // Add label in white (Default color)
            Text labelText = new Text(field.getLabel() + " ");
            labelText.setFill(Color.WHITE);
            livePreviewDisplay.getChildren().add(labelText);

            // If invalid, display text in red
            if (!field.getValue().isEmpty()) {
                Text valueText = new Text(field.getValue());
                valueText.setFill(field.isValid() ? Color.WHITE : Color.RED);
                livePreviewDisplay.getChildren().add(valueText);
            }

            // Add newline except for the last field
            if (i < fieldPreviews.size() - 1) {
                Text newline = new Text("\n");
                newline.setFill(Color.WHITE);
                livePreviewDisplay.getChildren().add(newline);
            }
        }

        // Show TextFlow, hide TextArea
        resultDisplay.setVisible(false);
        resultDisplay.setManaged(false);
        livePreviewScrollPane.setVisible(true);
        livePreviewScrollPane.setManaged(true);
    }

}
