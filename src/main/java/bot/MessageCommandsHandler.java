package bot;

import bot.commands.BotCommandHandler;
import bot.commands.CommandArguments;
import game.AnimeCardsGame;
import game.Player;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MessageCommandsHandler extends ListenerAdapter {

    private final AnimeCardsGame game;
    private HashMap<String, BotCommandHandler> commandsMap;

    public MessageCommandsHandler(AnimeCardsGame game) {
        this.game = game;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String messageContent = event.getMessage().getContentRaw();
        if (!BotCommandHandler.stringIsCommand(messageContent)){
            return;
        }

        BotCommandHandler commandHandler = findCommandForString(messageContent);

        Player player = game.getPlayerById(event.getAuthor().getId());

        if (!playerHasAccessToCommand(player, commandHandler)) {
            handleCommandMessageForUser(player, commandHandler, messageContent);
        }
    }

    private void handleCommandMessageForUser(Player player, BotCommandHandler commandHandler, String messageContent) {

        String[] messageArguments = commandHandler.getArguments(messageContent);
        if (commandHandler.isArgumentsValid(messageArguments)){
            CommandArguments args = new CommandArguments(player, game, messageArguments);
            commandHandler.handleCommand(args);
        }
    }

    boolean playerHasAccessToCommand(Player player, BotCommandHandler command) {
        if (command == null || player == null) {
            return false;
        }else{
            return player.getAccessLevel().level >= command.getAccessLevel().level;
        }
    }

    BotCommandHandler findCommandForString(String messageString) {
        String commandName = BotCommandHandler.getCommandName(messageString);
        return commandsMap.getOrDefault(commandName, null);
    }

    public void setCommands(List<BotCommandHandler> commandList) {
        commandsMap = new HashMap<>();
        for (BotCommandHandler command : commandList) {
            commandsMap.put(command.getName(), command);
        }
    }

    public List<BotCommandHandler> getAllCommands() {
        return new ArrayList<>(commandsMap.values());
    }
}
