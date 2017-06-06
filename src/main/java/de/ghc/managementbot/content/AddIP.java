package de.ghc.managementbot.content;

import de.ghc.managementbot.commands.AddIPInRange;
import de.ghc.managementbot.commands.AddIPWithQuestions;
import de.ghc.managementbot.commands.AddIPsWithParams;
import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.IMentionable;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public abstract class AddIP extends Database {

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

    protected static void addEntryAndHandleResponse(IPEntry entry, MessageChannel channel, IMentionable author) {
        String result = addIPtoDB(entry);
        if (result.equals("1")) {
            Content.getGhc().getTextChannelById(Data.hackersip).sendMessage(new MessageBuilder().append(author).append(" hat eine IP zur Datenbank hinzugef\u00FCgt").build()).queue();
            //Content.getGhc().getTextChannelById("269153131957321728").sendMessage(new MessageBuilder().append(author).append(Strings.getString(Strings.addIP_success_addedIp)).build()).queue();
        } else if (result.equals("ip already registered")) {
            channel.sendMessage("Diese IP existiert bereits in der Datenbank. Updates k\u00F6nnen momentan noch nicht mit dem Bot durchgef\u00FChrt werden. Bitte schreibe einem Kontributor, er wird sich dann darum k\u00FCmmern.").queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
            //channel.sendMessage(Strings.getString(Strings.addIP_error_ipAlreadyExsists)).queue();
        } else {
            channel.sendMessage("Es ist ein Fehler aufgetreten:\n" + result).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
            //channel.sendMessage(Strings.getString(Strings.addIP_error_exception)).queue();
        }
    }

    protected static int setName(int i, String[] command, IPEntry entry) {
        if (command[i].startsWith("{")) {
            StringBuilder name = new StringBuilder().append(command[i].replaceFirst("\\{", ""));
            while (!command[i].endsWith("}")) {
                name.append(" ").append(command[i]);
                i++;
            }
            name.append(command[i].replace("}", ""));
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
