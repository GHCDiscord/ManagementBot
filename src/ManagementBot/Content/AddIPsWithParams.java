package ManagementBot.Content;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

import static ManagementBot.Content.Content.isVerified;

public class AddIPsWithParams extends AddIP {

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
                event.getChannel().sendMessage("Stimmen diese Daten?\nIP: " + entry.getIP() + "\nName: " + entry.getName() + "\nMiner: " + entry.getMiners() + "\nRepopulation: " + entry.getRepopulation() + "\nGuild: " + entry.getGuildTag()).queue(m -> new Thread(new DeleteMessageThread(100, m)).start());
            }
        } else {
            String msg = event.getMessage().getContent();
            if (msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("Yes") || msg.equalsIgnoreCase("j") || msg.equalsIgnoreCase("y"))
                addIPtoDB(entry);
            else
                event.getChannel().sendMessage("abgebrochen").queue(m -> new Thread(new DeleteMessageThread(10, m)).start());
            Content.deleteUserAddIPWithParams(user, this);
        }

    }

}
