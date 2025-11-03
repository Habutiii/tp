package seedu.address.ui;
import static javafx.beans.binding.Bindings.min;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    record LabelWithText(Label l, javafx.scene.text.Text t) {}
    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
            .map(tag->new LabelWithText(new Label(tag.tagName), new javafx.scene.text.Text(tag.tagName)))
            .peek(r->r.l().setWrapText(true)).peek(r->r.l().maxWidthProperty().bind(tags.widthProperty().subtract(5)))
            .peek(r->r.t().setFont(r.l().getFont())).peek(r->r.l().prefWidthProperty().bind(min(r.t().getLayoutBounds()
            .getWidth() + 5, tags.widthProperty().subtract(10)))).forEach(r->tags.getChildren().add(r.l()));
        name.maxWidthProperty().bind(cardPane.widthProperty().subtract(id.widthProperty()).subtract(40));
        address.maxWidthProperty().bind(cardPane.widthProperty().subtract(60));
        tags.maxWidthProperty().bind(cardPane.widthProperty().subtract(30));
    }
}
