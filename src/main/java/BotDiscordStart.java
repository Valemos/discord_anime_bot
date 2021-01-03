import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;


public class BotDiscordStart {
    public static void main(String[] args) throws Exception
    {
        JDA jda = JDABuilder.createDefault("Nzk1MzU0NDIzMTk3OTU4MTY0.X_IJhw.t59wu6ie-UZ7xPw8DW-HOxcl3sk").build();
        System.out.println(jda.getStatus().toString());
    }
}
