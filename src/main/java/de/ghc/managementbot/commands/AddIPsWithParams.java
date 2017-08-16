package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.AddIP;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

import static de.ghc.managementbot.content.Content.isVerified;


public class AddIPsWithParams extends AddIP implements Command {

  private boolean done = false;
  private IPEntry entry;
  private final User user;

  public AddIPsWithParams(User user) {
    this.user = user;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    new Thread(new DeleteMessageThread(30, event.getMessage())).start();
    String[] command = event.getMessage().getContent().split(" ");
    if (!done) {
      try {
        String IP = command[1];
        Member member = event.getMember();
        if (member == null) {
          member = Content.getGHCMember(event.getAuthor());
        }
        if (checkIP(IP)) {
          if (!isVerified(member) || (event.getGuild() != null && event.getTextChannel().getIdLong() != Data.Channel.hackersip)) {
            Content.deleteUserAddIP(user, this);
            return;
          }
          entry = new IPEntry(IP);
          for (int i = 2; i < command.length; i++) {
            if (command[i].equalsIgnoreCase("-n")) {
              i = setName(++i, command, entry);
            } else if (command[i].equalsIgnoreCase("-m")) {
              try {
                entry.setMiners(Integer.parseInt(command[++i]));
              } catch (NumberFormatException ignore) {
              }
            } else if (command[i].equalsIgnoreCase("-r") || command[i].equalsIgnoreCase("-rep")) {
              try {
                entry.setRepopulation(Integer.parseInt(command[++i]));
              } catch (NumberFormatException ignore) {
              }
            } else if (command[i].equalsIgnoreCase("-g")) {
              if (command[++i].length() == 3 || command[i].length() == 4) {
                entry.setGuildTag(command[i]);
              }
            } else {
              String[] desc = Arrays.copyOfRange(command, i, command.length);
              StringBuilder description = new StringBuilder().append(desc[0]);
              for (int j = 0; i < desc.length; i++) {
                description.append(" ").append(desc[j]);
              }
              entry.setDescription(description.toString());
              break;
            }
          }
        } else {
          event.getChannel().sendMessageFormat("Die IP %s ist nicht gültig!", IP).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
          Content.deleteUserAddIP(user, this);
          return;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        event.getChannel().sendMessage(String.format("Der Parameter %s hat keinen Wert erhalten", command[command.length - 1])).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
        Content.deleteUserAddIP(user, this);
        return;
      }
      entry.setUser(event.getAuthor());
      done = true;
      event.getChannel().sendMessage("Stimmen diese Daten?\nIP: " + entry.toString() + "\nSchreibe 'Ja' zum best\u00E4tigen").queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
     } else {
      Content.deleteUserAddIP(user, this);
      String msg = event.getMessage().getContent();
      if (msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("Yes") || msg.equalsIgnoreCase("j") || msg.equalsIgnoreCase("y")) {
        addEntryAndHandleResponse(entry, event.getChannel(), event.getAuthor());
      } else {
        event.getChannel().sendMessage("abgebrochen").queue(m -> new Thread(new DeleteMessageThread(10, m)).start());
        //TODO Add to Bot CI
      }
    }

  }

}
