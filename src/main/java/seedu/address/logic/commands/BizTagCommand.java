package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FEATURE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.HashSet;
import java.util.Set;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tag.FeatureTag;
import seedu.address.model.tag.Tag;

/**
 * Declares Features for grouping Tags to be used as categories.
 */
public class BizTagCommand extends Command {
    public static final String COMMAND_WORD = "biz";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Declares Features, and Tags as categories for Statistics.\n"
            + "Parameters: "
            + PREFIX_FEATURE + "FEATURE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_FEATURE + "Plan "
            + PREFIX_TAG + "A "
            + PREFIX_TAG + "B";

    public static final String MANUAL = String.join("\n",
            "NAME",
            "  biz — Declares Features to use for grouping Tags for Statistics.",
            "        Using biz with the same Feature name overwrites the existing Feature - Tags combination.",
            "",
            "USAGE",
            "  biz f/FEATURE [t/TAG]…",
            "",
            "PARAMETERS",
            "  • FEATURE: non-empty string, may contain spaces but not leading/trailing spaces",
            "  • TAG: alphanumeric, no spaces; you may have multiple t/TAG categories for the FEATURE",
            "",
            "EXAMPLES",
            "  biz f/Plan t/A t/B",
            "",
            "SEE MORE",
            "  https://ay2526s1-cs2103-f13-2.github.io/tp/UserGuide.html#adding-a-person-add"
    );

    public static final String MESSAGE_DECLARED_BIZ_TAGS =
            "The following Feature(s) and Tags have been declared for Stats:\n%S";

    public static final String UNDO_SUCCESS =
            "The following Feature(s) have been undeclared:\n%s";
    public static final String UNDO_SUCCESS_RESTORE =
            "The following Feature(s) have been restored to original:\n%s";


    private final FeatureTag feature;
    private final Set<Tag> tags;
    private boolean isExistingFeature;
    private Set<Tag> previousTags;

    /**
     * Creates a BizTagCommand to declare the specified {@code Feature} and {@code Tags}
     * @param feature Feature
     * @param tags to be used for grouping Tags in statistics
     */
    public BizTagCommand(FeatureTag feature, Set<Tag> tags) {
        requireNonNull(feature);
        requireNonNull(tags);
        this.feature = feature;
        this.tags = tags;
        this.isExistingFeature = false;
        this.previousTags = new HashSet<>();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BizTagCommand)) {
            return false;
        }
        BizTagCommand e = (BizTagCommand) other;
        return feature.equals(e.feature)
                && tags.equals(e.tags);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.isBizFeature(feature)) {
            isExistingFeature = true;
            previousTags = model.getBizTags().get(feature);
        }
        model.addBizTags(feature, tags);
        return new CommandResult(String.format(MESSAGE_DECLARED_BIZ_TAGS,
                String.join(" ", feature.toString(), tags.toString())));
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
        model.removeBizFeature(feature);
        if (isExistingFeature) {
            model.addBizTags(feature, previousTags);
            return String.format(UNDO_SUCCESS_RESTORE, feature.toString());
        }
        return String.format(UNDO_SUCCESS, feature.toString());
    }
}
