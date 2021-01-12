package game;

import bot.CommandAccessLevel;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.cards.CollectionGlobal;
import game.cards.CollectionPersonal;
import game.items.ItemGlobal;

import java.util.ArrayList;
import java.util.List;

public class AnimeCardsGame {
    CollectionGlobal collectionGlobal = new CollectionGlobal();

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

    public void addCard(CardGlobal card) {
        collectionGlobal.addCard(card);
    }

    public CardGlobal getGlobalCardById(int id) {
        return collectionGlobal.getCardById(id);
    }

    public CardGlobal getGlobalCardByName(String cardName) {
        return collectionGlobal.getCardByName(cardName);
    }

    public void removeCardById(int id) {
        collectionGlobal.removeCardById(id);
    }

    public CollectionGlobal getGlobalCollection() {
        return collectionGlobal;
    }

    public Player createNewPlayer(String playerId) {
        return createNewPlayer(playerId, CommandAccessLevel.USER);
    }

    public Player createNewPlayer(String playerId, CommandAccessLevel accessLevel) {
        Player player = new Player(playerId, accessLevel);
        addPlayer(player);
        return player;
    }

    public CardPersonal pickPersonalCardDelay(Player player, int globalCardId, float pickDelay) {
        CardGlobal cardGlobal = getGlobalCardById(globalCardId);
        CardPersonal cardPersonal = cardGlobal.getPersonalCardForDelay(player.getId(), pickDelay);
        player.addPersonalCard(cardPersonal);
        return cardPersonal;
    }

    public CollectionPersonal getPlayerCollection(Player player) {
        if (player != null){
            return player.getCollection();
        }
        return null;
    }

    public ItemGlobal addItem(ItemGlobal item) {
        // TODO add item system
        return item;
    }

    public boolean removeItemById(String itemId) {
        // TODO add delete from item system
        return false;
    }
}

