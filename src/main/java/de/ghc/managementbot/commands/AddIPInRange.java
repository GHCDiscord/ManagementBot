package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.AddIP;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Strings;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.IPEntry;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AddIPInRange extends AddIP implements Command {

    private boolean done;
    private final User user;
    private IPEntry entry;

    public AddIPInRange(User user) {
        done = false;
        this.user = user;
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        new Thread(new DeleteMessageThread(30,event.getMessage())).start();
        Member member = event.getMember();
        if (member == null)
            member = Content.getGHCMember(event.getAuthor());
        if ((Content.isVerified(member) && event.getTextChannel() != null && event.getTextChannel().equals(event.getGuild().getTextChannelById(269153131957321728L))) || (Content.isVerified(member) && event.getChannel().getType().equals(ChannelType.PRIVATE))) {
            if (!done) {
                String[] data = event.getMessage().getContent().split(" ");
                entry = setupEntry(data);
                if (entry != null) {
                    entry.setUser(user);
                    event.getChannel().sendMessage(Strings.getString(Strings.addIP_confirm_correctDataParamsAnswer)
                            .replace("$[IP]", entry.getIP()).replace("$[name]", entry.getName()).replace("$[miner]", entry.getMiners() + "").replace("$[rep]", entry.getRepopulation() + "").replace("$[guild]", entry.getGuildTag()).replace("$[desc]", entry.getDescription())
                    ).queue(m -> new Thread(new DeleteMessageThread(60, m)).start());
                    done = true;
                } else {
                    Content.deleteUserAddIPInRange(user, this);
                    AddIPWithQuestions command = new AddIPWithQuestions(user);
                    Content.addUserAddIPWithQuestions(user, command);
                    event.getChannel().sendMessage("Es ist ein Fehler bei der Verarbeitung deiner Eingaben aufgetreten. Bitte gebe die Informationen einzlen ein: ").queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
                    command.onMessageReceived(event);
                }
            } else {
                Content.deleteUserAddIPInRange(user, this);
                String msg = event.getMessage().getContent();
                if (msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("Yes") || msg.equalsIgnoreCase("j") || msg.equalsIgnoreCase("y")) {
                    addEntryAndHandleResponse(entry, event.getChannel(), event.getAuthor());
                } else {
                    event.getChannel().sendMessage("abgebrochen").queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
                }

            }
        } else {
            Content.deleteUserAddIPInRange(user, this);
        }
    }

    private IPEntry setupEntry(String[] data) {
        if (data.length > 1 && checkIP(data[1])) {
            IPEntry entry = new IPEntry(data[1]);
            if (data.length < 3)
                return entry;
            entry.setName(data[2]);
            if (data.length < 4)
                return entry;
            try {
                entry.setRepopulation(Integer.parseInt(data[3]));
            } catch (NumberFormatException ignore) {
            }
            if (data.length < 5)
                return entry;
            try {
                entry.setMiners(Integer.parseInt(data[4]));
            } catch (NumberFormatException ignore) {
            }
            if (data.length < 6)
                return entry;
            if (data[5].length() == 3 || data[5].length() == 4)
                entry.setGuildTag(data[5]);
            StringBuilder builder = new StringBuilder();
            for (int i = 6; i < data.length; i++) {
                builder.append(data[i]).append(" ");
            }
            entry.setDescription(builder.toString());
            return entry;
        }
        return null;
    }
}
