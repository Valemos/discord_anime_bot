package bot.commands;

import bot.CommandPermissions;
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
    private final Class<T> argumentsClass;
    protected T commandArgs;

    protected AnimeCardsGame game;
    protected Player player;

    public AbstractCommand(AnimeCardsGame game, Class<T> argumentsClass) {
        this(game, argumentsClass, CommandPermissions.USER);
    }

    public AbstractCommand(AnimeCardsGame game, Class<T> argumentsClass, CommandPermissions commandPermissions) {
        this.game = game;
        this.argumentsClass = argumentsClass;
        userPermissions = commandPermissions.getRequiredPermissions();
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

        if(tryParseArguments(event)){
            handle(event);
        }
    }

    private boolean tryParseArguments(CommandEvent event) {
        String[] args = Commandline.translateCommandline(event.getArgs());

        try{
            getNewArgumentsParser(argumentsClass).parseArgument(args);
            return true;
        }catch(CmdLineException e){
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
        return false;
    }

    public abstract void handle(CommandEvent event);

    protected static void sendMessage(CommandEvent event, String message) {
        event.getChannel().sendMessage(message).queue();
    }
}
