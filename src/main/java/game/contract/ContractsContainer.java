package game.contract;

import java.util.HashMap;

public class ContractsContainer {

    HashMap<String, ContractInterface> container = new HashMap<>();


    public void add(String messageId, ContractInterface contract) {
        container.put(messageId, contract);
    }

    public void remove(String messageId) {
        container.remove(messageId);
    }

    public ContractInterface get(String messageId) {
        return container.getOrDefault(messageId, null);
    }
}
