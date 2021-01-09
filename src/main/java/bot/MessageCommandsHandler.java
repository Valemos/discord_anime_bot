package bot;

import bot.commands.arguments.CommandArgument;
import bot.commands.handlers.BotCommandHandler;
import bot.commands.CommandParameters;
import bot.commands.arguments.MessageArguments;
import game.AnimeCardsGame;
import game.Player;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MessageCommandsHandler extends ListenerAdapter {

    private final AnimeCardsGame game;
    private HashMap<String, BotCommandHandler> commandsMap;

    public MessageCommandsHandler(@NotNull AnimeCardsGame game) {
        this.game = game;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        String messageString = event.getMessage().getContentRaw().trim();
        if (!CommandArgument.stringIsCommand(messageString)){
            return;
        }

        BotCommandHandler commandHandler = findCommandForString(messageString);
        if (isInvalidCommand(commandHandler)){
            return;
        }

        User user = event.getAuthor();
        Player player = getExistingOrCreateNewPlayerById(user.getId());

        if (playerHasAccessToCommand(player, commandHandler)) {
            handleCommandMessageForPlayer(player, user, commandHandler, event.getChannel(), messageString);
        }
    }

    Player getExistingOrCreateNewPlayerById(String id) {
        Player player = game.getPlayerById(id);

        if (player == null){
            player = game.createNewPlayer(id);
        }

        return player;
    }

    void handleCommandMessageForPlayer(Player player, User user, BotCommandHandler command,
                                       MessageChannel channel, String messageContent) {

        MessageArguments messageArguments = command.parseArguments(messageContent);
        if (messageArguments.isValid()){
            CommandParameters parameters = new CommandParameters(game, player, user, channel, messageArguments);
            command.handle(parameters);
        }else{
            channel.sendMessage("invalid arguments: " + messageArguments.getErrorMessage()).queue();
        }
    }

    boolean playerHasAccessToCommand(Player player, BotCommandHandler command) {
        if (command == null || player == null) {
            return false;
        }

        return player.getAccessLevel().level >= command.getAccessLevel().level;
    }

    private boolean isInvalidCommand(BotCommandHandler commandHandler) {
        return commandHandler == null;
    }

    BotCommandHandler findCommandForString(String messageString) {
        String commandName = CommandArgument.getCommandName(messageString);
        return commandsMap.getOrDefault(commandName, null);
    }

    public void setCommands(BotCommandHandler... commands) {
        setCommands(Arrays.asList(commands));
    }

    public void setCommands(Collection<BotCommandHandler> commands) {
        commandsMap = new HashMap<>();
        for (BotCommandHandler command : commands) {
            for (String name : command.getCommandNames()){
                commandsMap.put(name, command);
            }
        }
    }

    public List<BotCommandHandler> getAllCommands() {
        return new ArrayList<>(commandsMap.values());
    }

}
