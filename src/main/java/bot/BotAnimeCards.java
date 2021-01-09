package bot;

import bot.commands.handlers.creator.CreateGlobalCardHandler;
import bot.commands.handlers.creator.JoinAsTesterHandler;
import bot.commands.handlers.user.DropHandler;
import bot.commands.handlers.creator.JoinAsCreatorHandler;
import bot.commands.handlers.user.ShowCardHandler;
import bot.commands.handlers.user.ShowCollectionHandler;
import game.*;
import game.cards.CardStatsGlobal;
import game.cards.CharacterCardGlobal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class BotAnimeCards {

    private final MessageCommandsHandler commandsHandler;
    JDA discord_api;

    AnimeCardsGame game;

    public BotAnimeCards() {
        game = new AnimeCardsGame();
        commandsHandler = new MessageCommandsHandler(game);
        commandsHandler.setCommands(
                new DropHandler(),
                new CreateGlobalCardHandler(),
                new JoinAsCreatorHandler(),
                new ShowCollectionHandler(),
                new ShowCardHandler(),
                new JoinAsTesterHandler()
        );
    }

    public boolean authenticate(String token) {
        try{
            discord_api = getApiBuilderWithHandlers(token).build();
            discord_api.awaitReady();
            return true;
        }
        catch (LoginException | InterruptedException e2){
            Logger.getGlobal().log(Level.SEVERE,"cannot use bot token");
            return false;
        }
    }

    @NotNull
    private JDABuilder getApiBuilderWithHandlers(String token) {
        return JDABuilder.createDefault(token)
                .addEventListeners(commandsHandler);
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
                "https://drive.google.com/file/d/1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7/view?usp=sharing"));
        game.addCard(new CharacterCardGlobal(
                "Haruhi Suzumiya",
                "Suzumiya Haruhi no Yuuutsu",
                "https://sandradillondesign.com.au/wp-content/uploads/2015/02/Number-2-Cake-Topper-Blue-Sandra-Dillon-Design-500x500.jpg"));
        game.addCard(new CharacterCardGlobal(
                "Kaiman",
                "Dorohedoro",
                "https://www.pngarts.com/files/3/Number-3-PNG-Image-Transparent.png"));

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
