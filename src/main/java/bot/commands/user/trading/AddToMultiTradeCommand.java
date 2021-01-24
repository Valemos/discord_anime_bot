package bot.commands.user.trading;

import bot.commands.AbstractCommand;
import bot.menu.MultiTradeContractMenu;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.ArmorItemPersonal;
import game.cards.CardPersonal;
import game.contract.MultiTradeContract;
import game.materials.Material;
import game.materials.MaterialsSet;
import game.player_objects.StockValue;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.Option;

import java.util.List;
import java.util.Map;

public class AddToMultiTradeCommand extends AbstractCommand<AddToMultiTradeCommand.Arguments> {

    public static class Arguments {
        public MaterialsSet materialsSet = new MaterialsSet();

        @Option(name = "-material", aliases = {"-m"}, usage = "specify material amounts in format -m <material_name>=<amount>")
        public void setMaterials(Map<String, Integer> valuesMap) throws CmdLineException {
            StringBuilder errorMessage = new StringBuilder();
            for(String key : valuesMap.keySet()){
                Material material = Material.fromString(key);
                if (material != null) materialsSet.addAmount(material, valuesMap.get(key));
                else errorMessage.append(key).append(" material not found\n");
            }
            throw new CmdLineException(errorMessage.toString());
        }

        @Option(name = "-card", aliases = {"-c"}, usage = "personal card id")
        List<String> cardIds;

        @Option(name = "-armor", aliases = {"-a"}, usage = "armor item id")
        List<String> armorIds;

        @Option(name = "-stock", aliases = {"-s"}, usage = "series name with stock value you want to give")
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

        MultiTradeContract contract = game.getContractsManager().getForUser(MultiTradeContract.class, player.getId());
        if (contract == null){
            sendMessage(event, "you don't have active multitrade contract");
            return;
        }

        List<CardPersonal> cards = getCards(commandArgs.cardIds);
        List<ArmorItemPersonal> armorItems = getArmorItems(commandArgs.armorIds);
        List<StockValue> stockValues = getStocks(commandArgs.stockValuesMap);

        contract.addCards(cards);
        contract.addArmor(armorItems);
        contract.addStocks(stockValues);
    }

    private List<ArmorItemPersonal> getArmorItems(List<String> armorIds) {
        return null;
    }

    private List<StockValue> getStocks(Map<String, Float> stockValuesMap) {
        return null;
    }

    private List<CardPersonal> getCards(List<String> cardIds) {
        return null;
    }
}
