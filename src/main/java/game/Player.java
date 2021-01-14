package game;

import bot.CommandPermissions;
import game.cards.CardPersonal;
import game.cards.CardsPersonalManager;
import game.items.ItemGlobal;
import game.items.ItemsPersonalManager;
import game.items.MaterialsSet;
import game.squadron.Squadron;

public class Player {
    private final String id;
    private final CardsPersonalManager cardsManager;
    private final ItemsPersonalManager itemsPersonalManager;
    private final MaterialsSet materialsSet;
    private Squadron squadron;

    public Player(String id,
                  CardsPersonalManager cardsManager,
                  MaterialsSet materialsSet,
                  ItemsPersonalManager itemsPersonalManager) {
        this.id = id;
        this.cardsManager = cardsManager;
        this.materialsSet = materialsSet;
        this.itemsPersonalManager = itemsPersonalManager;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Player){
            return id.equals(((Player) obj).id);
        }
        return false;
    }

    public String getId() {
        return id;
    }

    public CardsPersonalManager getCardsManager() {
        return cardsManager;
    }

    public MaterialsSet getMaterials() {
        return materialsSet;
    }

    public void addCard(CardPersonal card) {
        cardsManager.addCard(id, card);
    }

    public ItemsPersonalManager getInventoryItems() {
        return itemsPersonalManager;
    }

    public void setSquadron(Squadron squadron) {
        this.squadron = squadron;
        squadron.setPlayerId(id);
    }

    public Squadron getSquadron() {
        return squadron;
    }

    public void addItem(ItemGlobal newItem) {
        itemsPersonalManager.addItem(id, newItem);
    }
}
