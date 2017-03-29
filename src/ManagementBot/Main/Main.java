package ManagementBot.Main;

import ManagementBot.Listener.MessageListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

/**
 * Created by Schule on 29.03.2017.
 */
public class Main {
    public static void main(String[] args) {
        try {
            JDA jda = new JDABuilder(AccountType.BOT).addListener(new MessageListener()).setToken("TOKEN HIER").buildBlocking();
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (RateLimitedException e) {
            e.printStackTrace();
        }
    }
}
