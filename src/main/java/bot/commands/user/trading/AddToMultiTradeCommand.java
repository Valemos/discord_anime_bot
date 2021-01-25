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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AddToMultiTradeCommand extends AbstractCommand<AddToMultiTradeCommand.Arguments> {

    public static class Arguments {

        @Option(name = "-card", aliases = {"-c"}, usage = "personal card id")
        List<String> cardIds;

        @Option(name = "-armor", aliases = {"-a"}, usage = "armor item id")
        List<String> armorIds;

        @Option(name = "-material", aliases = {"-m"}, usage = "specify material amounts in format -m <material_name>=<amount>")
        Map<String, Integer> materialsMap;

        @Option(name = "-stock", aliases = {"-s"}, usage = "series name with stock value you want to give in format <name>=<float amount>")
        Map<String, Float> stockValuesMap;
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

        contract.addCards(playerId, cards);
        contract.addArmor(playerId, armorItems);
        contract.addStocks(playerId, stockValues);
        contract.addMaterials(playerId, materialsSet);

        contract.buildMenu(game).sendMenu(event);
    }

    private MaterialsSet getMaterialsSet(Map<String, Integer> materialsMap) {
        MaterialsSet materialsSet = new MaterialsSet();
        for(String key : materialsMap.keySet()){
            Material material = Material.fromString(key);
            if (material != null) {
                materialsSet.addAmount(material, materialsMap.get(key));
            }
        }
        return materialsSet;
    }

    private List<ArmorItemPersonal> getArmorItems(List<String> armorIds) {
        return player.getArmorItems().stream()
                .filter(item -> armorIds.contains(item.getId()))
                .collect(Collectors.toList());
    }

    private Map<SeriesInfo, Float> getStocks(Map<String, Float> stockValues) {
        return player.getStocks().stream()
                .filter(stock -> stockValues.containsKey(stock.getSeries().getName()))
                .map(stock -> new StockValue(
                        stock.getSeries(),
                        stock.getValue() - stockValues.get(stock.getSeries().getName())
                ))
                .collect(Collectors.toMap(StockValue::getSeries, StockValue::getValue));
    }

    private List<CardPersonal> getCards(List<String> cardIds) {
        return player.getCards().stream()
                .filter(card -> cardIds.contains(card.getName()))
                .collect(Collectors.toList());
    }
}
