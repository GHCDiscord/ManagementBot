package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.Registrable;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RegisterChannel implements Command {

    private Registrable registrable;
    public RegisterChannel(Registrable registrable) {
        this.registrable = registrable;
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
            event.getMessage().getMentionedChannels().forEach(ch -> {
                if (!registrable.getChannels().contains(ch)) {
                    registrable.addChannel(ch);
                    Content.getJda().getTextChannelById(Data.Channel.saves).sendMessage(registrable.getToken() + Data.SPLITTER + ch.getId()).queue();
                }
            });
    }
}
