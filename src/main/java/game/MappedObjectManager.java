package game;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MappedObjectManager<A, B> {
    private final HashMap<A, B> playerStocksMap = new HashMap<>();
    private final Class<B> mappedObjectClass;

    public MappedObjectManager(Class<B> mappedObjectClass) {
        this.mappedObjectClass = mappedObjectClass;
    }

    protected void addElement(A key, B element) {
        playerStocksMap.put(key, element);
    }

    @NotNull
    public B getElement(A key) {
        B element = playerStocksMap.getOrDefault(key, null);
        if (element == null) {
            element = createNewElement();
            addElement(key, element);
        }
        return element;
    }

    private B createNewElement() {
        try {
            return mappedObjectClass.getConstructor(new Class<?>[0]).newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
