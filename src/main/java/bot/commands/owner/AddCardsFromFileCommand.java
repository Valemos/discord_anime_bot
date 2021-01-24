package bot.commands.owner;

import bot.commands.AbstractCommand;
import bot.commands.AbstractCommandNoArguments;
import com.jagrosh.jdautilities.command.CommandEvent;
import game.AnimeCardsGame;
import game.cards.CardGlobal;
import net.dv8tion.jda.api.entities.Message;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddCardsFromFileCommand extends AbstractCommandNoArguments {

    // TODO test this class for correct file loading
    public AddCardsFromFileCommand(AnimeCardsGame game) {
        super(game);
    }

    @Override
    public void handle(CommandEvent event) {
        List<Message.Attachment> attachments = event.getMessage().getAttachments();
        if(attachments.size() > 0){
            Message.Attachment attachment = attachments.get(0);
            String fileExtension = attachment.getFileExtension();

            if (fileExtension != null && fileExtension.equals(".txt")){
                try {
                    File tempFile = File.createTempFile("cards-", ".tmp");
                    attachment.downloadToFile(tempFile).thenAccept(file -> {
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

    private String handleCreateCardsFile(AnimeCardsGame game, File file) {
        int errorMessageCounter = 0;
        StringBuilder errorMessage = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            AddCardCommand.Arguments addCardArgs = new AddCardCommand.Arguments();


            String line = reader.readLine();
            while (line != null) {

                try {
                    AbstractCommand.tryParseArguments(addCardArgs, line);

                    CardGlobal newCard = new CardGlobal(
                            addCardArgs.name,
                            addCardArgs.series,
                            addCardArgs.imageUrl
                    );
                    game.addCard(newCard);

                } catch (CmdLineException e) {
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
}
