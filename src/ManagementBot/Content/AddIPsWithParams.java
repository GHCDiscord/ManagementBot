package ManagementBot.Content;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class AddIPsWithParams extends AddIP {

    private User user;
    boolean done = false;
    IPEntry entry;

    public AddIPsWithParams(User user) {
        this.user = user;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        new Thread(new DeleteMessageThread(120, event.getMessage())).start();
        String[] command = event.getMessage().getContent().split(" ");
        if (!done) {
            String IP = command[1];
            if (checkIP(IP)) {
                entry = new IPEntry(IP);
                for (int i = 2; i < command.length - 1; i++) {
                    try {
                        if (command[i].equalsIgnoreCase("-n")) {
                            entry.setName(command[++i]);
                        } else if (command[i].equalsIgnoreCase("-m")) {
                            entry.setMiners(Integer.parseInt(command[++i]));
                        } else if (command[i].equalsIgnoreCase("-r")) {
                            entry.setRepopulation(Integer.parseInt(command[++i]));
                        } else if (command[i].equalsIgnoreCase("-g")) {
                            i = setGuild(i, command, entry);
                        } else {
                            String[] desc = Arrays.copyOfRange(command, i, command.length);
                            StringBuilder description = new StringBuilder();
                            for (String s : desc) {
                                description.append(" ").append(s);
                            }
                            entry.setDescription(description.toString());
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                done = true;
                event.getChannel().sendMessage("Stimmen diese Daten?\nIP: " + entry.getIP() + "\nName: " + entry.getName() + "\nMiner: " + entry.getMiners() + "\nRepopulation: " + entry.getRepopulation() + "\nGuild: " + entry.getGuild()).queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
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
