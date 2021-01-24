package bot.commands.user.shop;

import bot.BotMessageSenderMock;
import game.AnimeCardsGame;
import game.Player;
import game.cards.CardPersonal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.security.auth.login.LoginException;

public class MessageSenderTester {
    protected static BotMessageSenderMock sender;

    @BeforeAll
    static void initializeSender() throws LoginException {
        sender = new BotMessageSenderMock();
    }

    @BeforeEach
    void resetSender() {
        sender.reset();
    }

    protected Player tester() {
        return sender.tester();
    }

    protected AnimeCardsGame game() {
        return sender.getGame();
    }

    protected void send(String message){
        sender.send(message);
    }

    protected void sendAndCapture(String message){
        sender.sendAndCaptureMessage(message);
    }

    protected void sendAndCapture(String message, String userId, String messageId){
        sender.sendAndCaptureMessage(message, userId, messageId);
    }

    protected void send(String message, String userId, String messageId){
        sender.send(message, userId, messageId);
    }

    protected CardPersonal getTesterCard(int index) {
        return tester().getCards().get(index);
    }
}
