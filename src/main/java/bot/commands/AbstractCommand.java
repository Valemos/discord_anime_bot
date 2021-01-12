package bot.commands;

import bot.CommandAccessLevel;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import org.apache.tools.ant.types.Commandline;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class AbstractCommand<T> extends Command {

    private final Class<T> argumentsConfig;
    protected T commandArgs;
    protected AnimeCardsGame game;
    protected Player player;
    private final CommandAccessLevel commandAccessLevel;


    public static class NoArgumentsConfig {}

    public AbstractCommand(AnimeCardsGame game, Class<T> argumentsConfig) {
        this(game, argumentsConfig, CommandAccessLevel.USER);
    }

    public AbstractCommand(AnimeCardsGame game, Class<T> argumentsConfig, CommandAccessLevel accessLevel) {
        this.game = game;
        this.argumentsConfig = argumentsConfig;
        commandAccessLevel = accessLevel;
        getNewArgumentsParser(argumentsConfig);
    }

    @NotNull
    private CmdLineParser getNewArgumentsParser(Class<T> argumentsConfig) {
        commandArgs = createArgumentsInstance(argumentsConfig);
        return new CmdLineParser(commandArgs);
    }

    private T createArgumentsInstance(Class<T> config) {
        try {
            return config.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Logger.getGlobal().log(Level.SEVERE, "cannot create command arguments instance");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void execute(CommandEvent event) {
        String userId = event.getAuthor().getId();
        player = game.getPlayerById(userId);
        if (player == null) player = game.createNewPlayer(userId);

        if (playerHasAccess(player, this)){
            if(tryParseArguments(event)){
                handle(event);
            }
        }
    }

    private boolean tryParseArguments(CommandEvent event) {
        String[] args = Commandline.translateCommandline(event.getArgs());

        try{
            getNewArgumentsParser(argumentsConfig).parseArgument(args);
            return true;
        }catch(CmdLineException e){
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
        return false;
    }

    private boolean playerHasAccess(@NotNull Player player, AbstractCommand<T> command) {
        CommandAccessLevel playerAccessLevel = player.getAccessLevel();
        if (CommandAccessLevel.USER == commandAccessLevel){

            return true;

        } else if (CommandAccessLevel.ADMIN == commandAccessLevel){

            return CommandAccessLevel.ADMIN == playerAccessLevel ||
                    CommandAccessLevel.CREATOR == playerAccessLevel;

        } else if (CommandAccessLevel.CREATOR == commandAccessLevel){

            return CommandAccessLevel.CREATOR == playerAccessLevel;

        }
        return false;
    }

    protected abstract void handle(CommandEvent event);

    protected static void sendMessage(CommandEvent event, String message) {
        event.getChannel().sendMessage(message).queue();
    }
}
