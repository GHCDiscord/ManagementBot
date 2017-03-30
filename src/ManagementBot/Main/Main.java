package ManagementBot.Main;

import ManagementBot.Listener.JoinListener;
import ManagementBot.Listener.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.impl.GameImpl;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

public class Main {
    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(AccountType.BOT).addListener(new MessageListener(), new JoinListener()).setToken("TOKEN HIER").setGame(new GameImpl("Hackerz", "", Game.GameType.DEFAULT)).buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }
    }
}
