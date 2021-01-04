import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class BotAnimeCards {

    JDA discord_api;
    GlobalCollection globalCollection;

    public BotAnimeCards() {
    }

    public boolean authenticate(String token) {
        try{
            discord_api = JDABuilder.createDefault(token).build();
            discord_api.awaitReady();
            return true;
        }
        catch (LoginException | InterruptedException e2){
            return false;
        }
    }
}
