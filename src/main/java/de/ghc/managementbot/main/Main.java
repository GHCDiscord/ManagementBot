package de.ghc.managementbot.main;

import de.ghc.managementbot.listener.JoinListener;
import de.ghc.managementbot.listener.MessageListener;
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
      JDA jda = new JDABuilder(AccountType.BOT).addListener(new MessageListener(), new JoinListener()).setToken("TOKEN").setGame(Game.of("Hackerz")).buildBlocking();
    } catch (LoginException | InterruptedException | RateLimitedException e) {
      e.printStackTrace();
    }
  }
}
