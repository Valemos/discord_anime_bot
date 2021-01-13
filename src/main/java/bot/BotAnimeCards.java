package bot;

import bot.commands.AbstractCommand;
import bot.commands.creator.AddItemCommand;
import bot.commands.creator.AddCardCommand;
import bot.commands.creator.DeleteCardCommand;
import bot.commands.creator.DeleteItemCommand;
import bot.commands.user.*;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import game.*;
import game.cards.CardGlobal;
import game.items.ItemGlobal;
import game.items.ItemCollectionGlobal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class BotAnimeCards {

    JDA discord_api;
    AnimeCardsGame game;
    CommandClient commandClient;
    EventWaiter eventWaiter;

    public BotAnimeCards() {
        eventWaiter = new EventWaiter();
        game = new AnimeCardsGame(eventWaiter);
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

        addCommands(builder, Set.of(
                DropCommand.class,
                DailyCommand.class,

                AddCardCommand.class,
                DeleteCardCommand.class,
                AddItemCommand.class,
                DeleteItemCommand.class,

                ShowCollectionCommand.class,
                InspectCardCommand.class,

                ShopCommand.class,
                ArmorShopCommand.class,
                BuyCommand.class,

                SquadronCommand.class,
                SquadronAddCommand.class
        ));

        return builder.build();
    }

    private void addCommands(CommandClientBuilder builder, Set<Class<? extends AbstractCommand<?>>> commands) {
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
        commandClient = buildCommandClient();
        return JDABuilder.createDefault(token).addEventListeners(commandClient, eventWaiter).build();
    }

    public void shutdown(){
        discord_api.shutdown();
    }

    public boolean loadExternalSettings() {
        return false;
    }

    public void loadDefaultSettings() {
        Player tester = game.createNewPlayer("409754559775375371", CommandPermissions.CREATOR);
        Player tester2 = game.createNewPlayer("797845777618698240", CommandPermissions.CREATOR);

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

        ItemCollectionGlobal items = game.getItemsCollection();
        items.addItem(new ItemGlobal("item1", 1, 0));
        items.addItem(new ItemGlobal("item2", 2, 0));
        items.addItem(new ItemGlobal("item4", 3, 5));
        items.addItem(new ItemGlobal("item5", 3, 6));
        items.addItem(new ItemGlobal("item6", 0, 1));
        items.addItem(new ItemGlobal("item7", 15, 0));
        items.addItem(new ItemGlobal("item100", 15, 10));
        items.addItem(new ItemGlobal("item1000", 15, 15));
        items.addItem(new ItemGlobal("item3very long name with spaces", 3, 4));

        for (CardGlobal card : game.getCollection().getAllCards()){
            game.pickPersonalCardDelay(tester, card.getId(), 1);
            game.pickPersonalCardDelay(tester2, card.getId(), 1);
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
