package bot;

import bot.commands.BotCommandHandler;
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
        BotCommandHandler command = findCommandForString(messageContent);

        String userId = event.getAuthor().getId();

        if (!userHasAccessToCommand(userId, command)) {
            command.handleCommand(game);
        }
    }

    boolean userHasAccessToCommand(String userId, BotCommandHandler command) {
        if (command == null) return false;

        Player player = game.getPlayerById(userId);
        if (player == null) return false;

        return player.getAccessLevel().level >= command.getAccessLevel().level;
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
