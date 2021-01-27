package bot;

import bot.commands.AbstractCommand;
import bot.commands.admin.GrabTimeCommand;
import bot.commands.admin.LinkServerCommand;
import bot.commands.admin.PrefixCommand;
import bot.commands.admin.ResetCooldownsCommand;
import bot.commands.owner.*;
import bot.commands.user.carddrop.CardDropCommand;
import bot.commands.user.inventory.InspectCardCommand;
import bot.commands.user.inventory.InventoryCommand;
import bot.commands.user.inventory.MaterialsCommand;
import bot.commands.user.inventory.ShowCollectionCommand;
import bot.commands.user.shop.BuyArmorCommand;
import bot.commands.user.squadron.*;
import bot.commands.user.trading.AddToMultiTradeCommand;
import bot.commands.user.trading.MultiTradeCommand;
import bot.commands.user.trading.SendCardsCommand;
import bot.commands.user.trading.TradeCardForCardCommand;
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
import game.cards.*;
import game.materials.Material;
import game.player_objects.ArmorItemPersonal;
import game.shop.items.*;
import game.player_objects.squadron.Squadron;
import game.player_objects.StockValue;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BotAnimeCards {

    private JDA discordAPI;
    private AnimeCardsGame game;
    private CommandClient commandListener;
    private final EventWaiter eventWaiter;

    public BotAnimeCards() {
        this(new EventWaiter());
    }

    public BotAnimeCards(EventWaiter eventWaiter) {
        this.eventWaiter = eventWaiter;
    }

    public AnimeCardsGame getGame() {
        return game;
    }

    public void setGame(AnimeCardsGame game) {
        this.game = game;
    }

    public boolean authenticate(String token) {
        try{
            // all functions must be called in this order
            if (game == null) loadSettings();
            commandListener = buildCommandClient();
            discordAPI = buildJDA(token);
            discordAPI.awaitReady();
            return true;
        }
        catch (LoginException | InterruptedException e2){
            Logger.getGlobal().log(Level.SEVERE,"cannot authenticate with provided token");
            return false;
        }
    }

    CommandClient buildCommandClient() {
        CommandClientBuilder builder = getCommandClientBuilder(null);
        addCommands(builder, getCommands(game));
        return builder.build();
    }

    @Nonnull
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
                new ShutdownCommand(this),
                new PrefixCommand(this),
                new ResetCooldownsCommand(game),
                new GrabTimeCommand(game),
                new LinkServerCommand(game),

                new CardDropCommand(game),
                new DailyCommand(game),
                new TopCharactersCommand(game),
                new CooldownCommand(game),

                new AddCardCommand(game),
                new AddCardsFromFileCommand(game),
                new DeleteCardCommand(game),
                new ModifyCharacterCommand(game),

                new ShowCollectionCommand(game),
                new InspectCardCommand(game),

                new ShopCommand(game),
                new BuyCommand(game),
                new ArmorShopCommand(game),
                new BuyArmorCommand(game),

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
                new TradeCardForCardCommand(game),
                new MultiTradeCommand(game),
                new AddToMultiTradeCommand(game)
        };
    }

    @Nonnull
    JDA buildJDA(String token) throws LoginException {
        return JDABuilder.createDefault(token).addEventListeners(commandListener, eventWaiter).build();
    }

    public void shutdown(){
        eventWaiter.waitForEvent(StatusChangeEvent.class,
                event -> event.getNewStatus() == JDA.Status.SHUTDOWN,
                event -> System.exit(0)
        );

        discordAPI.shutdown();
    }

    public void loadSettings() {
        loadSettings("hibernate.cfg.xml");
    }

    public void loadSettings(String configPath) {
        SessionFactory sessionFactory = getDatabaseSessionFactory(configPath);
        game = new AnimeCardsGame(eventWaiter, sessionFactory.openSession());
    }

    public SessionFactory getDatabaseSessionFactory(String configPath) {
        Configuration config = new Configuration()
                .addAnnotatedClass(Player.class)
                .addAnnotatedClass(CardGlobal.class)
                .addAnnotatedClass(CardPersonal.class)
                .addAnnotatedClass(CharacterInfo.class)
                .addAnnotatedClass(SeriesInfo.class)
                .addAnnotatedClass(StockValue.class)
                .addAnnotatedClass(Squadron.class)
                .addAnnotatedClass(ArmorItem.class)
                .addAnnotatedClass(ArmorItemPersonal.class)
                .configure(configPath);

        ServiceRegistry reg = new StandardServiceRegistryBuilder()
                .applySettings(config.getProperties())
                .build();

        return config.buildSessionFactory(reg);
    }

    public void loadTestGameSettings() {
        if (game == null) loadSettings();
        loadTestGameSettings(game);
    }

    public void loadTestGameSettings(AnimeCardsGame game) {
        Player tester1 = game.getOrCreatePlayer("409754559775375371");
        Player tester2 = game.getOrCreatePlayer("347162620996091904");

        List<CardGlobal> cards = List.of(
                new CardGlobal(
                "Riko",
                "Made in Abyss",
                "https://drive.google.com/uc?export=view&id=1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7",
                        new CardStatsGlobal(1000, 10, 1800, Charisma.NEUTRAL)),
                new CardGlobal(
                "Reg",
                "Made in Abyss",
                "https://drive.google.com/uc?export=view&id=1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7",
                        new CardStatsGlobal(110, 1, 125, Charisma.NEUTRAL)),
                new CardGlobal(
                "Mitty",
                "Made in Abyss",
                "https://drive.google.com/uc?export=view&id=1ZgYRfy6pxFeDh2TKH8d2n6yENwyEuUN7",
                        new CardStatsGlobal(5, 0, 5, Charisma.NEUTRAL)),
                new CardGlobal(
                "Haruhi Suzumiya",
                "Suzumiya Haruhi no Yuuutsu",
                "https://drive.google.com/uc?export=view&id=1KzOy0arH9zuVx3L0HNc_ge6lrWPL-ZKk",
                        new CardStatsGlobal(100, 80, 115, Charisma.NEUTRAL)),
                new CardGlobal(
                "Kaiman",
                "Dorohedoro",
                "https://drive.google.com/uc?export=view&id=1yv3-lkLhsH5PlClDtdjxOdLYhqFEmB5x",
                        new CardStatsGlobal(1000, 80, 1300, Charisma.NEUTRAL)));

        tester1.getCards().clear();
        for (CardGlobal card : cards) {
            game.addCard(card);
            game.pickPersonalCard(tester1.getId(), card, 1);
        }

        tester1.getMaterials().setAmount(Material.GOLD, 100);
    }

    private static String loadBotTokenFile() throws IOException {
        Path filePath = Path.of("token.txt");
        return Files.readString(filePath);
    }

    public static void main(String[] args) throws IOException {
        String bot_token = loadBotTokenFile();
        BotAnimeCards bot = new BotAnimeCards();

        if (!bot.authenticate(bot_token)){
            return;
        }

        bot.loadTestGameSettings();
    }
}
