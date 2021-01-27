package game.contract;

import bot.menu.MultiTradeContractMenu;
import game.Player;
import game.cards.CardPersonal;
import game.cards.SeriesInfo;
import game.materials.MaterialsSet;
import game.player_objects.ArmorItemPersonal;
import org.hibernate.Session;

import java.util.*;

public class MultiTradeContract extends AbstractContract {

    private final Set<CardPersonal> senderCards = new HashSet<>();
    private final Set<CardPersonal> receiverCards = new HashSet<>();

    private final Set<ArmorItemPersonal> senderArmor = new HashSet<>();
    private final Set<ArmorItemPersonal> receiverArmor = new HashSet<>();

    private Map<SeriesInfo, Float> senderStocks = new HashMap<>();
    private Map<SeriesInfo, Float> receiverStocks = new HashMap<>();

    private MaterialsSet senderMaterials = new MaterialsSet();
    private MaterialsSet receiverMaterials = new MaterialsSet();

    public MultiTradeContract(String senderId, String receiverId) {
        super(senderId, receiverId, MultiTradeContractMenu.class);
    }

    @Override
    public boolean finish(Session session, Player sender, Player receiver) {
        exchangeSets(sender.getCards(), receiver.getCards(), senderCards, receiverCards);

        exchangeSets(sender.getArmorItems(), receiver.getArmorItems(), senderArmor, receiverArmor);

        sender.removeStocks(senderStocks);
        sender.addStocks(receiverStocks);

        receiver.addStocks(senderStocks);
        receiver.removeStocks(receiverStocks);

        sender.subtractMaterials(senderMaterials);
        sender.addMaterials(receiverMaterials);

        receiver.subtractMaterials(receiverMaterials);
        receiver.addMaterials(senderMaterials);

        return true;
    }

    private <T> void exchangeSets(List<T> sender, List<T> receiver, Set<T> send,
                                  Set<T> received) {

        receiver.addAll(send);
        sender.removeAll(send);

        sender.addAll(received);
        receiver.removeAll(received);
    }

    @Override
    public String getMoreInfo() {
        return null;
    }

    public void addCards(String playerId, List<CardPersonal> cards) {
        if (senderId.equals(playerId)){
            senderCards.addAll(cards);
        }else if (receiverId.equals(playerId)){
            receiverCards.addAll(cards);
        }
        updateMenu();
    }

    public void addArmor(String playerId, List<ArmorItemPersonal> armorItems) {
        if (senderId.equals(playerId)){
            senderArmor.addAll(armorItems);
        }else if (receiverId.equals(playerId)){
            receiverArmor.addAll(armorItems);
        }
        updateMenu();
    }

    public void addStocks(Player player, Map<SeriesInfo, Float> stockValues) {
        String playerId = player.getId();
        if (senderId.equals(playerId)){
            incrementMap(senderStocks, stockValues);
            senderStocks = player.getAvailableStocks(senderStocks);
        }else if (receiverId.equals(playerId)){
            incrementMap(receiverStocks, stockValues);
            receiverStocks = player.getAvailableStocks(receiverStocks);
        }
        updateMenu();
    }

    public void addMaterials(Player player, MaterialsSet materialsSet) {
        String playerId = player.getId();
        if (senderId.equals(playerId)){
            senderMaterials.addMaterials(materialsSet);
            senderMaterials = player.getAvailableMaterials(senderMaterials);
        }else if (receiverId.equals(playerId)){
            receiverMaterials.addMaterials(materialsSet);
            receiverMaterials = player.getAvailableMaterials(receiverMaterials);
        }
        updateMenu();
    }

    private void incrementMap(Map<SeriesInfo, Float> map, Map<SeriesInfo, Float> increments) {
        increments.forEach((key, value) -> incrementValue(map, key, value));
    }

    private void incrementValue(Map<SeriesInfo, Float> map, SeriesInfo key, Float increment) {
        map.put(key, increment + map.getOrDefault(key, 0f));
    }

    public Set<CardPersonal> getSenderCards() {
        return senderCards;
    }

    public Set<CardPersonal> getReceiverCards() {
        return receiverCards;
    }

    public Set<ArmorItemPersonal> getSenderArmor() {
        return senderArmor;
    }

    public Set<ArmorItemPersonal> getReceiverArmor() {
        return receiverArmor;
    }

    public Map<SeriesInfo, Float> getSenderStocks() {
        return senderStocks;
    }

    public Map<SeriesInfo, Float> getReceiverStocks() {
        return receiverStocks;
    }

    public MaterialsSet getSenderMaterials() {
        return senderMaterials;
    }

    public MaterialsSet getReceiverMaterials() {
        return receiverMaterials;
    }
}
