package ManagementBot.Content;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

import static ManagementBot.Content.Content.getRandomColor;
import static ManagementBot.Content.Content.helpMessageAddIPGuilds;


public class AddIPWithQuestions extends AddIP {

    private IPEntry entry;
    ArrayList<Message> messages;
    User user;

    private enum Status {
        start, IP, name, miner, repupulation, guild, accept, accepted, unknown
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
                    onMessageReceived(event);
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
                status = Status.guild;
                channel.sendMessage("Schreibe nun die Informationen zur Gilde des Spielers. Wenn du nicht weißt wie, schreibe !help").queue(m -> messages.add(m));
                break;
            case guild:
                if (msg.equalsIgnoreCase("!help")) {
                    channel.sendMessage(new EmbedBuilder().setColor(getRandomColor()).setDescription(helpMessageAddIPGuilds).build()).queue(m -> messages.add(m));
                    break;
                } else if (msg.startsWith("-g"))
                    setGuild(1, msg.split(" "), entry);
                channel.sendMessage("Stimmen diese Daten?\n IP: " + entry.getIP() + "\nName: " + entry.getName() + "\nMiner: " + entry.getMiners() + "\n Repopulation: " + entry.getRepopulation() + "\nGuild: " + entry.getGuild()).queue(m -> messages.add(m));
                status = Status.accept;
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
                if (event.getGuild() != null && messages != null) {
                    for (Message m : messages) {
                        new Thread(new DeleteMessageThread(1, m)).start();
                    }
                    //event.getGuild().getTextChannelsByName("hackers-ip", true).get(0).sendMessage(new MessageBuilder().append(event.getAuthor()).append(" hat eine IP zur Datenbank hinzugefügt").build());
                }
                messages = null;
                Content.deleteUserAddIPWithQuestions(user, this);
                break;
            case unknown:
                channel.sendMessage("abgebrochen").queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
                messages.forEach(m -> new Thread(new DeleteMessageThread(0, m)).start());
                Content.deleteUserAddIPWithQuestions(user, this);
                break;
        }
    }
}
