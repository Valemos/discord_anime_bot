package bot;

import bot.commands.handlers.BotCommandHandler;
import bot.commands.CommandParameters;
import bot.commands.CommandParser;
import bot.commands.handlers.MessageArguments;
import game.AnimeCardsGame;
import game.Player;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageCommandsHandler extends ListenerAdapter {

    private final AnimeCardsGame game;
    private HashMap<String, BotCommandHandler> commandsMap;

    public MessageCommandsHandler(@NotNull AnimeCardsGame game) {
        this.game = game;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String messageString = event.getMessage().getContentRaw().trim();
        if (!CommandParser.stringIsCommand(messageString)){
            return;
        }

        BotCommandHandler commandHandler = findCommandForString(messageString);
        if (BotCommandHandler.isNotCommandValid(commandHandler)){
            return;
        }

        String playerId = event.getAuthor().getId();
        Player player = getExistingOrCreateNewPlayerById(playerId);

        if (playerHasAccessToCommand(player, commandHandler)) {
            handleCommandMessageForPlayer(player, commandHandler, event.getChannel(), messageString);
        }
    }

    Player getExistingOrCreateNewPlayerById(String id) {
        Player player = game.getPlayerById(id);

        if (player == null){
            player = game.createNewPlayer(id);
        }

        return player;
    }

    void handleCommandMessageForPlayer(Player player, BotCommandHandler commandHandler,
                                       MessageChannel channel, String messageContent) {

        MessageArguments messageArguments = commandHandler.getArguments(messageContent);
        if (messageArguments.isValid()){
            CommandParameters args = new CommandParameters(game, player, channel, messageArguments);
            commandHandler.handleCommand(args);
        }
    }

    boolean playerHasAccessToCommand(Player player, BotCommandHandler command) {
        if (command == null || player == null) {
            return false;
        }

        return player.getAccessLevel().level >= command.getAccessLevel().level;
    }

    BotCommandHandler findCommandForString(String messageString) {
        String commandName = CommandParser.getCommandName(messageString);
        return commandsMap.getOrDefault(commandName, null);
    }

    public void setCommands(List<BotCommandHandler> commandList) {
        commandsMap = new HashMap<>();
        for (BotCommandHandler command : commandList) {
            for (String name : command.getCommandNames()){
                commandsMap.put(name, command);
            }
        }
    }

    public List<BotCommandHandler> getAllCommands() {
        return new ArrayList<>(commandsMap.values());
    }
}
