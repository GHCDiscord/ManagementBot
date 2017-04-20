package ManagementBot.Content;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static ManagementBot.Content.Content.isVerified;

public class RefreshUser extends Database implements Command {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getType() != ChannelType.PRIVATE)
            new Thread(new DeleteMessageThread(3, event.getMessage())).start();
        Member member = event.getMember();
        if (member == null)
            member = Content.getGHCMember(event.getAuthor());
        if (isVerified(member)) {
            String result = refreshUser(event.getAuthor());
            if (result.equals("success"))
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Account wurde erfolgreich reaktiviert! Viel Spaß mit der IP-Datenbank der GCH unter " + url).queue());
            else if (result.equals("user not found"))
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Es wurde kein Account gefunden, der mit deinem Discord-Account verbunden ist. Bitte registiere dich zuerst mit `!register` und deinem gewünschten Namen in der Datenbank!").queue());
            else
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Es ist ein unerwarteter Fehler aufgetreten! Bitte sende die folgenden Informationen an die Programmierer via @Coding:\n" + result).queue());
        }
    }
}
