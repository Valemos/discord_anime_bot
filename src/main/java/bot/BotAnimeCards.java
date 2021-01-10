package bot;

import bot.commands.handlers.AbstractCommand;
import bot.commands.handlers.creator.CreateGlobalCardCommand;
import bot.commands.handlers.creator.JoinAsTesterCommand;
import bot.commands.handlers.user.DropCommand;
import bot.commands.handlers.creator.JoinAsCreatorCommand;
import bot.commands.handlers.user.InspectCardCommand;
import bot.commands.handlers.user.ShowCollectionCommand;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import game.*;
import game.cards.CharacterCardGlobal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class BotAnimeCards {

    JDA discord_api;
    AnimeCardsGame game;

    public BotAnimeCards() {
        game = new AnimeCardsGame();
    }

    public boolean authenticate(String token) {
        try{
            discord_api = buildDiscordAPI(token);
            discord_api.awaitReady();
            return true;
        }
        catch (LoginException | InterruptedException e2){
            Logger.getGlobal().log(Level.SEVERE,"cannot use bot token");
            return false;
        }
    }

    private CommandClient buildCommandClient() {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId("797845777618698240");
        builder.setPrefix("#");
        builder.setAlternativePrefix("c#");

        addCommands(builder, List.of(
                        DropCommand.class,
                        CreateGlobalCardCommand.class,
                        JoinAsCreatorCommand.class,
                        ShowCollectionCommand.class,
                        InspectCardCommand.class,
                        JoinAsTesterCommand.class));

        return builder.build();
    }

    private void addCommands(CommandClientBuilder builder, List<Class<? extends AbstractCommand>> commands) {
        try {
            for (Class<? extends AbstractCommand> command : commands){
                AbstractCommand commandHandler =
                        (AbstractCommand) command
                                .getDeclaringClass()
                                .getConstructor(AnimeCardsGame.class)
                                .newInstance(game);
                builder.addCommands(commandHandler);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private JDA buildDiscordAPI(String token) throws LoginException {
        return JDABuilder.createDefault(token).addEventListeners(buildCommandClient()).build();
    }

    public void shutdown(){
        discord_api.shutdown();
    }

    public boolean loadExternalSettings() {
        return false;
    }

    public void loadDefaultSettings() {
        game.addCard(new CharacterCardGlobal(
                "Riko",
                "Made in Abyss",
                "https://drive.google.com/uc?export=view&id=1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7"));
        game.addCard(new CharacterCardGlobal(
                "Haruhi Suzumiya",
                "Suzumiya Haruhi no Yuuutsu",
                "https://drive.google.com/uc?export=view&id=1KzOy0arH9zuVx3L0HNc_ge6lrWPL-ZKk"));
        game.addCard(new CharacterCardGlobal(
                "Kaiman",
                "Dorohedoro",
                "https://drive.google.com/uc?export=view&id=1yv3-lkLhsH5PlClDtdjxOdLYhqFEmB5x"));
    }

    public void waitForShutdown() {
        try {
            discord_api.awaitStatus(JDA.Status.SHUTTING_DOWN);

            // clear all resources and exit

            discord_api.awaitStatus(JDA.Status.SHUTDOWN);
            exit(0);

        } catch (InterruptedException e) {
            Logger.getGlobal().log(Level.INFO, "shutdown await interrupted");
            exit(1);
        }
    }
}
