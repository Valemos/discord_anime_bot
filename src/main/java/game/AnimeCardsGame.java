package game;

import java.util.ArrayList;
import java.util.List;

public class AnimeCardsGame {
    GlobalCollection globalCollection = new GlobalCollection();
    private List<Player> players = new ArrayList<>();

    public AnimeCardsGame() {
    }

    public Player getPlayerById(String playerId) {
        return players.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst().orElse(null);
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)){
            players.add(player);
        }
    }

    public void addCard(CharacterCard card) {
        globalCollection.addCard(card);
    }

    public CharacterCard getCardById(String id) {
        return globalCollection.getCardById(id);
    }

    public void removeCardById(String id) {
        globalCollection.removeCardById(id);
    }
}

