package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_MISSING_BIZ_TAGS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FEATURE;

import java.util.HashMap;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.FeatureTag;
import seedu.address.model.tag.Tag;

/**
 * Undeclares Features, and their corresponding Tags from Statistics.
 */
public class BizUntagCommand extends Command {
    public static final String COMMAND_WORD = "unbiz";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Undeclares Features, and their grouped Tags from Statistics.\n"
            + "Parameters: "
            + "[" + PREFIX_FEATURE + "FEATURE]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_FEATURE + "Plan "
            + PREFIX_FEATURE + "Gender";
    public static final String MESSAGE_SUCCESS = "The following Feature(s) have been undeclared:\n";
    public static final String UNDO_SUCCESS = "The following Feature(s) have been redeclared:\n%s";
    public static final String MANUAL = String.join("\n",
            "NAME",
            "  unbiz — Undeclares Feature(s), and their grouped Tags from Statistics.",
            "",
            "USAGE",
            "  biz [f/FEATURE] ...",
            "",
            "PARAMETERS",
            "  • FEATURE: non-empty string, may contain spaces but not leading/trailing spaces",
            "",
            "EXAMPLES",
            "  unbiz f/Plan f/Gender",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#adding-a-person-add"
    );

    private final Set<FeatureTag> features;
    private HashMap<FeatureTag, Set<Tag>> bizTags = new HashMap<>();

    /**
     * Creates an BizUntagCommand to undeclare specified {@code features} in Statistics.
     * @param features Features currently declared in Statistics
     */
    public BizUntagCommand(Set<FeatureTag> features) {
        requireNonNull(features);
        this.features = features;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        StringBuilder missingFeatures = new StringBuilder();
        for (FeatureTag feature : features) {
            if (!model.isBizFeature(feature)) {
                missingFeatures.append(feature.toString()).append(" ");
            }
        }

        if (!missingFeatures.isEmpty()) {
            throw new CommandException(
                    String.format(MESSAGE_INVALID_MISSING_BIZ_TAGS,
                            "[" + missingFeatures + "]"));
        }

        this.bizTags = model.getBizTags(); // Save

        StringBuilder unTaggedFeatures = new StringBuilder();
        for (FeatureTag feature : features) {
            model.removeBizFeature(feature);
            unTaggedFeatures.append(feature.toString()).append(" ");
        }

        return new CommandResult(
                String.join("\n", MESSAGE_SUCCESS, unTaggedFeatures.toString()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BizUntagCommand)) {
            return false;
        }
        BizUntagCommand e = (BizUntagCommand) other;
        return features.equals(e.features);
    }

    @Override
    public String man() {
        return MANUAL;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public String undo(Model model) {
        requireNonNull(model);
        StringBuilder reTaggedFields = new StringBuilder();
        for (FeatureTag feature : features) {
            Set<Tag> cats = bizTags.get(feature);
            model.addBizTags(feature, cats);
            reTaggedFields
                    .append(feature.toString())
                    .append(" ").append(cats.toString());
        }
        return String.format(UNDO_SUCCESS, reTaggedFields.toString());
    }
}
