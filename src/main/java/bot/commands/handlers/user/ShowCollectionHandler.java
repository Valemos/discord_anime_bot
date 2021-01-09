package bot.commands.handlers.user;

import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.arguments.ArgumentSettingsBuilder;
import bot.commands.arguments.IntegerArgument;
import bot.commands.arguments.MessageArguments;
import bot.commands.arguments.StringArgument;
import bot.commands.handlers.BotCommandHandler;
import game.Player;
import game.cards.CharacterCardPersonal;
import game.cards.PersonalCollection;

public class ShowCollectionHandler extends BotCommandHandler {

    public ShowCollectionHandler() {
        super(new CommandInfo("collection", "c"));
        setArguments(
                ArgumentSettingsBuilder.getBuilder(commandInfo)
                        .addOptional(new StringArgument("userId"))
                        .addOptional(new StringArgument("userId"))
                        .build()
        );
    }

    @Override
    public void handle(CommandParameters parameters) {
        MessageArguments args = parameters.messageArgs;

        Player player;
        String playerId = args.get(0);
        if (playerId == null){
            player = parameters.player;
        }else{
            player = parameters.game.getPlayerById(playerId);
        }

        PersonalCollection collection = parameters.game.getPlayerCollection(player);

        StringBuilder b = new StringBuilder();
        b.append("User collection");
        int counter = 1;
        for (CharacterCardPersonal card : collection.getCards()){
            b.append('\n').append(counter++).append('.');
            b.append(card.getOneLineRepresentationString());
        }

        parameters.channel.sendMessage(b.toString()).queue();
    }
}
