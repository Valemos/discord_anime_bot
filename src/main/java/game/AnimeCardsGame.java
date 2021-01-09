package game;

import bot.AccessLevel;
import game.cards.CharacterCardGlobal;
import game.cards.CharacterCardPersonal;
import game.cards.GlobalCollection;
import game.cards.PersonalCollection;

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

    public void addCard(CharacterCardGlobal card) {
        globalCollection.addCard(card);
    }

    public CharacterCardGlobal getGlobalCardById(int id) {
        return globalCollection.getCardById(id);
    }

    public void removeCardById(int id) {
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

    public CharacterCardPersonal pickPersonalCardWithDelay(Player player, int globalCardId, float pickDelay) {
        CharacterCardGlobal cardGlobal = getGlobalCardById(globalCardId);
        return cardGlobal.getPersonalCardForPickDelay(player.getId(), pickDelay);
    }

    public PersonalCollection getPlayerCollection(Player player) {
        return player.getCollection();
    }
}

