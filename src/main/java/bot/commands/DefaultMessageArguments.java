package bot.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultMessageArguments extends MessageArguments {

    public List<String> commandParts = new ArrayList<>();

    public DefaultMessageArguments(CommandInfo commandInfo) {
        super(commandInfo);
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public DefaultMessageArguments fromString(String commandString){
        try {
            List<String> parts = CommandParser.getStringCommandParts(commandString);
            setValidatedMessageParts(parts);
        } catch (CommandParser.CommandParseException e) {
            Logger.getGlobal().log(Level.INFO, e.getMessage());
        } catch (InvalidCommandPartsException e) {
            Logger.getGlobal().log(Level.SEVERE, "invalid command parsed. programmer error");
            throw new RuntimeException(e);
        }
        return this;
    }

    private void setValidatedMessageParts(List<String> parts) throws InvalidCommandPartsException {
        if (commandPartsValid(parts)) {
            if (parts.size() == 1) {
                commandParts = new ArrayList<>();
            } else {
                parts.remove(0);
                commandParts = parts;
            }
        }else{
            throw new InvalidCommandPartsException("command parts invalid");
        }
    }

    protected boolean commandPartsValid(List<String> parts){
        if (parts != null) {
            if (parts.size() > 0) {
                return parts.get(0).equals(CommandInfo.commandChar + commandInfo.name);
            }
        }
        return false;
    }
}
