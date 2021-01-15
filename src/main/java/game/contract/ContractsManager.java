package game.contract;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ContractsManager {

    HashMap<Class<? extends ContractInterface>, ContractsContainer> containersMap;

    public ContractsManager(List<Class<? extends AbstractContract>> contractClasses) {
        containersMap = new HashMap<>();
        for (Class<? extends AbstractContract> contractClass : contractClasses){
            containersMap.put(contractClass, new ContractsContainer());
        }
    }

    public void removeContract(Class<? extends ContractInterface> contractClass, String messageId) {
        ContractsContainer contractContainer = getContainer(contractClass);

        if (Objects.nonNull(contractContainer)){
            contractContainer.remove(messageId);
        }
    }

    public void add(String messageId,
                    Class<? extends ContractInterface> contractClass,
                    ContractInterface contract) {
        ContractsContainer contractsContainer = getContainer(contractClass);

        if (Objects.nonNull(contractsContainer)){
            contractsContainer.add(messageId, contract);
        }
    }

    public ContractInterface get(Class<? extends ContractInterface> contractClass, String messageId) {
        ContractsContainer contractsContainer = getContainer(contractClass);

        if (Objects.nonNull(contractsContainer)) {
            return contractsContainer.get(messageId);
        }

        return null;
    }

    private ContractsContainer getContainer(Class<? extends ContractInterface> contractClass) {
        return containersMap.getOrDefault(contractClass, null);
    }
}
