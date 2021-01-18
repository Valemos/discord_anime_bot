package game;

import game.cards.CardDropManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MappedObjectManager<K, V> {
    private final HashMap<K, V> elementsMap = new HashMap<>();
    private final Class<V> mappedObjectClass;

    public MappedObjectManager(Class<V> mappedObjectClass) {
        this.mappedObjectClass = mappedObjectClass;
    }

    protected void addElement(K key, V element) {
        elementsMap.put(key, element);
    }

    public synchronized void removeElement(K key) {
        elementsMap.remove(key);
    }

    public synchronized boolean isElementExists(K key) {
        return elementsMap.containsKey(key);
    }

    public synchronized V getElement(K key) {
        return elementsMap.getOrDefault(key, null);
    }

    @NotNull
    public V getElementOrCreate(K key) {
        V element = getElement(key);
        if (element == null) {
            element = createNewElement();
            addElement(key, element);
        }
        return element;
    }

    private V createNewElement() {
        try {
            return mappedObjectClass.getConstructor(new Class<?>[0]).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
