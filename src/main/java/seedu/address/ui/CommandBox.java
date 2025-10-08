package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final Consumer<List<FieldPreview>> livePreviewCallback;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor} and
     * live preview callback.
     */
    public CommandBox(CommandExecutor commandExecutor, Consumer<List<FieldPreview>> livePreviewCallback) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.livePreviewCallback = livePreviewCallback;
        // Add listener to command box to handle live preview
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> {
            setStyleToDefault();
            handleLiveFeedback(commandTextField.getText());
        });
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
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Provides live feedback for add command.
     */
    private void handleLiveFeedback(String input) {
        assert !input.isEmpty() : "Input should not be empty";
        assert input != null : "Input should not be null";

        if (!input.startsWith("add")) {
            // Clear live preview when not typing add command
            livePreviewCallback.accept(new ArrayList<>());
            return;
        }

        String args = input.substring(3).trim();

        // Ensure there's a space before the first prefix for tokenizer to work
        if (!args.isEmpty() && !args.startsWith(" ")) {
            args = " " + args;
        }

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args,
                CliSyntax.PREFIX_NAME,
                CliSyntax.PREFIX_PHONE,
                CliSyntax.PREFIX_EMAIL,
                CliSyntax.PREFIX_ADDRESS,
                CliSyntax.PREFIX_TAG);

        // Check for duplicate fields
        String name = argMultimap.getValue(CliSyntax.PREFIX_NAME).orElse("");
        boolean isDuplicateName = argMultimap.getAllValues(CliSyntax.PREFIX_NAME).size() > 1;

        String phone = argMultimap.getValue(CliSyntax.PREFIX_PHONE).orElse("");
        boolean isDuplicatePhone = argMultimap.getAllValues(CliSyntax.PREFIX_PHONE).size() > 1;

        String email = argMultimap.getValue(CliSyntax.PREFIX_EMAIL).orElse("");
        boolean isDuplicateEmail = argMultimap.getAllValues(CliSyntax.PREFIX_EMAIL).size() > 1;

        String address = argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).orElse("");
        boolean isDuplicateAddress = argMultimap.getAllValues(CliSyntax.PREFIX_ADDRESS).size() > 1;

        String tags = String.join(", ", argMultimap.getAllValues(CliSyntax.PREFIX_TAG));

        List<FieldPreview> fieldPreviews = new ArrayList<>();
        fieldPreviews.add(createNamePreview(name, isDuplicateName));
        fieldPreviews.add(createPhonePreview(phone, isDuplicatePhone));
        fieldPreviews.add(createEmailPreview(email, isDuplicateEmail));
        fieldPreviews.add(createAddressPreview(address, isDuplicateAddress));
        fieldPreviews.add(new FieldPreview("Tags (t/):", tags, true));

        livePreviewCallback.accept(fieldPreviews);
    }

    static FieldPreview createNamePreview(String name, boolean duplicate) {
        if (duplicate) {
            return new FieldPreview("Name (n/):", name + " (duplicate)", false);
        } else if (!name.isEmpty()) {
            boolean isValid = Name.isValidName(name);
            return new FieldPreview("Name (n/):", name, isValid);
        }
        return new FieldPreview("Name (n/):", "", true);
    }

    static FieldPreview createPhonePreview(String phone, boolean duplicate) {
        if (duplicate) {
            return new FieldPreview("Phone (p/):", phone + " (duplicate)", false);
        } else if (!phone.isEmpty()) {
            boolean isValid = Phone.isValidPhone(phone);
            return new FieldPreview("Phone (p/):", phone, isValid);
        }
        return new FieldPreview("Phone (p/):", "", true);
    }

    static FieldPreview createEmailPreview(String email, boolean duplicate) {
        if (duplicate) {
            return new FieldPreview("Email (e/):", email + " (duplicate)", false);
        } else if (!email.isEmpty()) {
            boolean isValid = Email.isValidEmail(email);
            return new FieldPreview("Email (e/):", email, isValid);
        }
        return new FieldPreview("Email (e/):", "", true);
    }

    static FieldPreview createAddressPreview(String address, boolean duplicate) {
        if (duplicate) {
            return new FieldPreview("Address (a/):", address + " (duplicate)", false);
        } else if (!address.isEmpty()) {
            boolean isValid = Address.isValidAddress(address);
            return new FieldPreview("Address (a/):", address, isValid);
        }
        return new FieldPreview("Address (a/):", "", true);
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

}
