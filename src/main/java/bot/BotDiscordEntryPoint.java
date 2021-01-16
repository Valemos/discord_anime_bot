package bot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BotDiscordEntryPoint {
    public static void main(String[] args) throws IOException {
        String bot_token = loadBotTokenFile();
        BotAnimeCards bot = new BotAnimeCards();

        if (!bot.authenticate(bot_token)){
            return;
        }

        if (!bot.loadExternalSettings()){
            bot.loadDefaultGameSettings(bot.getGame());
        }
    }

    private static String loadBotTokenFile() throws IOException {
        Path filePath = Path.of("token.txt");
        return Files.readString(filePath);
    }
}
