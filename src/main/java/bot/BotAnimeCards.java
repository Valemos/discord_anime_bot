package bot;

import bot.commands.CreateCardHandler;
import bot.commands.DropHandler;
import game.AnimeCardsGame;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotAnimeCards {

    private final MessageCommandsHandler commandsHandler;
    JDA discord_api;

    AnimeCardsGame game;

    public BotAnimeCards() {
        game = new AnimeCardsGame();
        commandsHandler = new MessageCommandsHandler(game);
        commandsHandler.setCommands(new ArrayList<>(List.of(
                new DropHandler(),
                new CreateCardHandler()
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

    public boolean loadEmailSettings() {
        return false;
    }

    public void loadDefaultSettings() {

    }
}
