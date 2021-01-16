package game.cards;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardDropManager {
    private final Map<String, List<CardGlobal>> cardDropsMap;

    public CardDropManager() {
        this.cardDropsMap = new HashMap<>();
    }

    public void add(String messageId, List<CardGlobal> cards) {
        cardDropsMap.put(messageId, cards);
    }

    public List<CardGlobal> get(String messageId) {
        return cardDropsMap.getOrDefault(messageId, null);
    }
}
