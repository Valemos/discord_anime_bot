package bot;

import bot.commands.AbstractCommand;
import bot.commands.user.inventory.InspectCardCommand;
import bot.commands.user.inventory.InventoryCommand;
import bot.commands.user.inventory.MaterialsCommand;
import bot.commands.user.inventory.ShowCollectionCommand;
import bot.commands.user.wishlist.*;
import bot.commands.creator.AddItemCommand;
import bot.commands.creator.AddCardCommand;
import bot.commands.creator.DeleteCardCommand;
import bot.commands.creator.DeleteItemCommand;
import bot.commands.user.*;
import bot.commands.user.shop.ArmorShopCommand;
import bot.commands.user.shop.BuyCommand;
import bot.commands.user.shop.ShopCommand;
import bot.commands.user.squadron.PatrolCommand;
import bot.commands.user.squadron.PatrolStopCommand;
import bot.commands.user.squadron.SquadronAddCommand;
import bot.commands.user.squadron.SquadronCommand;
import bot.commands.user.stocks.*;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import game.*;
import game.cards.CardGlobal;
import game.items.ItemGlobal;
import game.items.ItemsGlobalManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;

public class BotAnimeCards {

    JDA discordAPI;
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
            discordAPI = buildDiscordAPI(token);
            discordAPI.awaitReady();
            return true;
        }
        catch (LoginException | InterruptedException e2){
            Logger.getGlobal().log(Level.SEVERE,"cannot use bot token");
            return false;
        }
    }

    private CommandClient buildCommandClient() {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId("797845777618698240")
                .setCoOwnerIds("409754559775375371")
                .setPrefix("#")
                .setAlternativePrefix("c#");

        addCommands(builder, getCommands(game));

        return builder.build();
    }

    AbstractCommand<?>[] getCommands(AnimeCardsGame game) {
        return new AbstractCommand<?>[]{
                new DropCommand(game),
                new DailyCommand(game),
                new TopCharactersCommand(game),

                new AddCardCommand(game),
                new DeleteCardCommand(game),
                new AddItemCommand(game),
                new DeleteItemCommand(game),

                new ShowCollectionCommand(game),
                new InspectCardCommand(game),

                new ShopCommand(game),
                new ArmorShopCommand(game),
                new BuyCommand(game),

                new SquadronCommand(game),
                new SquadronAddCommand(game),
                new PatrolCommand(game),
                new PatrolStopCommand(game),

                new ExchangeForStockCommand(game),
                new ShowStocksCommand(game),
                new StockValueCommand(game),
                new StockCollectionValueCommand(game),

                new WishListCommand(game),
                new WishCardCommand(game),
                new WishCardByIdCommand(game),
                new WishRemoveCommand(game),
                new WishRemoveByIdCommand(game),

                new InventoryCommand(game),
                new MaterialsCommand(game),

                new SendCardsCommand(game),
                new TradeCommand(game),
                new MultiTradeCommand(game)
        };
    }

    void addCommands(CommandClientBuilder builder, Command ... commands) {
        for (Command command : commands){
            builder.addCommand(command);
        }
    }

    JDA buildDiscordAPI(String token) throws LoginException {
        commandClient = buildCommandClient();
        return buildJDA(token);
    }

    @NotNull
    JDA buildJDA(String token) throws LoginException {
        return JDABuilder.createDefault(token).addEventListeners(commandClient, eventWaiter).build();
    }

    public void shutdown(){
        discordAPI.shutdown();
    }

    public boolean loadExternalSettings() {
        return false;
    }

    public void loadDefaultSettings() {
        Player tester = game.createNewPlayer("409754559775375371");
        Player tester2 = game.createNewPlayer("347162620996091904");

        CardGlobal card1 = new CardGlobal(
                "Riko",
                "Made in Abyss",
                "https://drive.google.com/uc?export=view&id=1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7");

        CardGlobal card2 = new CardGlobal(
                "Haruhi Suzumiya",
                "Suzumiya Haruhi no Yuuutsu",
                "https://drive.google.com/uc?export=view&id=1KzOy0arH9zuVx3L0HNc_ge6lrWPL-ZKk");

        CardGlobal card3 = new CardGlobal(
                "Kaiman",
                "Dorohedoro",
                "https://drive.google.com/uc?export=view&id=1yv3-lkLhsH5PlClDtdjxOdLYhqFEmB5x");

        game.addCard(card1);
        game.addCard(card2);
        game.addCard(card3);

        ItemsGlobalManager items = game.getItemsGlobal();
        items.addItem(new ItemGlobal("item1", 1, 0));
        items.addItem(new ItemGlobal("item2", 2, 0));
        items.addItem(new ItemGlobal("item4", 3, 5));
        items.addItem(new ItemGlobal("item5", 3, 6));

        for (CardGlobal card : game.getCardsGlobalManager().getAllCards()){
            game.pickPersonalCardDelay(tester, card.getId(), 1);
            game.pickPersonalCardDelay(tester2, card.getId(), 1);
        }
    }

    public void waitForShutdown() {
        try {
            discordAPI.awaitStatus(JDA.Status.SHUTTING_DOWN);

            // clear all resources and exit

            discordAPI.awaitStatus(JDA.Status.SHUTDOWN);
            exit(0);

        } catch (InterruptedException e) {
            Logger.getGlobal().log(Level.INFO, "shutdown await interrupted");
            exit(1);
        }
    }


}
