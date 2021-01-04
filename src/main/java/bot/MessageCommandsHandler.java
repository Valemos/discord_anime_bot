package bot;

import bot.commands.BotCommandHandler;
import game.AnimeCardsGame;
import game.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MessageCommandsHandler extends ListenerAdapter {

    protected static final String commandPrefix = "#";
    private final AnimeCardsGame game;
    private HashMap<String, BotCommandHandler> commandsMap;

    public MessageCommandsHandler(AnimeCardsGame game) {
        this.game = game;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String messageContent = event.getMessage().getContentRaw();
        BotCommandHandler command = findCommandForString(messageContent);

        String userId = event.getAuthor().getId();

        if (!userHasAccessToCommand(userId, command)) {
            command.handleCommand(game);
        }
    }


    boolean userHasAccessToCommand(String userId, BotCommandHandler command) {
        if (command == null) return false;

        User user = game.getUserById(userId);
        if (user == null) return false;

        return user.getAccessLevel().level >= command.getAccessLevel().level;
    }

    BotCommandHandler findCommandForString(String messageString) {
        String commandName = getCommandName(messageString);
        return commandsMap.getOrDefault(commandName, null);
    }

    String getCommandName(String commandString) {
        if (!commandString.startsWith(commandPrefix)){
            return "";
        }

        try{
            int nameStart = commandString.indexOf(commandPrefix) + 1;
            int nameEnd = commandString.indexOf(' ');
            if (nameEnd == -1) nameEnd = commandString.length();

            return commandString.substring(nameStart, nameEnd);
        }catch(IndexOutOfBoundsException e){
            return "";
        }
    }

    public void setCommands(List<BotCommandHandler> commandList) {
        commandsMap = new HashMap<>();
        for (BotCommandHandler command : commandList) {
            commandsMap.put(command.getName(), command);
        }
    }

    public List<BotCommandHandler> getAllHandlers() {
        return new ArrayList<>(commandsMap.values());
    }
}
