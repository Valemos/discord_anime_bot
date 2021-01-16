package bot.commands.user;

import bot.commands.AbstractCommand;
import bot.commands.SortingType;
import bot.commands.arguments.MenuPageArguments;
import bot.menu.SimpleMenuCreator;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;

import java.util.List;
import java.util.stream.Collectors;

public class TopCharactersCommand extends AbstractCommand<MenuPageArguments> {

    public TopCharactersCommand(AnimeCardsGame game) {
        super(game, MenuPageArguments.class);
        name = "top";
        guildOnly = true;
    }

    @Override
    public void handle(CommandEvent event) {
        List<CardGlobal> allCards = game.getCardsGlobalManager().getCardsSorted(List.of(SortingType.POWER));

        SimpleMenuCreator.showMenuForCardsTop(allCards, event, game, commandArgs.pageNumber);
    }
}
