package game.contract;

public abstract class AbstractContract implements ContractInterface {

    protected String senderId;
    protected String recipientId;

    public AbstractContract(String senderId, String recipientId) {
        this.senderId = senderId;
        this.recipientId = recipientId;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getRecipientId() {
        return recipientId;
    }
}
