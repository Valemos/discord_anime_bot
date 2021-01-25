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

    private final Map<SeriesInfo, Float> senderStocks = new HashMap<>();
    private final Map<SeriesInfo, Float> receiverStocks = new HashMap<>();

    private final MaterialsSet senderMaterials = new MaterialsSet();
    private final MaterialsSet receiverMaterials = new MaterialsSet();

    public MultiTradeContract(String senderId, String recipientId) {
        super(senderId, recipientId, MultiTradeContractMenu.class);
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

    public void addStocks(String playerId, Map<SeriesInfo, Float> stockValues) {
        if (senderId.equals(playerId)){
            incrementMap(senderStocks, stockValues);
        }else if (receiverId.equals(playerId)){
            incrementMap(senderStocks, stockValues);
        }
        updateMenu();
    }

    public void addMaterials(String playerId, MaterialsSet materialsSet) {
        if (senderId.equals(playerId)){
            senderMaterials.addMaterials(materialsSet);
        }else if (receiverId.equals(playerId)){
            receiverMaterials.addMaterials(materialsSet);
        }
        updateMenu();
    }

    private void incrementMap(Map<SeriesInfo, Float> map, Map<SeriesInfo, Float> increments) {
        for (SeriesInfo key : increments.keySet()) {
            incrementValue(map, key, increments.get(key));
        }
    }

    private void incrementValue(Map<SeriesInfo, Float> map, SeriesInfo key, Float increment) {
        map.put(key, increment + map.get(key));
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
