package de.ghc.managementbot.listener;

import de.ghc.managementbot.commands.*;
import de.ghc.managementbot.content.AddIP;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.entity.Command;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageListener extends ListenerAdapter {

  private static final List<Command> registeredCommands = new ArrayList<>(); //TODO Thread-save

  public static void startCommand(MessageReceivedEvent event) {
    getCommand(event).onMessageReceived(event);
  }

  private static Command getCommand(MessageReceivedEvent event) {
    String msg = event.getMessage().getContent();
    String[] command = msg.split(" ");

    if (AddIP.getUserAddIP().containsKey(event.getAuthor())) {
      return (Command) AddIP.getUserAddIP().get(event.getAuthor());
    } else if (UpdateIP.getUpdateIP().containsKey(event.getAuthor())) {
      return UpdateIP.getUpdateIP().get(event.getAuthor());
    }else if (command[0].equalsIgnoreCase("!addip") || command[0].equalsIgnoreCase("!a")) {
      return getAddIP(msg, event);
    }

    for (Command com : registeredCommands) {
      if (com.isCalled(event))
        return com.createCommand(event);
    }
    return Content.doNothing;
  }

  private static Command getAddIP(String msg, MessageReceivedEvent event) {
    AddIP addIP = AddIP.getAddIP(msg, event.getAuthor());
    if (addIP instanceof Command) {
      AddIP.addUserAddIP(event.getAuthor(), addIP);
      return (Command) addIP;
    }
    return Content.doNothing;
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
    if (event.getChannel().getIdLong() == Data.Channel.obeybot) {
      return;
    }
    startCommand(event);
  }

  public final void registerCommads() {
    registeredCommands.addAll(Arrays.asList(new AddRole(), new AddUser(), new AllTutorials(), new CountryStats(),
            new DeleteRole(), new Guide(null), new Help(), new RefreshUser(), new RegisterChannel(null),
            new Rules(), new ServerStats(), new Stats(), new TopCountry(), new TopGuilds(), new Verify(), new Version()
    ));
  }
}
