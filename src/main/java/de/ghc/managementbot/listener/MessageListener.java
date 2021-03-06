package de.ghc.managementbot.listener;

import de.ghc.managementbot.commands.*;
import de.ghc.managementbot.content.AddIP;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

  public static void startCommand(MessageReceivedEvent event) {
    getCommand(event).onMessageReceived(event);
  }

  private static Command getCommand(MessageReceivedEvent event) {
    String msg = event.getMessage().getContent();
    String[] command = msg.split(" ");

    if (Content.getUserAddIP().containsKey(event.getAuthor())) {
      return (Command) Content.getUserAddIP().get(event.getAuthor());
    } else if (Content.getUpdateIP().containsKey(event.getAuthor())) {
      return Content.getUpdateIP().get(event.getAuthor());
    } else if (msg.equalsIgnoreCase("!stats")) {
      return new Stats();
    } else if (msg.equalsIgnoreCase(".c3po")) { //verify
      return new Verify();
    } else if (msg.equalsIgnoreCase("!topGuilds")) {
      return new TopGuilds();
    } else if (command[0].equalsIgnoreCase("!help") || command[0].equalsIgnoreCase("!hilfe")) {
      return new Help();
    } else if (command[0].equalsIgnoreCase("!regeln") || command[0].equalsIgnoreCase("!rules")) {
      return new Rules();
    } else if (command[0].equalsIgnoreCase("!tut") || command[0].equalsIgnoreCase("!guide")) {
      return new Guide(Guide.faq);
    } else if (command[0].equalsIgnoreCase("!addip")) {
      return getAddIP(msg, event);
    } else if (command[0].equalsIgnoreCase("!gilde") || command[0].equalsIgnoreCase("!guild")) {
      return new Guide(Guide.guild);
    } else if (command[0].equalsIgnoreCase("!taktik")) {
      return new Guide(Guide.taktik);
    } else if (command[0].equalsIgnoreCase("!register") || command[0].equalsIgnoreCase("!addAccount")) {
      return new AddUser();
    } else if (command[0].equalsIgnoreCase("!refresh")) {
      return new RefreshUser();
    } else if (msg.equalsIgnoreCase("!stats db") || msg.equalsIgnoreCase("!stats ip")) {
      return new ServerStats();
    } else if (command[0].equalsIgnoreCase("!stats") && command.length >= 2) {
      return new CountryStats();
    } else if (msg.equalsIgnoreCase("!topcountry")) {
      return new TopCountry();
    } else if (command[0].equalsIgnoreCase("!de") || command[0].equalsIgnoreCase("!english") || command[0].equalsIgnoreCase("!englisch") || command[0].equalsIgnoreCase("!en")) {
      return new Guide(Guide.language);
    } else if (msg.equalsIgnoreCase("!latest") || msg.equalsIgnoreCase("!version")) {
      return new Version();
    } else if (msg.equalsIgnoreCase("!allTutorials") || msg.equalsIgnoreCase("!alltut") || msg.equalsIgnoreCase("!allTutorial")) {
      return new AllTutorials();
    }
    return e -> {};
  }

  private static Command getAddIP(String msg, MessageReceivedEvent event) {
    AddIP addIP = AddIP.getAddIP(msg, event.getAuthor());
    if (addIP instanceof Command) {
      Content.addUserAddIP(event.getAuthor(), addIP);
      return (Command) addIP;
    }
    return e -> {};
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    String msg = event.getMessage().getContent();
    //output
        if (event.getGuild() == null) {
			System.out.printf("[Priv][%s] %s: %s\n", event.getChannel().getName(), event.getAuthor().getName(), msg);
		} else {
			System.out.printf("[%s][%s] %s: %s \n", event.getGuild().getName(),
					event.getChannel().getName(), event.getAuthor().getName(), msg);
		}
    startCommand(event);
  }
}
