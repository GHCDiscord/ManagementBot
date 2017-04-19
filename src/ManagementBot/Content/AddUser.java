package ManagementBot.Content;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static ManagementBot.Content.Content.isVerified;

public class AddUser extends Database {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Member member = event.getMember();
        if (member == null)
            member = Content.getGHCMember(event.getAuthor());
        if (isVerified(member)) {
            if (event.getChannel().getType() != ChannelType.PRIVATE)
                new Thread(new DeleteMessageThread(3, event.getMessage())).start();
            event.getAuthor().openPrivateChannel().queue();
            String[] usernamearr = event.getMessage().getContent().split(" ");
            if (usernamearr.length > 2) {
                event.getAuthor().openPrivateChannel().queue(DM -> {
                    DM.sendMessage("Bei Nutzernamen in der Datenbank ist nur ein Wort erlaubt (keine Leerzeichen).\nDein Account wird nun mit dem ersten Wort deines Nutzernamens erstellt").queue();
                });
            } else if (usernamearr.length < 1)
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Bitte gebe einen Nutzernamen für die Datenbank an!").queue(m -> new Thread(new DeleteMessageThread(120, m)).start()));
            String username = usernamearr[1];
            User user = new User(username);
            user.setDiscordUser(event.getAuthor());
            String response = registerNewUserInDB(user);
            String password = user.getPassword();
            if (response.equals("success"))
                event.getAuthor().openPrivateChannel().queue(DM -> {
                    DM.sendMessage(String.format("Dein Account wurde erfolgreich erstellt.\nNutzername: %s\nPasswort: %s\nDein Account ist nun 30 Tage gültig. Danach musst du mit `!refresh` deinen Account reaktivieren.\nViel Spaß mit der IP-Datenbank unter %s", username, password, url)).queue();
                });
            else if (response.equals("discord taken"))
                event.getAuthor().openPrivateChannel().queue(DM -> {
                    DM.sendMessage("Du hast bereits einen Account in der Datenbank. Reaktivieren kannst du diesen mit `!refresh`").queue(m -> new Thread(new DeleteMessageThread(120, m)).start());
                });
            else if (response.equals("name taken"))
                event.getAuthor().openPrivateChannel().queue(DM ->
                        DM.sendMessage(String.format("Der Nutzername %s ist bereits vergeben! Bitte wähle einen anderen Nuternamen!", username)).queue(m -> new Thread(new DeleteMessageThread(120, m)).start())
                );
            else
                event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Es ist ein unerwarteter Fehler aufgetreten! Bitte sende die folgenen Informationen an die Programmierer via @Coding:\n" + response).queue());

        }
    }
}
