package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.threads.DeleteMessageThread;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Collections;
import java.util.List;

import static de.ghc.managementbot.content.Content.addRole;

public class Verify implements Command {

  private static final int deleteTimeVerify = 600;

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (event.getGuild() != null) {
      if (!event.getMember().getRoles().contains(event.getGuild().getRolesByName("verified", true).get(0))) {
        addRole(event.getMember(), event.getGuild(), event.getGuild().getRoleById(Data.Role.verified));
        Message message = new MessageBuilder().append(event.getAuthor()).append(" ist nun verifiziert!").build();
        event.getMessage().getChannel().sendMessage(message).queue(m ->
            new Thread(new DeleteMessageThread(deleteTimeVerify, m)).start()
        );

      } else {
        Message message = new MessageBuilder().append(event.getAuthor()).append(" ist bereits verifiziert").build();
        event.getMessage().getChannel().sendMessage(message).queue(m ->
            new Thread(new DeleteMessageThread(deleteTimeVerify, m)).start()
        );
      }
      event.getMessage().delete().queue();
    }
  }

  @Override
  public List<String> getCallers() {
    return Collections.singletonList(".c3p0");
  }
}
