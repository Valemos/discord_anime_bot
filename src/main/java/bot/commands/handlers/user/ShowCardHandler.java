package bot.commands.handlers.user;

import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.arguments.ArgumentSettingsBuilder;
import bot.commands.arguments.IntegerArgument;
import bot.commands.handlers.BotCommandHandler;
import game.cards.CharacterCardGlobal;

public class ShowCardHandler extends BotCommandHandler {

    public ShowCardHandler() {
        super(new CommandInfo("showcard", "sc"));
        setArguments(
                ArgumentSettingsBuilder.getBuilder(commandInfo)
                        .addRequired(new IntegerArgument("card id"))
                        .build());
    }

    @Override
    public void handle(CommandParameters parameters) {
        int cardId = Integer.parseInt(parameters.messageArgs.get(0));
        CharacterCardGlobal card = parameters.game.getGlobalCardById(cardId);
        parameters.channel.sendMessage(card.getOneLineRepresentationString()).queue();
    }
}
