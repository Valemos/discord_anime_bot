package bot.commands.handlers.user;

import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.arguments.ArgumentSettingsBuilder;
import bot.commands.arguments.FullLineArgument;
import bot.commands.arguments.IntegerArgument;
import bot.commands.handlers.BotCommandHandler;
import game.cards.CharacterCardGlobal;

public class InspectCardHandler extends BotCommandHandler {

    public InspectCardHandler() {
        super(new CommandInfo("inspect", "i"));
        setArguments(
                ArgumentSettingsBuilder.getBuilder(commandInfo)
                        .addRequired(new IntegerArgument("card info"))
                        .build());
    }

    @Override
    public void handle(CommandParameters parameters) {
        int cardId = Integer.parseInt(parameters.messageArgs.get(0));
        CharacterCardGlobal card = parameters.game.getGlobalCardById(cardId);
        if (card != null){
            parameters.channel.sendMessage(card.getOneLineRepresentationString()).queue();
        }else{
            parameters.channel.sendMessage("card not found").queue();
        }
    }
}
