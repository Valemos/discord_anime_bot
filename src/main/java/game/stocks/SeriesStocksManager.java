package game.stocks;

import game.Player;
import game.cards.CardPersonal;
import game.cards.CharacterInfo;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class SeriesStocksManager {

    HashMap<String, StocksPersonal> playerStocksMap;


    public float getCardStockValue(CardPersonal card) {
        return card.getPowerLevel();
    }

    public void addSeriesStock(String playerId, CharacterInfo cardInfo, float cardStockValue) {
        StocksPersonal playerStocks = getStocks(playerId);
        playerStocks.add(cardInfo.getSeriesTitle(), cardStockValue);
    }

    @NotNull
    public StocksPersonal getStocks(String playerId) {
        StocksPersonal playerStocks = playerStocksMap.getOrDefault(playerId, null);
        if (playerStocks == null){
            playerStocks = new StocksPersonal();
            playerStocksMap.put(playerId, playerStocks);
        }
        return playerStocks;
    }
}
