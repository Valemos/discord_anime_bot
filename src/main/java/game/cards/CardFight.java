package game.cards;

import game.AnimeCardsGame;

import java.util.*;

public class CardFight {

    private final CardGlobal cardTrophy;
    private final Map<String, Float> playerPickDelay = new LinkedHashMap<>();
    private String winnerId = null;


    public CardFight(CardGlobal card) {
        cardTrophy = card;
    }

    public void fight(String id, float cardPickTime) {
        Float lastPickTime = playerPickDelay.put(id, cardPickTime);

        if(lastPickTime != null && lastPickTime < cardPickTime)
            playerPickDelay.put(id, lastPickTime);
    }

    public String findWinner() {
        if (playerPickDelay.size() <= 1){
            winnerId = playerPickDelay.keySet().stream().findFirst().orElse(null);
        }else{
            // TODO maybe change how winner should be found
            int randomIndex = new Random().nextInt(playerPickDelay.size());
            winnerId = new ArrayList<>(playerPickDelay.keySet()).get(randomIndex);
        }

        return winnerId;
    }

    public CardPersonal giveCardToWinner(AnimeCardsGame game) {
        if (winnerId != null){
            if (playerPickDelay.size() > 1){
                cardTrophy.getStats().incrementCardFights();
                game.getCardsGlobal().update(cardTrophy);
            }
            return game.pickPersonalCard(winnerId, cardTrophy, playerPickDelay.get(winnerId));
        }
        return null;
    }
}
