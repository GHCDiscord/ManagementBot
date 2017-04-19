package ManagementBot.Content;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

import static ManagementBot.Content.Content.isVerified;


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
        Member member = event.getMember();
        if (member == null)
            member = Content.getGHCMember(event.getAuthor());
        if (isVerified(member)) {
            switch (status) {
                case start:
                    channel.sendMessage("Bitte nenne die IP: ").queue(messages::add);
                    status = Status.IP;
                    break;
                case IP:
                    if (checkIP(msg)) {
                        entry = new IPEntry(msg);
                        status = Status.name;
                        channel.sendMessage("Bitte nenne den Namen: ").queue(messages::add);
                    } else {
                        status = Status.unknown;
                        onMessageReceived(event);
                    }
                    break;
                case name:
                    entry.setName(msg);
                    status = Status.miner;
                    channel.sendMessage("Bitte nenne die Anzahl der Miner: ").queue(messages::add);
                    break;
                case miner:
                    try {
                        entry.setMiners(Integer.parseInt(msg));
                    } catch (NumberFormatException e) {
                    }
                    status = Status.repupulation;
                    channel.sendMessage("Bitte nenne jetzt die Rep: ").queue(messages::add);
                    break;
                case repupulation:
                    try {
                        entry.setRepopulation(Integer.parseInt(msg));
                    } catch (NumberFormatException e) {
                    }
                    status = Status.guild;
                    channel.sendMessage("Schreibe nun den Guild-Tag. Wenn er in keiner Gilde ist, schreibe null").queue(messages::add);
                    break;
                case guild:
                    if (msg.length() == 3)
                        entry.setGuildTag(msg);
                    channel.sendMessage("Stimmen diese Daten?\n IP: " + entry.getIP() + "\nName: " + entry.getName() + "\nMiner: " + entry.getMiners() + "\nRepopulation: " + entry.getRepopulation() + "\nGuild: " + entry.getGuildTag() + "\nBeschreibung: " + entry.getDescription()+ "\n schreibe 'Ja' zum bestätigen.").queue(messages::add);
                    status = Status.accept;
                    break;
                case accept:
                    if (msg.equalsIgnoreCase("ja") || msg.equalsIgnoreCase("Yes") || msg.equalsIgnoreCase("j") || msg.equalsIgnoreCase("y"))
                        status = Status.accepted;
                    else
                        status = Status.unknown;
                    onMessageReceived(event);
                    break;
                case accepted:
                    Content.deleteUserAddIPWithQuestions(user, this);
                    entry.setUser(user);
                    String result = addIPtoDB(entry);
                    if (event.getGuild() != null && messages != null) {
                        for (Message m : messages) {
                            new Thread(new DeleteMessageThread(0, m)).start();
                        }
                        if (result.equals("1")) {
                        //event.getGuild().getTextChannelsByName("hackers-ip", true).get(0).sendMessage(new MessageBuilder().append(event.getAuthor()).append(" hat eine IP zur Datenbank hinzugefügt").build()).queue(m -> new Thread(new DeleteMessageThread(86400, m)));
                        } else
                            channel.sendMessage("Es ist ein Fehler aufgetreten:\n" + result).queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
                    }
                    messages = null;
                    break;
                case unknown:
                    channel.sendMessage("abgebrochen").queue(m -> new Thread(new DeleteMessageThread(30, m)).start());
                    messages.forEach(m -> new Thread(new DeleteMessageThread(0, m)).start());
                    Content.deleteUserAddIPWithQuestions(user, this);
                    break;
            }
        }
    }
}
