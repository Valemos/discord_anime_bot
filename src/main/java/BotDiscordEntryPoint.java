import java.util.logging.Level;
import java.util.logging.Logger;

public class BotDiscordEntryPoint {
    public static void main(String[] args) {
        String bot_token = "Nzk1MzU0NDIzMTk3OTU4MTY0.X_IJhw.t59wu6ie-UZ7xPw8DW-HOxcl3sk";
        BotAnimeCards bot = new BotAnimeCards();

        if (!bot.authenticate(bot_token)){
            Logger.getGlobal().log(Level.SEVERE,"cannot use bot token");
        }
    }
}
