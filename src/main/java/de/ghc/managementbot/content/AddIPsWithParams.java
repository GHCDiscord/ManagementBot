package de.ghc.managementbot.Content;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

import static ManagementBot.Content.Content.isVerified;

public class AddIPsWithParams extends AddIP implements Command{

    private User user;
    boolean done = false;
    IPEntry entry;

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
            if (member == null)
                member = Content.getGHCMember(event.getAuthor());
            if (checkIP(IP) && isVerified(member)) {
                if (event.getGuild() != null && !event.getTextChannel().equals(event.getGuild().getTextChannelById("269153131957321728")))
                    return;
                entry = new IPEntry(IP);
                for (int i = 2; i < command.length - 1; i++) {
                    if (command[i].equalsIgnoreCase("-n")) {
                        entry.setName(command[++i]);
                    } else if (command[i].equalsIgnoreCase("-m")) {
                        try {
                            entry.setMiners(Integer.parseInt(command[++i]));
                        } catch (NumberFormatException ignore) {}
                    } else if (command[i].equalsIgnoreCase("-r")) {
                        try {
                            entry.setRepopulation(Integer.parseInt(command[++i]));
                        } catch (NumberFormatException ignore) {}
                    } else if (command[i].equalsIgnoreCase("-g")) {
                        if(command[++i].length() == 3)
                            entry.setGuildTag(command[i]);
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
                entry.setUser(event.getAuthor());
                done = true;
                event.getChannel().sendMessage("Stimmen diese Daten?\nIP: " + entry.getIP() + "\nName: " + entry.getName() + "\nMiner: " + entry.getMiners() + "\nReputation: " + entry.getRepopulation() + "\nGilde: " + entry.getGuildTag() + "\nBeschreibung: " + entry.getDescription() + "\nSchreibe 'Ja' zum bestätigen").queue(m -> new Thread(new DeleteMessageThread(100, m)).start());
            }
        } else {
            Content.deleteUserAddIPWithParams(user, this);
            String msg = event.getMessage().getContent();
            if (msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("Yes") || msg.equalsIgnoreCase("j") || msg.equalsIgnoreCase("y")) {
                String result = addIPtoDB(entry);
                    if (result.equals("1")) {
                        event.getGuild().getTextChannelById("269153131957321728").sendMessage(new MessageBuilder().append(event.getAuthor()).append(" hat eine IP zur Datenbank hinzugefügt").build()).queue();
                    } else if (result.equals("ip already registered"))
                        event.getChannel().sendMessage("Diese IP existiert bereits in der Datenbank. Updates können momentan noch nicht mit dem Bot durchgeführt werden. Bitte schreibe einem Kontributor, er wird sich dann darum kümmern.").queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
                    else
                        event.getChannel().sendMessage("Es ist ein Fehler aufgetreten:\n" + result).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
            } else
                event.getChannel().sendMessage("abgebrochen").queue(m -> new Thread(new DeleteMessageThread(10, m)).start());
        }

    }

}
