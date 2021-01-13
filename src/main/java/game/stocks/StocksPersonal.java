package game.stocks;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class StocksPersonal {

    private final HashMap<String, Float> seriesStockMap = new HashMap<>();

    public void add(String seriesTitle, float stockValue) {
        float previousValue = getStockValue(seriesTitle);
        setStockValue(seriesTitle, previousValue + stockValue);
    }

    public Float getStockValue(String seriesTitle) {
        return seriesStockMap.getOrDefault(seriesTitle.toLowerCase(), 0.f);
    }

    public void subtract(String seriesTitle, float stockValue){
        seriesTitle = seriesTitle.toLowerCase();

        float newValue = getStockValue(seriesTitle) + stockValue;
        if (newValue < 0) newValue = 0;

        setStockValue(seriesTitle, newValue);
    }

    private void setStockValue(String seriesTitle, float newValue) {
        seriesStockMap.put(seriesTitle.toLowerCase(), newValue);
    }

    public Set<String> getNames() {
        return seriesStockMap.keySet();
    }

    public String getStockStringValue(String name) {
        return name + ": " + getStockValue(name);
    }

    public String findByNamePart(String searchName) {
        for (String name : getNames()){
            if (name.contains(searchName)){
                return name;
            }
        }
        return null;
    }
}
