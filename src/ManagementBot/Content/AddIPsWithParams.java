package ManagementBot.Content;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

public class AddIPsWithParams extends AddIP {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] command = event.getMessage().getContent().split(" ");
        String IP = command[1];
        if (checkIP(IP)) {
            IPEntry entry = new IPEntry(IP);
            for (int i = 2; i < command.length - 1; i++) {
                try {
                    if (command[i].equalsIgnoreCase("-n")) {
                        entry.setName(command[++i]);
                    } else if (command[i].equalsIgnoreCase("-m")) {
                        entry.setMiners(Integer.parseInt(command[++i]));
                    } else if (command[i].equalsIgnoreCase("-r")) {
                        entry.setRepopulation(Integer.parseInt(command[++i]));
                    } else if (command[i].equalsIgnoreCase("-g")) {
                        setGuild(i, command, entry);
                    }
                    else {
                        String[] desc = Arrays.copyOfRange(command, i, command.length);
                        StringBuilder description = new StringBuilder();
                        for (String s : desc) {
                            description.append(" ").append(s);
                        }
                        entry.setDescription(description.toString());
                        break;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            addIPtoDB(entry);
        }

    }

}
