package game.contract;

import game.AnimeCardsGame;
import game.ArmorItemPersonal;
import game.Player;
import game.cards.CardPersonal;
import game.materials.MaterialsSet;
import game.player_objects.StockValue;

import java.util.LinkedList;
import java.util.List;

public class MultiTradeContract extends AbstractContract {

    List<CardPersonal> cardsSent = new LinkedList<>();
    MaterialsSet materialsSent = new MaterialsSet();

    List<CardPersonal> cardsReceived = new LinkedList<>();
    MaterialsSet materialsReceived = new MaterialsSet();

    public MultiTradeContract(String senderId, String recipientId) {
        super(senderId, recipientId);
    }

    @Override
    public boolean finish(AnimeCardsGame game) {
        Player sender = getSender(game);
        Player recipient = getRecipient(game);

        for (CardPersonal card : cardsSent){
            recipient.addCard(card);
        }

        for (CardPersonal card : cardsReceived){
            sender.addCard(card);
        }

        sender.subtractMaterials(materialsSent);
        recipient.subtractMaterials(materialsReceived);

        sender.addMaterials(materialsReceived);
        recipient.addMaterials(materialsSent);
        
        return true;
    }

    @Override
    public String getMoreInfo() {
        return null;
    }

    public void addCards(List<CardPersonal> cards) {

    }

    public void addArmor(List<ArmorItemPersonal> armorItems) {

    }

    public void addStocks(List<StockValue> stockValues) {

    }
}
