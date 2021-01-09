package bot.commands.handlers.creator;

import bot.AccessLevel;
import bot.commands.CommandInfo;
import bot.commands.CommandParameters;
import bot.commands.arguments.ArgumentSettings;
import bot.commands.arguments.ArgumentSettingsBuilder;
import bot.commands.arguments.MessageArguments;
import bot.commands.arguments.StringArgument;
import bot.commands.handlers.BotCommandHandler;
import game.cards.CardStatsGlobal;
import game.cards.CharacterCardGlobal;

public class CreateGlobalCardHandler extends BotCommandHandler {

    public CreateGlobalCardHandler() {
        super(new CommandInfo("createcard", "cr"), AccessLevel.CREATOR);

        setArguments(
                ArgumentSettingsBuilder.getBuilder(commandInfo)
                    .addRequired(new StringArgument("name"))
                    .addRequired(new StringArgument("series"))
                    .addOptional(new StringArgument("url"))
                    .addOptional(new StringArgument("url2"))
                    .build()
        );
    }

    @Override
    public void handle(CommandParameters parameters) {
        MessageArguments args = parameters.messageArgs;
        CharacterCardGlobal new_card = new CharacterCardGlobal(
                args.get(0),
                args.get(1),
                args.get(2),
                new CardStatsGlobal());

        parameters.game.addCard(new_card);
    }
}
