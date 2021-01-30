package bot.commands.owner;

import bot.commands.AbstractCommand;
import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import net.dv8tion.jda.api.entities.Message;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Localizable;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCardsFromFileCommand extends AbstractCommandNoArguments {

    public AddCardsFromFileCommand(AnimeCardsGame game) {
        super(game);
        name = "cardsfile";
        ownerCommand = true;
        guildOnly = false;
        help = """
                load .txt file as attachment to command message.
                it must contain lines in format of `addcard` command
                <name> <series> <url>
                any repeated cards by character name and series name will be skipped""";
    }

    @Override
    public void handle(CommandEvent event) {
        List<Message.Attachment> attachments = event.getMessage().getAttachments();
        if(attachments.size() > 0){
            Message.Attachment attachment = attachments.get(0);
            String fileExtension = attachment.getFileExtension();

            if (fileExtension != null && fileExtension.equals("txt")){
                try {
                    File tempFile = File.createTempFile("cards-", ".tmp");
                    attachment.downloadToFile(tempFile).thenAccept(
                    file -> {
                        String message = handleCreateCardsFile(game, file);
                        if (message != null) sendMessage(event, message);
                    }).exceptionally(t ->{
                        Logger.getGlobal().log(Level.SEVERE, t.getMessage());
                        return null;
                    });

                    tempFile.delete();

                } catch (IOException e) {
                    sendMessage(event,"System error: cannot create temp file to download attachment");
                    Logger.getGlobal().log(Level.SEVERE, e.getMessage());
                }
            }else{
                sendMessage(event, "incorrect file extension");
            }

        }else{
            sendMessage(event,
                    """
                            ```please provide a .txt file as attachment to command
                            it must contain lines with following format:
                            `<character-name> <series-name> <url-to-character-image>`
                            ```
                            """);
        }
    }

    protected String handleCreateCardsFile(AnimeCardsGame game, File file) {
        int errorMessageCounter = 0;
        StringBuilder errorMessage = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            AddCardCommand.Arguments addCardArgs = new AddCardCommand.Arguments();

            String line = reader.readLine();
            while (line != null) {

                try {
                    handleFileLine(game, addCardArgs, line);

                } catch (CmdLineException | InvalidArgumentsException e) {
                    errorMessageCounter++;
                    if (errorMessageCounter <= 10) {
                        errorMessage.append(e.getMessage()).append('\n');
                    }
                }

                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            Logger.getGlobal().log(Level.SEVERE, e.getMessage());
        }

        if (errorMessageCounter > 10){
            errorMessage.append("and ").append(errorMessageCounter - 10).append(" more");
        }

        return errorMessage.isEmpty() ? null : errorMessage.toString();
    }

    protected static void handleFileLine(AnimeCardsGame game, AddCardCommand.Arguments addCardArgs, String line) throws CmdLineException, InvalidArgumentsException {
        CmdLineParser commandParser = new CmdLineParser(addCardArgs);
        AbstractCommand.tryParseArguments(commandParser, line);

        CardGlobal newCard = new CardGlobal(
                addCardArgs.name,
                addCardArgs.series,
                addCardArgs.imageUrl
        );

        if(!game.addCard(newCard)){
            throw new InvalidArgumentsException("cannot add card \"" + (line.length() < 50 ? line : line.substring(0, 50) + "...") + '"');
        }
    }

    public static class InvalidArgumentsException extends Exception {
        public InvalidArgumentsException(String message) {
            super(message);
        }
    }
}
