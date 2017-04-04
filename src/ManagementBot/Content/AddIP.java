package ManagementBot.Content;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;

import static ManagementBot.Content.Content.checkIP;

public class AddIP extends Command{
    @Override
    void onMessageReceived(MessageReceivedEvent event) {
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
                    } else {
                        String[] desc = Arrays.copyOfRange(command, i, command.length);
                        String description = "";
                        for (String s : desc) {
                            description += s;
                        }
                        entry.setDescription(description);
                        break;
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //TODO Add IP to Database
    }
}
