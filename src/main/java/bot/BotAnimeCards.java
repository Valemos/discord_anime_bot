package bot;

import bot.commands.AbstractCommand;
import bot.commands.creator.AddItemCommand;
import bot.commands.creator.CreateCardCommand;
import bot.commands.creator.DeleteItemCommand;
import bot.commands.user.DropCommand;
import bot.commands.user.InspectCardCommand;
import bot.commands.user.ShowCollectionCommand;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import game.*;
import game.cards.CardGlobal;
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

    public AnimeCardsGame getGame() {
        return game;
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
                CreateCardCommand.class,
                ShowCollectionCommand.class,
                InspectCardCommand.class,
                AddItemCommand.class,
                DeleteItemCommand.class
        ));

        return builder.build();
    }

    private void addCommands(CommandClientBuilder builder, List<Class<? extends AbstractCommand<?>>> commands) {
        try {
            for (Class<? extends AbstractCommand<?>> command : commands){
                AbstractCommand<?> commandHandler = command.getConstructor(AnimeCardsGame.class).newInstance(game);
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
        Player tester = game.createNewPlayer("409754559775375371", CommandAccessLevel.CREATOR);
        Player tester2 = game.createNewPlayer("797845777618698240", CommandAccessLevel.CREATOR);

        game.addCard(new CardGlobal(
                "Riko",
                "Made in Abyss",
                "https://drive.google.com/uc?export=view&id=1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7"));
        game.addCard(new CardGlobal(
                "Haruhi Suzumiya",
                "Suzumiya Haruhi no Yuuutsu",
                "https://drive.google.com/uc?export=view&id=1KzOy0arH9zuVx3L0HNc_ge6lrWPL-ZKk"));
        game.addCard(new CardGlobal(
                "Kaiman",
                "Dorohedoro",
                "https://drive.google.com/uc?export=view&id=1yv3-lkLhsH5PlClDtdjxOdLYhqFEmB5x"));

        for (CardGlobal card : game.getGlobalCollection().getAllCards()){
            game.pickPersonalCardDelay(tester, card.getCardId(), 1);
            game.pickPersonalCardDelay(tester2, card.getCardId(), 1);
        }
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
