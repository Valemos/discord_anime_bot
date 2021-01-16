package game;

import game.cards.CardPersonal;
import game.cards.CardsPersonalManager;
import game.items.ItemGlobal;
import game.items.ItemsPersonalManager;
import game.items.MaterialsManager;
import game.items.MaterialsSet;
import game.squadron.Squadron;

public class Player {
    private final String id;
    private final CardsPersonalManager cardsManager;
    private final ItemsPersonalManager itemsPersonalManager;
    private final MaterialsManager materialsManager;
    private Squadron squadron;

    public Player(String id,
                  CardsPersonalManager cardsManager,
                  ItemsPersonalManager itemsPersonalManager,
                  MaterialsManager materialsManager) {
        this.id = id;
        this.cardsManager = cardsManager;
        this.itemsPersonalManager = itemsPersonalManager;
        this.materialsManager = materialsManager;
        materialsManager.createEmptyMaterials(id);
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
        return materialsManager.get(id);
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
