package de.ghc.managementbot.listener;

import de.ghc.managementbot.content.*;
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
      return new Guide(Guide.faq);
    } else if (command[0].equalsIgnoreCase("!addip") && command.length > 3) {
      AddIPsWithParams com = new AddIPsWithParams(event.getAuthor());
      Content.addUserAddIPWithParams(event.getAuthor(), com);
      return com;
    } else if (command[0].equalsIgnoreCase("!addip") && command.length <= 3) {
      AddIPWithQuestions com = new AddIPWithQuestions(event.getAuthor());
      Content.addUserAddIPWithQuestions(event.getAuthor(), com);
      return com;
    } else if (command[0].equalsIgnoreCase("!gilde") || command[0].equalsIgnoreCase("!guild")) {
      return new Guide(Guide.guild);
    } else if (command[0].equalsIgnoreCase("!taktik"))
      return new Guide(Guide.taktik);
    else if (command[0].equalsIgnoreCase("!register") || command[0].equalsIgnoreCase("!addAccount")) {
      return new AddUser();
    } else if (command[0].equalsIgnoreCase("!refresh")) {
      return new RefreshUser();
    } else if (command[0].equalsIgnoreCase("!stats") && command.length >= 2) {
      return new CountryStats();
    } else if (msg.equalsIgnoreCase("!topcountry")) {
      return new TopCountry();
    } else if (command[0].equalsIgnoreCase("!de") || command[0].equalsIgnoreCase("!english") || command[0].equalsIgnoreCase("!englisch") || command[0].equalsIgnoreCase("!en"))
      return new Guide(Guide.language);
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

    if (Content.getGhc() == null && event.getGuild() != null && event.getGuild().getName().equals("German Hackers Community")) {
      Content.setGhc(event.getGuild());
      new Thread(new ServerStatsThread(Content.getGhc(), 43200000)).start();
    }

    startCommand(event);
  }
}
