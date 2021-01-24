package bot.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.Player;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import org.apache.tools.ant.types.Commandline;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class AbstractCommand<T> extends Command {
    private final Class<T> argumentsClass;
    protected T commandArgs;

    protected AnimeCardsGame game;
    protected Player player;

    public AbstractCommand(AnimeCardsGame game, Class<T> argumentsClass) {
        this.game = game;
        this.argumentsClass = argumentsClass;

        help = getHelpMessage(argumentsClass);
    }

    private String getHelpMessage(Class<T> argumentsClass) {
        CmdLineParser parser = new CmdLineParser(createArgumentsInstance(argumentsClass));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        parser.printUsage(stream);

        return stream.toString(StandardCharsets.UTF_8);
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
    public void execute(CommandEvent event) {
        String userId = event.getAuthor().getId();
        player = game.getPlayer(userId);

        commandArgs = createArgumentsInstance(argumentsClass);

        try {
            tryParseArguments(commandArgs, event.getArgs());
            handle(event);

        } catch (CmdLineException e) {
            event.getChannel().sendMessage(e.getMessage()).queue();
        }
    }

    public static <T> void tryParseArguments(T argumentsObject, String argsString) throws CmdLineException {
        String[] args = Commandline.translateCommandline(argsString);
        new CmdLineParser(argumentsObject).parseArgument(args);
    }

    public abstract void handle(CommandEvent event);

    protected static void sendMessage(CommandEvent event, String message) {
        sendMessage(event.getEvent(), message);
    }

    protected static void sendMessage(GenericMessageEvent event, String message) {
        event.getChannel().sendMessage(message).queue();
    }

    public boolean isSameCommandClass(Class<? extends AbstractCommand<?>> otherClass){
        return this.getClass().getCanonicalName().startsWith(otherClass.getCanonicalName());
    }

    public T getArgumentsObject() {
        return commandArgs;
    }
}
