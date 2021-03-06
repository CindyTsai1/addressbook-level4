package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    //@@author April0616
    public static final String DELETE_ONE_PERSON_VALIDATION_REGEX = "-?\\d+";

    public static final String DELETE_MULTIPLE_PERSON_COMMA_VALIDATION_REGEX =
            "((-?\\d([\\s+]*)\\,([\\s+]*)(?=-?\\d))|-?\\d)+";

    public static final String DELETE_MULTIPLE_PERSON_WHITESPACE_VALIDATION_REGEX =
            "(((-?\\d)([\\s]+)(?=-?\\d))|-?\\d)+";


    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);
        String preamble = argMultimap.getPreamble();

        if (preamble.equals("")) { // code block for delete for a tag
            DeleteCommand tagList = getDeleteCommandForTags(argMultimap);
            if (tagList != null) {
                return tagList;
            }
        } else if (preamble.matches(DELETE_ONE_PERSON_VALIDATION_REGEX)) { // code block for delete for a person
            try {
                Index index = ParserUtil.parseIndex(args);
                return new DeleteCommand(index);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
        } else if (preamble.matches(DELETE_MULTIPLE_PERSON_COMMA_VALIDATION_REGEX)) {
            //code block for delete multiple persons, input string separated by comma
            try {
                ArrayList<Index> deletePersons = ParserUtil.parseIndexes(args, ",");
                return new DeleteCommand(deletePersons);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
        } else if (preamble.matches(DELETE_MULTIPLE_PERSON_WHITESPACE_VALIDATION_REGEX)) {
            //code block for delete multiple persons, input indexes separated by whitespace
            try {
                ArrayList<Index> deletePersons = ParserUtil.parseIndexes(args, " ");
                return new DeleteCommand(deletePersons);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
        }

        throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    //@@author nbriannl
    private DeleteCommand getDeleteCommandForTags (ArgumentMultimap argMultimap) throws ParseException {
        try {
            if (arePrefixesPresent(argMultimap, PREFIX_TAG)) {
                Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
                return new DeleteCommand(tagList);
            }
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
        return null;
    }

    //@@author nbriannl-reused
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
