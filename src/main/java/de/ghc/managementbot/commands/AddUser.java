package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Database;
import de.ghc.managementbot.entity.User;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import static de.ghc.managementbot.content.Content.isVerified;

public class AddUser extends Database implements Command {
  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    Member member = event.getMember();
    if (member == null) {
      member = Content.getGHCMember(event.getAuthor());
    }
    if ((event.getGuild() == null && isVerified(member)) || (event.getTextChannel().equals(event.getGuild().getTextChannelById("269153131957321728"))) && isVerified(member)) {
      if (event.getChannel().getType() != ChannelType.PRIVATE) {
        new Thread(new DeleteMessageThread(3, event.getMessage())).start();
      }
      event.getAuthor().openPrivateChannel().queue(DM -> DM.sendTyping().queue());
      String[] usernamearr = event.getMessage().getContent().split(" ");
      if (usernamearr.length > 2) {
        event.getAuthor().openPrivateChannel().queue(DM -> {
          DM.sendMessage("Bei Nutzernamen in der Datenbank ist nur ein Wort erlaubt (keine Leerzeichen).\nDein Account wird nun mit dem ersten Wort deines Nutzernamens erstellt").queue();
        });
      } else if (usernamearr.length < 1) {
        event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Bitte gebe einen Nutzernamen f\u00FCr die Datenbank an!").queue(m -> new Thread(new DeleteMessageThread(120, m)).start()));
      }
      String username = usernamearr[1];
      if (!username.matches("[a-zA-Z0-9_]*")) {
        event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Im Nutzernamen sind nur A-Z, a-z, 0-9 und _ erlaubt!").queue());
        return;
      }
      User user = new User(username);
      user.setDiscordUser(event.getAuthor());
      new Thread(() -> {
        String response = registerNewUserInDB(user);
        String password = user.getPassword();
        if (response.equals("success")) {
          event.getAuthor().openPrivateChannel().queue(DM -> {
            DM.sendMessage(String.format("Dein Account wurde erfolgreich erstellt.\nNutzername: %s\nPasswort: %s\nDein Account ist nun 30 Tage g\u00FCltig. Danach musst du mit `!refresh` deinen Account reaktivieren.\nViel Spaß mit der IP-Datenbank unter %s", username, password, url)).queue();
          });
        } else if (response.equals("discord taken")) {
          event.getAuthor().openPrivateChannel().queue(DM -> {
            DM.sendMessage("Du hast bereits einen Account in der Datenbank. Reaktivieren kannst du diesen mit `!refresh`").queue(m -> new Thread(new DeleteMessageThread(120, m)).start());
          });
        } else if (response.equals("name taken")) {
          event.getAuthor().openPrivateChannel().queue(DM ->
              DM.sendMessage(String.format("Der Nutzername %s ist bereits vergeben! Bitte w\u00E4hle einen anderen Nutzernamen!", username)).queue(m -> new Thread(new DeleteMessageThread(120, m)).start())
          );
        } else {
          event.getAuthor().openPrivateChannel().queue(DM -> DM.sendMessage("Es ist ein unerwarteter Fehler aufgetreten! Bitte sende die folgenden Informationen an die Programmierer via @Coding:\n" + response).queue());
        }
      }).start();
    }
  }
}
