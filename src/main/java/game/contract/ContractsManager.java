package game.contract;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ContractsManager {

    HashMap<String, AbstractContract> contractsMap = new HashMap<>();

    public ContractsManager() {
    }

    public void removeContract(String messageId) {
        contractsMap.remove(messageId);
    }

    public void add(String messageId, AbstractContract contract) {
        contractsMap.put(messageId, contract);
    }

    public <T extends AbstractContract> T getForMessage(Class<T> contractClass, String messageId) {

        AbstractContract contract = contractsMap.getOrDefault(messageId, null);

        if (contract != null && contract.getClass().isAssignableFrom(contractClass)) {
            return contractClass.cast(contract);
        }

        return null;
    }

    public <T extends AbstractContract> T getForUser(Class<T> contractClass, String userId) {
        AbstractContract abstractContract = contractsMap.values().stream()
                .filter(contract -> contract.isOwner(userId))
                .filter(contract -> contract.getClass().isAssignableFrom(contractClass))
                .findFirst().orElse(null);
        if (abstractContract != null){
            return contractClass.cast(abstractContract);
        }else{
            return null;
        }
    }
}
