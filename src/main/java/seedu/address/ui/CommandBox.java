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
import seedu.address.logic.parser.DuplicateFieldChecker;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Person;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;
    private final Consumer<List<FieldPreview>> livePreviewCallback;
    private final javafx.collections.ObservableList<Person> personList;

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor} and
     * live preview callback.
     */
    public CommandBox(CommandExecutor commandExecutor, Consumer<List<FieldPreview>> livePreviewCallback,
            javafx.collections.ObservableList<seedu.address.model.person.Person> personList) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        this.livePreviewCallback = livePreviewCallback;
        this.personList = personList;
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
    void handleLiveFeedback(String input) {
        assert input != null : "Input should not be null";
        if (input.isEmpty()) {
            livePreviewCallback.accept(new ArrayList<>());
            return;
        }

        List<FieldPreview> fieldPreviews;
        if (input.startsWith("add")) {
            fieldPreviews = getAddCommandPreviews(input);
        } else if (input.startsWith("edit")) {
            fieldPreviews = getEditCommandPreviews(input);
        } else {
            livePreviewCallback.accept(new ArrayList<>());
            return;
        }

        livePreviewCallback.accept(fieldPreviews);
    }

    /**
     * Extracts field previews for add command.
     */
    private List<FieldPreview> getAddCommandPreviews(String input) {
        List<FieldPreview> fieldPreviews = new ArrayList<>();
        String args = input.substring(3).trim();
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
        boolean isDuplicateName = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_NAME);

        String phone = argMultimap.getValue(CliSyntax.PREFIX_PHONE).orElse("");
        boolean isDuplicatePhone = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_PHONE);

        String email = argMultimap.getValue(CliSyntax.PREFIX_EMAIL).orElse("");
        boolean isDuplicateEmail = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_EMAIL);

        String address = argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).orElse("");
        boolean isDuplicateAddress = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_ADDRESS);

        String tags = String.join(", ", argMultimap.getAllValues(CliSyntax.PREFIX_TAG));

        fieldPreviews.add(createNamePreview(name, isDuplicateName));
        fieldPreviews.add(createPhonePreview(phone, isDuplicatePhone));
        fieldPreviews.add(createEmailPreview(email, isDuplicateEmail));
        fieldPreviews.add(createAddressPreview(address, isDuplicateAddress));
        fieldPreviews.add(new FieldPreview("Tags (t/):", tags, true));
        return fieldPreviews;
    }

    /**
     * Extracts field previews for edit command.
     */
    private List<FieldPreview> getEditCommandPreviews(String input) {
        List<FieldPreview> fieldPreviews = new ArrayList<>();
        String args = input.substring(4).trim();
        String[] allTokens = args.split("\\s+");
        if (allTokens.length < 2) {
            return new ArrayList<>();
        }
        // Only one number allowed after "edit"
        int indexCount = 0;
        for (String token : allTokens) {
            if (token.matches("\\d+")) {
                indexCount++;
            }
        }
        if (indexCount != 1) {
            fieldPreviews.add(createEditInvalidIndexPreview());
            return fieldPreviews;
        }
        String indexStr = allTokens[0];
        int index = -1;
        try {
            index = Integer.parseInt(indexStr) - 1;
        } catch (NumberFormatException e) {
            fieldPreviews.add(createEditInvalidIndexPreview());
            return fieldPreviews;
        }
        if (index < 0 || index >= personList.size()) {
            fieldPreviews.add(createEditInvalidIndexPreview());
            return fieldPreviews;
        }
        Person person = personList.get(index);

        String flagArgs = args.substring(indexStr.length()).trim();
        if (!flagArgs.isEmpty() && !flagArgs.startsWith(" ")) {
            flagArgs = " " + flagArgs;
        }
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                flagArgs,
                CliSyntax.PREFIX_NAME,
                CliSyntax.PREFIX_PHONE,
                CliSyntax.PREFIX_EMAIL,
                CliSyntax.PREFIX_ADDRESS,
                CliSyntax.PREFIX_TAG);

        if (argMultimap.getValue(CliSyntax.PREFIX_NAME).isPresent()) {
            boolean isDuplicateName = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_NAME);
            fieldPreviews.add(createEditNamePreview(person, argMultimap.getValue(CliSyntax.PREFIX_NAME).orElse(""),
                    isDuplicateName));
        }
        if (argMultimap.getValue(CliSyntax.PREFIX_PHONE).isPresent()) {
            boolean isDuplicatePhone = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_PHONE);
            fieldPreviews.add(createEditPhonePreview(person, argMultimap.getValue(CliSyntax.PREFIX_PHONE).orElse(""),
                    isDuplicatePhone));
        }
        if (argMultimap.getValue(CliSyntax.PREFIX_EMAIL).isPresent()) {
            boolean isDuplicateEmail = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_EMAIL);
            fieldPreviews.add(createEditEmailPreview(person, argMultimap.getValue(CliSyntax.PREFIX_EMAIL).orElse(""),
                    isDuplicateEmail));
        }
        if (argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).isPresent()) {
            boolean isDuplicateAddress = DuplicateFieldChecker.isDuplicateField(argMultimap, CliSyntax.PREFIX_ADDRESS);
            fieldPreviews.add(createEditAddressPreview(person,
                    argMultimap.getValue(CliSyntax.PREFIX_ADDRESS).orElse(""), isDuplicateAddress));
        }
        if (!argMultimap.getAllValues(CliSyntax.PREFIX_TAG).isEmpty()) {
            fieldPreviews.add(createEditTagsPreview(person, argMultimap.getAllValues(CliSyntax.PREFIX_TAG)));
        }
        return fieldPreviews;
    }

    private FieldPreview createEditNamePreview(Person person, String newName, boolean duplicate) {
        String oldName = person.getName().fullName;
        if (duplicate) {
            return new FieldPreview("Name (n/):", oldName + " -> " + newName + " (duplicate)", false);
        }
        boolean isValid = Name.isValidName(newName);
        return new FieldPreview("Name (n/):", oldName + " -> " + newName, isValid);
    }

    private FieldPreview createEditPhonePreview(Person person, String newPhone, boolean duplicate) {
        String oldPhone = person.getPhone().value;
        if (duplicate) {
            return new FieldPreview("Phone (p/):", oldPhone + " -> " + newPhone + " (duplicate)", false);
        }
        boolean isValid = Phone.isValidPhone(newPhone);
        return new FieldPreview("Phone (p/):", oldPhone + " -> " + newPhone, isValid);
    }

    private FieldPreview createEditEmailPreview(Person person, String newEmail, boolean duplicate) {
        String oldEmail = person.getEmail().value;
        if (duplicate) {
            return new FieldPreview("Email (e/):", oldEmail + " -> " + newEmail + " (duplicate)", false);
        }
        boolean isValid = Email.isValidEmail(newEmail);
        return new FieldPreview("Email (e/):", oldEmail + " -> " + newEmail, isValid);
    }

    private FieldPreview createEditAddressPreview(Person person, String newAddress, boolean duplicate) {
        String oldAddress = person.getAddress().value;
        if (duplicate) {
            return new FieldPreview("Address (a/):", oldAddress + " -> " + newAddress + " (duplicate)", false);
        }
        boolean isValid = Address.isValidAddress(newAddress);
        return new FieldPreview("Address (a/):", oldAddress + " -> " + newAddress, isValid);
    }

    private FieldPreview createEditTagsPreview(Person person, List<String> newTagsList) {
        String oldTags = String.join(", ",
                person.getTags().stream().map(tag -> tag.tagName).toArray(String[]::new));
        String newTags = String.join(", ", newTagsList);
        return new FieldPreview("Tags (t/):", oldTags + " -> " + newTags, true);
    }

    private FieldPreview createEditInvalidIndexPreview() {
        return new FieldPreview("Edit Preview", "Invalid index!", false);
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
