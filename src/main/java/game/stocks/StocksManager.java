package game.stocks;

import game.cards.CardPersonal;

public class StocksManager extends game.MappedObjectManager<String, StocksPersonal> {

    public StocksManager() {
        super(StocksPersonal.class);
    }

    public float getCardStockValue(CardPersonal card) {
        return card.getPowerLevel();
    }

    public void addStockValue(String playerId, String seriesTitle, float stockValue) {
        StocksPersonal stocks = getElement(playerId);
        stocks.add(seriesTitle, stockValue);
    }
}
