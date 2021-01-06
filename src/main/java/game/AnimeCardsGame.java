package game;

import bot.AccessLevel;
import game.cards.CharacterCard;
import game.cards.GlobalCollection;

import java.util.ArrayList;
import java.util.List;

public class AnimeCardsGame {
    GlobalCollection globalCollection = new GlobalCollection();
    private final List<Player> players = new ArrayList<>();

    public AnimeCardsGame() {
    }

    public Player getPlayerById(String playerId) {
        return players.stream()
                .filter(player -> player.getId().equals(playerId))
                .findFirst().orElse(null);
    }

    public void addPlayer(Player player) {
        removePlayer(player);
        players.add(player);
    }

    private void removePlayer(Player player) {
        players.removeIf((p) -> p.getId().equals(player.getId()));
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

    public GlobalCollection getGlobalCollection() {
        return globalCollection;
    }

    public Player createNewPlayer(String new_player_id) {
        Player player = new Player(new_player_id, AccessLevel.USER);
        addPlayer(player);
        return player;
    }
}

