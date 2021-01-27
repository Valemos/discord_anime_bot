package bot.commands.user.trading;

import bot.commands.AbstractCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.SeriesInfo;
import game.player_objects.ArmorItemPersonal;
import game.cards.CardPersonal;
import game.contract.MultiTradeContract;
import game.materials.Material;
import game.materials.MaterialsSet;
import game.player_objects.StockValue;
import org.kohsuke.args4j.Option;

import java.util.*;
import java.util.stream.Collectors;

public class AddToMultiTradeCommand extends AbstractCommand<AddToMultiTradeCommand.Arguments> {

    public static class Arguments {

        @Option(name = "-card", aliases = {"-c"}, usage = "personal card id")
        List<String> cardIds = new ArrayList<>();

        @Option(name = "-armor", aliases = {"-a"}, usage = "armor item id")
        List<String> armorIds = new ArrayList<>();

        @Option(name = "-material", aliases = {"-m"}, usage = "specify material amounts in format -m <material_name>=<amount>")
        Map<String, String> materialsMap = new HashMap<>();

        @Option(name = "-stock", aliases = {"-s"}, usage = "series name with stock value you want to give in format <name>=<float amount>")
        Map<String, String> stockValuesMap = new HashMap<>();
    }

    public AddToMultiTradeCommand(AnimeCardsGame game) {
        super(game, Arguments.class);
        name = "addtrade";
        aliases = new String[]{"trade"};
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {

        String playerId = player.getId();
        MultiTradeContract contract = game.getContractsManager().getForUser(MultiTradeContract.class, playerId);
        if (contract == null){
            sendMessage(event, "you don't have active multitrade contract");
            return;
        }

        List<CardPersonal> cards = getCards(commandArgs.cardIds);
        List<ArmorItemPersonal> armorItems = getArmorItems(commandArgs.armorIds);
        Map<SeriesInfo, Float> stockValues = getStocks(commandArgs.stockValuesMap);
        MaterialsSet materialsSet = getMaterialsSet(commandArgs.materialsMap);

        if (!cards.isEmpty())           contract.addCards(playerId, cards);
        if (!armorItems.isEmpty())      contract.addArmor(playerId, armorItems);
        if (!stockValues.isEmpty())     contract.addStocks(player, stockValues);
        if (!materialsSet.isEmpty())    contract.addMaterials(player, materialsSet);

        contract.buildMenu(game).sendMenu(event);
    }

    private MaterialsSet getMaterialsSet(Map<String, String> materialsMap) {
        MaterialsSet materialsSet = new MaterialsSet();
        for(String key : materialsMap.keySet()){
            Material material = Material.fromString(key);
            if (material != null) {
                Integer amount = tryParseInteger(materialsMap.get(key));
                if (amount != null) materialsSet.addAmount(material, amount);
            }
        }
        return materialsSet;
    }

    private Map<SeriesInfo, Float> getStocks(Map<String, String> stockValues) {
        return stockValues.keySet().stream()
                .map(stockName -> player.findStockByUniqueName(stockName))
                .filter(Objects::nonNull)
                .map(stock -> {
                    Float stockIncrement = tryParseFloat(stockValues.get(stock.getSeries().getName()));
                    if (stockIncrement != null){
                        return new StockValue(
                                stock.getSeries(),
                                stock.getValue() > stockIncrement ? stockIncrement : stock.getValue());
                    }else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(StockValue::getSeries, StockValue::getValue));
    }

    private List<ArmorItemPersonal> getArmorItems(List<String> armorIds) {
        return player.getArmorItems().stream()
                .filter(item -> armorIds.contains(item.getId()))
                .collect(Collectors.toList());
    }


    private Integer tryParseInteger(String string) {
        try{
            return Integer.parseInt(string);
        }catch (NumberFormatException e){
            return null;
        }
    }

    private Float tryParseFloat(String string) {
        try{
            return Float.parseFloat(string);
        }catch (NumberFormatException e){
            return null;
        }
    }

    private List<CardPersonal> getCards(List<String> cardIds) {
        return cardIds.stream()
                .map(id -> game.getCardsPersonal().getById(id))
                .filter(Objects::nonNull)
                .filter(card -> card.getOwner().equals(player))
                .collect(Collectors.toList());
    }
}
