package bot.commands.owner;

import bot.commands.user.shop.MessageSenderTester;
import game.cards.CardGlobal;
import game.cards.CardPersonal;
import game.cards.CardsGlobalManager;
import game.cards.CharacterInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModifyCharacterCommandTest extends MessageSenderTester {

    CardsGlobalManager spyCardsGlobal;

    @BeforeEach
    void setUp() {
        spyCardsGlobal = spy(game().getCardsGlobal());
        doReturn(spyCardsGlobal).when(game()).getCardsGlobal();
    }

    @Test
    void testCannotModifyUnknownCard() {
        send("#modifycard 1234567890 name series url");
        verify(spyCardsGlobal, never()).updateCharacterInfo(any(), any(), any(), any());
    }

    @Test
    void testCharacterModified_andPersonalCardsUpdated() {
        CardPersonal card = getTesterCard(0);
        CardGlobal cardGlobal = game().getCardsGlobal().getByCharacter(card.getCharacterInfo());

        String name = "name";
        String series = "series";
        String url = "url";
        send("#modifycard " + cardGlobal.getId() + " " + name + " " + series + " " + url);

        CardPersonal newCard = getTesterCard(0);

        CardGlobal newCardGlobal = game().getCardsGlobal().getByCharacter(newCard.getCharacterInfo());

        assertEquals(newCard.getCharacterInfo(), cardGlobal.getCharacterInfo());
        assertEquals(newCard.getCharacterInfo(), newCardGlobal.getCharacterInfo());

        assertEquals(card.getId(), newCard.getId());
        assertEquals(name,      newCard.getCharacterInfo().getName());
        assertEquals(series,    newCard.getCharacterInfo().getSeriesName());
        assertEquals(url,       newCard.getCharacterInfo().getImageUrl());

        assertEquals(newCardGlobal.getId(), cardGlobal.getId());
        assertEquals(name,      newCardGlobal.getCharacterInfo().getName());
        assertEquals(series,    newCardGlobal.getCharacterInfo().getSeriesName());
        assertEquals(url,       newCardGlobal.getCharacterInfo().getImageUrl());
    }
}