package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.AddIP;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Strings;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

import static de.ghc.managementbot.content.Content.isVerified;


public class AddIPWithQuestions extends AddIP implements Command {

  private ArrayList<Message> messages;
  private TextChannel channel;
  private User user;
  private IPEntry entry;
  private Status status;

  public AddIPWithQuestions(User user) {
    status = Status.start;
    messages = new ArrayList<>();
    this.user = user;
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (channel == null)
      channel = event.getTextChannel();
    if (channel != null && !channel.equals(event.getChannel())) { // channel == null if private Message
      // channel switched
      channel.deleteMessages(messages).queue();
      messages = new ArrayList<>();
      channel = event.getTextChannel();
    }
    if (!messages.contains(event.getMessage())) {
      messages.add(event.getMessage());
    }
    String msg = event.getMessage().getContent();
    MessageChannel channel = event.getChannel(); //private Messages
    Member member = event.getMember();
    if (member == null) {
      member = Content.getGHCMember(event.getAuthor());
    }
    if (isVerified(member)) {
      switch (status) {
        case start:
          channel.sendMessage("Bitte nenne die IP: ").queue(messages::add);
          status = Status.IP;
          break;
        case IP:
          if (checkIP(msg)) {
            entry = new IPEntry(msg);
            status = Status.name;
            channel.sendMessage("Bitte nenne den Namen: ").queue(messages::add);
          } else {
            status = Status.unknown;
            onMessageReceived(event);
          }
          break;
        case name:
          entry.setName(msg);
          status = Status.miner;
          channel.sendMessage("Bitte nenne die Anzahl der Miner: ").queue(messages::add);
          break;
        case miner:
          try {
            entry.setMiners(Integer.parseInt(msg));
          } catch (NumberFormatException e) {
          }
          status = Status.reputation;
          channel.sendMessage("Bitte nenne jetzt die Rep: ").queue(messages::add);
          break;
        case reputation:
          try {
            entry.setRepopulation(Integer.parseInt(msg));
          } catch (NumberFormatException e) {
          }
          status = Status.guild;
          channel.sendMessage("Schreibe nun den Guild-Tag. Wenn er in keiner Gilde ist, schreibe n").queue(messages::add);
          break;
        case guild:
          if (msg.length() == 3 || msg.length() == 4) {
            entry.setGuildTag(msg);
          }
          channel.sendMessage("Stimmen diese Daten?\nIP: " + entry.getIP() + "\nName: " + entry.getName() + "\nMiner: " + entry.getMiners() + "\nReputation: " + entry.getRepopulation() + "\nGilde: " + entry.getGuildTag() + "\nSchreibe 'Ja' zum best\u00E4tigen.").queue(messages::add);
          status = Status.accept;
          break;
        case accept:
          if (msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("Yes") || msg.equalsIgnoreCase("j") || msg.equalsIgnoreCase("y")) {
            status = Status.accepted;
          } else {
            status = Status.unknown;
          }
          onMessageReceived(event);
          break;
        case accepted:
          Content.deleteUserAddIPWithQuestions(user, this);
          entry.setUser(user);
          addEntryAndHandleResponse(entry, channel, event.getAuthor());
          if (messages != null) {
            if (this.channel != null) {
              this.channel.deleteMessages(messages).queue();
            } else {
              for (Message message : messages) {
                message.delete().queue();
              }
            }
          }
          messages = null;
          break;
        case unknown:
          channel.sendMessage("abgebrochen").queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
          if (this.channel != null)
            this.channel.deleteMessages(messages);
          else
            messages.forEach(m -> m.delete().queue());
          Content.deleteUserAddIPWithQuestions(user, this);
          break;
      }
    }
  }

  private static enum Status {
    start, IP, name, miner, reputation, guild, accept, accepted, unknown
  }
}
