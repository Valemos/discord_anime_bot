package game.contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContractsManager {

    HashMap<String, AbstractContract> messageContractsMap = new HashMap<>();
    HashMap<String, List<AbstractContract>> userContractsMap = new HashMap<>();

    public ContractsManager() {
    }

    public void removeMessageContract(String messageId) {
        AbstractContract contract = messageContractsMap.remove(messageId);
        removeUserContract(contract.getSenderId(), contract);
        removeUserContract(contract.getReceiverId(), contract);
    }

    private void removeUserContract(String userId, AbstractContract contract) {
        List<AbstractContract> contracts = userContractsMap.getOrDefault(userId, null);
        if (contracts != null) {
            contracts.remove(contract);
        }
    }

    public void add(AbstractContract contract) {
        addUserContract(contract.getSenderId(), contract);
        addUserContract(contract.getReceiverId(), contract);
    }

    private void addUserContract(String userId, AbstractContract contract) {
        List<AbstractContract> contracts = userContractsMap.getOrDefault(userId, null);

        // TODO address issue with multiple inactive contracts
        if (contracts != null){
            contracts.add(contract);
        }else{
            userContractsMap.put(userId, new ArrayList<>(List.of(contract)));
        }
    }

    public <T extends AbstractContract> void addMessage(String messageId, T contract) {
        messageContractsMap.put(messageId, contract);
    }

    public <T extends AbstractContract> T getForMessage(Class<T> contractClass, String messageId) {

        AbstractContract contract = messageContractsMap.getOrDefault(messageId, null);

        if (contract != null && contract.getClass().isAssignableFrom(contractClass)) {
            return contractClass.cast(contract);
        }

        return null;
    }

    public <T extends AbstractContract> T getForUser(Class<T> contractClass, String userId) {
        List<AbstractContract> contracts = userContractsMap.getOrDefault(userId, null);

        if (contracts != null){
            // filter contracts and if abstract contract can be casted to given class,
            // return this contract
            AbstractContract contract = contracts.stream()
                    .filter(c -> c.getClass().isAssignableFrom(contractClass))
                    .findFirst().orElse(null);
            return contract != null ? contractClass.cast(contract) : null;
        }
        return null;
    }
}
