package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";
    private static final Logger logger = LogsCenter.getLogger(CommandBox.class.getName());

    private final CommandExecutor commandExecutor;
    private final Consumer<List<FieldPreview>> livePreviewCallback;
    private final ObservableList<Person> personList;

    private List<String> commandHistory = new ArrayList<>();
    private int historyPointer = -1;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor} and
     * live preview callback.
     */
    public CommandBox(CommandExecutor commandExecutor, Consumer<List<FieldPreview>> livePreviewCallback,
            ObservableList<Person> personList) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.livePreviewCallback = livePreviewCallback;
        this.personList = personList;
        // Add listener to command box to handle live preview
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> {
            setStyleToDefault();
            handleLiveFeedback(commandTextField.getText());
        });
        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
    }

    public TextField getCommandTextField() {
        return this.commandTextField;
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            commandTextField.setText("");
            commandHistory.add(commandText);
            historyPointer = commandHistory.size();
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Provides live feedback for add command.
     */
    void handleLiveFeedback(String input) {
        assert input != null : "Input should not be null";
        if (input.isEmpty()) {
            livePreviewCallback.accept(new ArrayList<>());
            return;
        }
        String[] splitInput = input.split(" ");
        String prefix = splitInput.length == 0 ? "" : splitInput[0];
        List<FieldPreview> fieldPreviews;
        if (prefix.equals("add")) {
            logger.info("----------------[LIVE PREVIEW][" + "add user" + "]");
            fieldPreviews = AddPreviewBuilder.buildPreview(input);
        } else if (prefix.equals("edit")) {
            logger.info("----------------[LIVE PREVIEW][" + "edit user" + "]");
            fieldPreviews = EditPreviewBuilder.buildPreview(input, personList);
        } else {
            livePreviewCallback.accept(new ArrayList<>());
            return;
        }

        livePreviewCallback.accept(fieldPreviews);
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

    /**
     * Handles any key pressed event.
     */
    @FXML
    private void handleKeyPress(KeyEvent keyEvent) {
        if (!keyEvent.getCode().isArrowKey()) {
            return;
        }

        String history = switch (keyEvent.getCode()) {
        case UP -> {
            keyEvent.consume();
            // Navigate up the history
            if (commandHistory.isEmpty() || historyPointer <= 0) {
                yield null;
            } else {
                historyPointer--;
                yield commandHistory.get(historyPointer);
            }
        }
        case DOWN -> {
            keyEvent.consume();
            // Navigate down the history
            if (commandHistory.isEmpty() || historyPointer >= commandHistory.size()) {
                yield null;
            } else if (historyPointer == commandHistory.size() - 1) {
                historyPointer++;
                yield "";
            } else {
                historyPointer++;
                yield commandHistory.get(historyPointer);
            }
        }
        default -> null;
        };

        if (history != null) {
            commandTextField.setText(history);
        }
    }

}

