package de.ghc.managementbot.main;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.content.Secure;
import de.ghc.managementbot.listener.LeaveListener;
import de.ghc.managementbot.listener.MessageListener;
import de.ghc.managementbot.threads.*;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;

public class Main {
  public static void main(String[] args) {
    try {
      JDA jda = new JDABuilder(AccountType.BOT).addEventListener(new MessageListener(), new LeaveListener()).setToken(Secure.DiscordToken).setGame(Game.of("Hackerz")).buildBlocking();
      Content.setJda(jda);
      Content.setGhc(jda.getGuildById(Data.GHC));
      new Thread(new ServerStatsThread(23)).start();
      new Thread(new TwitterThread()).start();
      new Thread(new YouTubeThread()).start();
      new Thread(new MarketAPIThread()).start();
      new Thread(new StartupThread(jda)).start();
      //Strings.start();
    } catch (LoginException | InterruptedException | RateLimitedException e) {
      e.printStackTrace();
    }
  }
}
