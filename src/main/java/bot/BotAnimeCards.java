package bot;

import bot.commands.AbstractCommand;
import bot.commands.admin.GrabTimeCommand;
import bot.commands.admin.PrefixCommand;
import bot.commands.creator.*;
import bot.commands.user.inventory.InspectCardCommand;
import bot.commands.user.inventory.InventoryCommand;
import bot.commands.user.inventory.MaterialsCommand;
import bot.commands.user.inventory.ShowCollectionCommand;
import bot.commands.user.squadron.*;
import bot.commands.user.wishlist.*;
import bot.commands.user.*;
import bot.commands.user.shop.ArmorShopCommand;
import bot.commands.user.shop.BuyCommand;
import bot.commands.user.shop.ShopCommand;
import bot.commands.user.stocks.*;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import game.*;
import game.cards.CardGlobal;
import game.cards.CardStatsGlobal;
import game.cards.Charisma;
import game.items.ItemGlobal;
import game.items.Material;
import game.items.MaterialsSet;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.exit;
import static java.util.Map.*;

public class BotAnimeCards {

    JDA discordAPI;
    AnimeCardsGame game;
    CommandClient commandListener;
    EventWaiter eventWaiter;

    public BotAnimeCards() {
        this(new EventWaiter());
    }

    public BotAnimeCards(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
        game = new AnimeCardsGame(eventWaiter);
    }

    public AnimeCardsGame getGame() {
        return game;
    }

    public JDA getDiscordAPI() {
        return discordAPI;
    }

    public boolean authenticate(String token) {
        try{
            commandListener = buildCommandClient();
            discordAPI = buildJDA(token);
            discordAPI.awaitReady();
            return true;
        }
        catch (LoginException | InterruptedException e2){
            Logger.getGlobal().log(Level.SEVERE,"cannot use bot token");
            return false;
        }
    }

    CommandClient buildCommandClient() {
        CommandClientBuilder builder = getCommandClientBuilder(null);
        addCommands(builder, getCommands(game));
        return builder.build();
    }

    @NotNull
    private CommandClientBuilder getCommandClientBuilder(String altPrefix) {
        CommandClientBuilder builder = new CommandClientBuilder()
                .setOwnerId("797845777618698240")
                .setCoOwnerIds("409754559775375371")
                .setPrefix("#");

        if (altPrefix != null) builder = builder.setAlternativePrefix(altPrefix);

        return builder;
    }

    public void rebuildCommandClient(String prefix){
        CommandClientBuilder builder = getCommandClientBuilder(prefix);

        List<Command> commands = commandListener.getCommands();
        addCommands(builder, commands);

        discordAPI.removeEventListener(commandListener);
        commandListener = builder.build();
        discordAPI.addEventListener(commandListener);
    }

    void addCommands(CommandClientBuilder builder, Collection<Command> commands) {
        for (Command command : commands){
            builder.addCommand(command);
        }
    }

    void addCommands(CommandClientBuilder builder, Command ... commands) {
        for (Command command : commands){
            builder.addCommand(command);
        }
    }

    AbstractCommand<?>[] getCommands(AnimeCardsGame game) {
        return new AbstractCommand<?>[]{
                new PrefixCommand(this),
                new ResetCooldownsCommand(game),
                new GrabTimeCommand(game),

                new DropCommand(game),
                new DailyCommand(game),
                new TopCharactersCommand(game),
                new CooldownCommand(game),

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
                new SquadronRemoveCommand(game),
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

    @NotNull
    JDA buildJDA(String token) throws LoginException {
        return JDABuilder.createDefault(token).addEventListeners(commandListener, eventWaiter).build();
    }

    public void shutdown(){
        discordAPI.shutdown();
    }

    public boolean loadExternalSettings() {
        return false;
    }

    public void loadDefaultGameSettings(AnimeCardsGame game) {
        Player tester1 = game.createNewPlayer("409754559775375371");
        Player tester2 = game.createNewPlayer("347162620996091904");

        List<CardGlobal> cards = List.of(
                new CardGlobal(
                "Riko",
                "Made in Abyss",
                "https://drive.google.com/uc?export=view&id=1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7",
                        new CardStatsGlobal(-1, 1000, 10, 1800, Charisma.NEUTRAL)),
                new CardGlobal(
                "Reg",
                "Made in Abyss",
                "https://drive.google.com/uc?export=view&id=1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7",
                        new CardStatsGlobal(-1, 110, 1, 125, Charisma.NEUTRAL)),
                new CardGlobal(
                "Mitty",
                "Made in Abyss",
                "https://drive.google.com/uc?export=view&id=1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7",
                        new CardStatsGlobal(-1, 5, 0, 5, Charisma.NEUTRAL)),
                new CardGlobal(
                "Haruhi Suzumiya",
                "Suzumiya Haruhi no Yuuutsu",
                "https://drive.google.com/uc?export=view&id=1KzOy0arH9zuVx3L0HNc_ge6lrWPL-ZKk",
                        new CardStatsGlobal(-1, 100, 80, 115, Charisma.NEUTRAL)),
                new CardGlobal(
                "Kaiman",
                "Dorohedoro",
                "https://drive.google.com/uc?export=view&id=1yv3-lkLhsH5PlClDtdjxOdLYhqFEmB5x",
                        new CardStatsGlobal(-1, 1000, 80, 1300, Charisma.NEUTRAL))
        );

        for (CardGlobal card : cards) {
            game.addCard(card);
            game.pickPersonalCard(tester1, card.getId(), 1);
            game.pickPersonalCard(tester2, card.getId(), 1);
        }

        tester1.getMaterials().setAmount(Material.GOLD, 100);

        game.getItemsGlobal().addItem(new ItemGlobal(
                "some potion 1", 100, 0, "",
                new MaterialsSet(of(Material.GOLD, 9000))));
        game.getItemsGlobal().addItem(new ItemGlobal(
                "some potion 2", 0, 5, "*potion description*",
                new MaterialsSet(of(Material.GOLD, 50))));
        game.getItemsGlobal().addItem(new ItemGlobal(
                "dummy item 1", 8, 0, "*item details*",
                new MaterialsSet(of(Material.GOLD, 20))));
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
