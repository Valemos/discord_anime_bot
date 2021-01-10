package bot;

import bot.commands.handlers.AbstractBotCommand;
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
    private HashMap<String, AbstractBotCommand> commandsMap;

    public MessageCommandsHandler(@NotNull AnimeCardsGame game) {
        this.game = game;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

//        AbstractBotCommand commandHandler = findCommandForString(messageString);
//        if (commandHandler == null){
//            return;
//        }
//
//        User user = event.getAuthor();
//        Player player = getPlayerById(user.getId());
//
//        if (playerHasAccessToCommand(player, commandHandler)) {
//            handleCommandMessage(player, user, commandHandler, event.getChannel(), messageString);
//        }
    }

    void handleCommandMessage(Player player, User user, AbstractBotCommand command,
                              MessageChannel channel, String messageContent) {

//        MessageArguments messageArguments = command.parseArguments(messageContent);
//        if (messageArguments.isValid()){
//        CommandParameters parameters = new CommandParameters(game, player, user, channel, messageArguments);
//        command.handle(parameters);
//        }else{
//            channel.sendMessage("invalid arguments: " + messageArguments.getErrorMessage()).queue();
//        }
    }

    Player getPlayerById(String id) {
        Player player = game.getPlayerById(id);

        if (player == null){
            player = game.createNewPlayer(id);
        }

        return player;
    }

    boolean playerHasAccessToCommand(Player player, AbstractBotCommand command) {
        if (command == null || player == null) {
            return false;
        }

//        TODO write comparsion with enums
        return true;
    }


//    AbstractBotCommand findCommandForString(String messageString) {
//        String commandName = CommandArgument.getCommandName(messageString);
//        return commandsMap.getOrDefault(commandName, null);
//    }

    public void setCommands(AbstractBotCommand... commands) {
        setCommands(Arrays.asList(commands));
    }

    public void setCommands(Collection<AbstractBotCommand> commands) {
        commandsMap = new HashMap<>();
        for (AbstractBotCommand command : commands) {
            for (String name : command.getCommandNames()){
                commandsMap.put(name, command);
            }
        }
    }

    public List<AbstractBotCommand> getAllCommands() {
        return new ArrayList<>(commandsMap.values());
    }

}
