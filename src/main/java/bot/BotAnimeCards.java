package bot;

import bot.commands.CreateCardHandler;
import bot.commands.DropHandler;
import bot.commands.JoinAsCreatorHandler;
import game.*;
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
        commandsHandler.setCommands(new ArrayList<>(List.of(
                new DropHandler(),
                new CreateCardHandler(),
                new JoinAsCreatorHandler()
        )));
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
        CardStats stats = new CardStats(0, 5, 0, CharismaState.CHARISMATIC, Constitution.HEALTHY);

        game.addCard(new CharacterCard(
                "Riko",
                "Made in Abyss",
                "img", stats.clone()));
        game.addCard(new CharacterCard(
                "Haruhi Suzumiya",
                "Suzumiya Haruhi no Yuuutsu",
                "img", stats.clone()));
        game.addCard(new CharacterCard(
                "Kaiman",
                "Dorohedoro",
                "img", stats.clone()));
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
