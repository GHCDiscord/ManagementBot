package de.ghc.managementbot.listener;

import de.ghc.managementbot.content.AddIPWithQuestions;
import de.ghc.managementbot.content.AddIPsWithParams;
import de.ghc.managementbot.content.AddUser;
import de.ghc.managementbot.content.Command;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.CountryStats;
import de.ghc.managementbot.content.GuildsGuide;
import de.ghc.managementbot.content.Help;
import de.ghc.managementbot.content.RefreshUser;
import de.ghc.managementbot.content.Rules;
import de.ghc.managementbot.content.ServerStatsThread;
import de.ghc.managementbot.content.Stats;
import de.ghc.managementbot.content.TopCountry;
import de.ghc.managementbot.content.TopGuilds;
import de.ghc.managementbot.content.Tutorial;
import de.ghc.managementbot.content.Verify;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

  public static void startCommand(MessageReceivedEvent event) {
    getCommand(event).onMessageReceived(event);
  }

  private static Command getCommand(MessageReceivedEvent event) {
    String msg = event.getMessage().getContent();
    String[] command = msg.split(" ");

    if (Content.getUserAddIPWithQuestionsMap().containsKey(event.getAuthor())) {
      return Content.getUserAddIPWithQuestionsMap().get(event.getAuthor());
    } else if (Content.getUserAddIPsWithParamsMap().containsKey(event.getAuthor())) {
      return Content.getUserAddIPsWithParamsMap().get(event.getAuthor());
    } else if (msg.equalsIgnoreCase("!stats")) {
      return new Stats();
    } else if (msg.equalsIgnoreCase(".c3po")) { //verify
      return new Verify();
    } else if (msg.equalsIgnoreCase("!topGuilds")) {
      return new TopGuilds();
    } else if (command[0].equalsIgnoreCase("!help")) {
      return new Help();
    } else if (command[0].equalsIgnoreCase("!regeln") || command[0].equalsIgnoreCase("!rules")) {
      return new Rules();
    } else if (command[0].equalsIgnoreCase("!tut") || command[0].equalsIgnoreCase("!guide")) {
      return new Tutorial();
    } else if (command[0].equalsIgnoreCase("!addip") && command.length > 3) {
      AddIPsWithParams com = new AddIPsWithParams(event.getAuthor());
      Content.addUserAddIPWithParams(event.getAuthor(), com);
      return com;
    } else if (command[0].equalsIgnoreCase("!addip") && command.length <= 3) {
      AddIPWithQuestions com = new AddIPWithQuestions(event.getAuthor());
      Content.addUserAddIPWithQuestions(event.getAuthor(), com);
      return com;
    } else if (command[0].equalsIgnoreCase("!gilde") || command[0].equalsIgnoreCase("!guild")) {
      return new GuildsGuide();
    } /*else if (command[0].equalsIgnoreCase("!taktik"))
            return new Taktik(); */ else if (command[0].equalsIgnoreCase("!register") || command[0].equalsIgnoreCase("!addAccount")) {
      return new AddUser();
    } else if (command[0].equalsIgnoreCase("!refresh")) {
      return new RefreshUser();
    } else if (command[0].equalsIgnoreCase("!stats") && command.length >= 2) {
      return new CountryStats();
    } else if (msg.equalsIgnoreCase("!topcountry")) {
      return new TopCountry();
    }
    return new Command() {
      @Override
      public void onMessageReceived(MessageReceivedEvent event) {
      }
    };
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    String msg = event.getMessage().getContent();
    //output
        /*if (event.getGuild() == null) {
			System.out.printf("[Priv][%s] %s: %s\n", event.getChannel().getName(), event.getAuthor().getName(), msg);
		} else {
			System.out.printf("[%s][%s] %s: %s \n", event.getGuild().getName(),
					event.getChannel().getName(), event.getAuthor().getName(), msg);
		} */

    if (Content.getGhc() == null && event.getGuild() != null && event.getGuild().getName().equals("German Hackers Community")) {
      Content.setGhc(event.getGuild());
      new Thread(new ServerStatsThread(Content.getGhc(), 2592000)).start();
    }

    startCommand(event);
  }
}
