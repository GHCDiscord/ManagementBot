package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.AddIP;
import de.ghc.managementbot.content.Content;
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
      String IP = command[1];
      Member member = event.getMember();
      if (member == null) {
        member = Content.getGHCMember(event.getAuthor());
      }
      if (checkIP(IP) && isVerified(member)) {
        if (event.getGuild() != null && !event.getTextChannel().equals(event.getGuild().getTextChannelById("269153131957321728")))
          return;
        entry = new IPEntry(IP);
        try {
          for (int i = 2; i < command.length; i++) {
            if (command[i].equalsIgnoreCase("-n")) {
              entry.setName(command[++i]);
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
              StringBuilder description = new StringBuilder();
              for (String s : desc) {
                description.append(" ").append(s);
              }
              entry.setDescription(description.toString());
              break;
            }
          }
        } catch (ArrayIndexOutOfBoundsException e) {
          event.getChannel().sendMessage(String.format("Der Parameter %s hat kein Argument erhalten!", command[command.length - 1])).queue();
          //event.getChannel().sendMessage(Strings.getString(Strings.addIP_error_noParameter).replace("$[param]", command[command.length - 1])).queue();
        }
        entry.setUser(event.getAuthor());
        done = true;
        event.getChannel().sendMessage("Stimmen diese Daten?\nIP: " + entry.getIP() + "\nName: " + entry.getName() + "\nMiner: " + entry.getMiners() + "\nReputation: " + entry.getRepopulation() + "\nGilde: " + entry.getGuildTag() + "\nBeschreibung: " + entry.getDescription() + "\nSchreibe 'Ja' zum best\u00E4tigen").queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
        //event.getChannel().sendMessage(Strings.getString(Strings.addIP_confirm_correctDataParamsAnswer).replace()) //TODO
      }
    } else {
      Content.deleteUserAddIPWithParams(user, this);
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
