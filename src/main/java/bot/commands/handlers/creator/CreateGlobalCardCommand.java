package bot.commands.handlers.creator;

import bot.PlayerAccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.handlers.AbstractCommand;
import bot.commands.parsing.ArgumentSettingsBuilder;
import bot.commands.parsing.MessageArguments;
import bot.commands.parsing.StringArgument;
import bot.commands.handlers.AbstractBotCommand;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardStatsGlobal;
import game.cards.CharacterCardGlobal;

public class CreateGlobalCardCommand extends AbstractCommand {

    public CreateGlobalCardCommand(AnimeCardsGame game) {
        super(game);
        name = "createcard";
        guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event) {
        CharacterCardGlobal new_card = new CharacterCardGlobal(
                args.get(0),
                args.get(1),
                args.get(2),
                new CardStatsGlobal());

        game.addCard(new_card);
        event.getChannel().sendMessage("new card added, card id is " + new_card.getCharacterInfo().getId()).queue();
    }
}
