package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.core.Messages;
import seedu.address.commons.events.ui.ChangeThemeRequestEvent;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Changes the theme to the theme indicated
 */
public class ThemeCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "theme";
    public static final String COMMAND_ALIAS = "th";
    public static final String COMMAND_SECONDARY = "changetheme";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the theme to the specified word. "
            + "Possible words are: dark, light"
            + "Example: " + COMMAND_WORD + " dark\n"
            + "         " + COMMAND_WORD + " light";

    public static final String MESSAGE_SUCCESS = "Theme switched: %1$s";

    private final String themeKeyword;

    /**
     * Creates an AddCommand to add the specified {@code ReadOnlyPerson}
     */
    public ThemeCommand (String themeKeyword) {
        this.themeKeyword = themeKeyword;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);

        String themeToSwitch;

        HashMap<String, String> themes = model.getThemeMap();

        if (themes.containsKey(themeKeyword)) {
            themeToSwitch = themes.get(themeKeyword);
        } else {
            throw new CommandException(Messages.MESSAGE_THEME_NOT_FOUND);
        }

        EventsCenter.getInstance().post(new ChangeThemeRequestEvent(themeToSwitch));

        return new CommandResult(String.format(MESSAGE_SUCCESS, themeToSwitch));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ThemeCommand // instanceof handles nulls
                && themeKeyword.equals(((ThemeCommand) other).themeKeyword));
    }
}
