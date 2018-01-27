package de.ghc.managementbot.content;

import de.ghc.managementbot.commands.AddIPInRange;
import de.ghc.managementbot.commands.AddIPWithQuestions;
import de.ghc.managementbot.commands.AddIPsWithParams;
import de.ghc.managementbot.commands.UpdateIP;
import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import org.json.JSONObject;

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
        return s.matches("^(([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))\\.){3}([0-9]|[1-9][0-9]|1([0-9][0-9])|2([0-4][0-9]|5[0-5]))$");
    }

    protected void addEntryAndHandleResponse(IPEntry entry, MessageChannel channel, User author) {
        if (Content.isStaff(Content.getGHCMember(author)))
            entry.setUpdate(true);
        JSONObject result = addIPtoDB(entry);
        handleAddIPResponse(entry, result, channel, author);
    }

    private void handleAddIPResponse(IPEntry entry, JSONObject result, MessageChannel channel, User author) {
        if (!result.getBoolean("error")) {
            sendAddedIP(Content.getGHCMember(author));
        } else if (result.has("msgToken")) {
            Content.getGhc().getTextChannelById(Data.Channel.botLog).sendMessage("Token-Fehler: " + result.getString("msgToken")).queue();
        } else if (result.has("msgDiscord")) {
            if (result.getString("msgDiscord").equals("Discord User nicht gefunden!")) {
                channel.sendMessage("Du hast noch keinen Account in unserer Datenbank. Erstelle einen mit `!register [Name im Spiel]`, z.B. `!register GHCBot`").queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
            } else {
                channel.sendMessage("Es ist ein Fehler aufgetreten:\n" + result.getString("msgDiscord")).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
            }
        } else if (result.has("msgIP")) {
            if (result.getString("msgIP").equals("IP bereits vorhanden!")) {
                if (result.has("msgName") && result.getString("msgName").equals("Spielername bereits vorhanden!")) {
                    //IP und Name existieren -> IP updaten
                    channel.sendMessage("Diese IP existiert bereits in der Datenbank. Sollen diese Daten aktualisiert werden?").queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
                    deleteUserAddIP(entry.getAddedBy(), this);
                    UpdateIP uip = new UpdateIP(entry, generateIpEntry0(result.getJSONArray("IPID").getJSONObject(0)));
                    UpdateIP.addUserUpdateIP(entry.getAddedBy(), uip);
                } else if (!result.has("msgName")) {
                    channel.sendMessage("Diese IP exitstiert bereits in der Datenbank, aber unter einem anderen Namen. Bitte wende dich an einen Kontributor!").queue();
                } else {
                    //Anderer Fehler bei Name
                    channel.sendMessage("Es ist ein Fehler aufgetreten:\n" + result.getString("msgName")).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
                }
            } else if (result.getString("msgIP").equals("IP ungültig!"))  {
                channel.sendMessage("Die angegebene IP ist ungültig!").queue();
            }
        } else if (result.has("msgName")) {
            if (result.getString("msgName").equals("Spielername bereits vorhanden!"))
                channel.sendMessage("Es gibt bereits einen Eintrag mit diesem Namen, aber nicht mit dieser IP. Bitte wende dich an einen Kontributor.").queue();
            else
                channel.sendMessage("Es ist ein Fehler aufgetreten:\n" + result.getString("msgName")).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
        } else if (result.has("")) {

        }
        else {
            channel.sendMessage("Es ist ein Fehler aufgetreten:\n" + result).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
            //channel.sendMessage(Strings.getString(Strings.addIP_error_exception)).queue();
        }
    }

    public static final void sendAddedIP(Member addedBy) {
        Content.getGhc().getTextChannelById(Data.Channel.de_hackersip).sendMessage(new MessageBuilder().append(addedBy).append(" hat eine IP zur Datenbank hinzugef\u00FCgt").build()).queue();
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
        if (data.length < 3)
            return new AddIPWithQuestions(user);
        if (data[2].startsWith("-"))
            return new AddIPsWithParams(user);
        else
            return new AddIPInRange(user);
    }
}
