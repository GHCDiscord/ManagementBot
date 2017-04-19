package ManagementBot.Content;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static ManagementBot.Content.Content.*;

public class RefreshUser extends Database {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member == null)
            member = Content.getGHCMember(event.getAuthor());
        if (isVerified(member)) {
            if (event.getChannel().getType() != ChannelType.PRIVATE)
                new Thread(new DeleteMessageThread(3, event.getMessage())).start();
            String result = refreshUser(event.getAuthor());
            if (result.equals("success"))
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Account wurde erfolgreich reaktiviert! Viel Spaß mit der IP-Datenbank der GCH unter " + url).queue());
            else
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Es ist ein unerwarteter Fehler aufgetreten! Bitte sende die folgenen Informationen an die Programmierer via @Coding:\n" + result).queue());
        }
    }
}
