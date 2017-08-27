package de.ghc.managementbot.content;

import de.ghc.managementbot.commands.AddIPInRange;
import de.ghc.managementbot.commands.AddIPWithQuestions;
import de.ghc.managementbot.commands.AddIPsWithParams;
import de.ghc.managementbot.commands.UpdateIP;
import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.IMentionable;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.HashMap;
import java.util.Map;

public abstract class AddIP extends Database {

    private static Map<User, AddIP> userAddIP = new HashMap<>();

    public static Map<User, AddIP> getUserAddIP() {
        return userAddIP;
    }
    public static void addUserAddIP(User user, AddIP addIP) {
        userAddIP.put(user, addIP);
    }
    public static void deleteUserAddIP(User user, AddIP addIP) {
        userAddIP.remove(user, addIP);
    }

    protected static boolean checkIP(String s) {
        String[] numbers = s.split("\\.");
        if (numbers.length != 4)
            return false;
        try {
            for (String strg : numbers) {
                Integer.parseInt(strg);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected void addEntryAndHandleResponse(IPEntry entry, MessageChannel channel, IMentionable author) {
        String result = addIPtoDB(entry);
        if (result.equals("1")) {
            Content.getGhc().getTextChannelById(Data.Channel.hackersip).sendMessage(new MessageBuilder().append(author).append(" hat eine IP zur Datenbank hinzugef\u00FCgt").build()).queue();
            //Content.getGhc().getTextChannelById("269153131957321728").sendMessage(new MessageBuilder().append(author).append(Strings.getString(Strings.addIP_success_addedIp)).build()).queue();
        } else if (result.equals("ip already registered")) {
            channel.sendMessage("Diese IP existiert bereits in der Datenbank. Sollen diese Daten aktualisiert werden?").queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
            //channel.sendMessage(Strings.getString(Strings.addIP_error_ipAlreadyExsists)).queue();
            deleteUserAddIP(entry.getAddedBy(), this);
            UpdateIP uip = new UpdateIP(entry);
            UpdateIP.addUserUpdateIP(entry.getAddedBy(), uip);
        } else if (result.equals("discorduser not found")) {
            channel.sendMessage("Du hast noch keinen Account in unserer Datenbank. Erstelle einen mit `!register [Name im Spiel]`, z.B. `!register GHCBot`").queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
        } else {
            channel.sendMessage("Es ist ein Fehler aufgetreten:\n" + result).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
            //channel.sendMessage(Strings.getString(Strings.addIP_error_exception)).queue();
        }
    }

    protected static int setName(int i, String[] command, IPEntry entry) {
        if (command[i].startsWith("{")) {
            StringBuilder name = new StringBuilder().append(command[i].replaceFirst("\\{", ""));
            for (i++;!command[i].endsWith("}"); i++) {
                name.append(" ").append(command[i]);
            }
            name.append(" ").append(command[i].replace("}", ""));
            entry.setName(name.toString());
        } else {
            entry.setName(command[i]);
        }
        return i;
    }

    public static AddIP getAddIP(String msg, User user) {
        String[] data = msg.split(" ");
        if (!data[0].equalsIgnoreCase("!addIP"))
            throw new IllegalArgumentException("No !addIP Command");
        if (data.length < 3)
            return new AddIPWithQuestions(user);
        if (data[2].startsWith("-"))
            return new AddIPsWithParams(user);
        else
            return new AddIPInRange(user);
    }
}
