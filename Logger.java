package de.ghc.managementbot.listener;

import de.ghc.managementbot.content.Content;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.message.MessageEmbedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Logger extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        new Thread(() -> {
            synchronized (this) {
                try {
                    while (Content.getGhc() == null)
                        this.wait(2000);
                } catch (InterruptedException ignored) {}
                Content.getGhc().getTextChannelById("270136161760968705").sendMessage("GHC-Bot mit Version " + Content.version + " gestartet").queue();
            }
        }).start();
    }

    @Override
    public void onGuildBan(GuildBanEvent event) {
        if (Content.getGhc() != null)
            Content.getGhc().getTextChannelById("270136161760968705").sendMessage(event.getUser() + " wurde gebannt").queue();
    }
}
