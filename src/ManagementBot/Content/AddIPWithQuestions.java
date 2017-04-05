package ManagementBot.Content;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;


public class AddIPWithQuestions extends AddIP {

    IPEntry entry;
    ArrayList<Message> messages;
    User user;

    private enum Status {
        start, IP, name, miner, repupulation, accept, accepted, unknown
    }
    private Status status;

    public AddIPWithQuestions(User user) {
        status = Status.start;
        messages = new ArrayList<>();
        this.user = user;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!messages.contains(event.getMessage()))
            messages.add(event.getMessage());
        String msg = event.getMessage().getContent();
        TextChannel channel = event.getTextChannel();

        switch (status) {
            case start:
                channel.sendMessage("Bitte nenne die IP: ").queue(m -> messages.add(m));
                status = Status.IP;
                break;
            case IP:
                if (checkIP(msg)) {
                    entry = new IPEntry(msg);
                    status = Status.name;
                    channel.sendMessage("Bitte nenne den Namen: ").queue(m -> messages.add(m));
                } else {
                    status = Status.unknown;
                }
                break;
            case name:
                entry.setName(msg);
                status = Status.miner;
                channel.sendMessage("Bitte nenne die Anzahl der Miner: ").queue( m -> messages.add(m));
                break;
            case miner:
                try {
                    entry.setMiners(Integer.parseInt(msg));
                } catch (NumberFormatException e) {
                }
                status = Status.repupulation;
                channel.sendMessage("Bitte nenne jetzt die Rep: ").queue(m -> messages.add(m));
                break;
            case repupulation:
                try {
                    entry.setRepopulation(Integer.parseInt(msg));
                } catch (NumberFormatException e) {
                }
                status = Status.accept;
                channel.sendMessage("Stimmen diese Daten?\n IP: " + entry.getIP() + "\nName: " + entry.getName() + "\nMiner: " + entry.getMiners() + "\n Repopulation: " + entry.getRepopulation()).queue(m -> messages.add(m));
                break;
            case accept:
                if (msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("Yes") || msg.equalsIgnoreCase("j") || msg.equalsIgnoreCase("y"))
                    status = Status.accepted;
                else
                    status = Status.unknown;
                onMessageReceived(event);
            case accepted:
                entry.setUser(user);
                addIPtoDB(entry);
                if (event.getGuild() != null) {
                    messages.forEach(m -> new Thread(new DeleteMessageThread(1, m)).start());
                    //event.getGuild().getTextChannelsByName("hackers-ip", true).get(0).sendMessage(new MessageBuilder().append(event.getAuthor()).append(" hat eine IP zur Datenbank hinzugefügt").build());
                }
                Content.deleteUser(user, this);
                break;
            case unknown:
                channel.sendMessage("abgebrochen").queue(m -> messages.add(m));
                messages.forEach(m -> new Thread(new DeleteMessageThread(0, m)).start());
                Content.deleteUser(user, this);

                break;
        }
    }
}
