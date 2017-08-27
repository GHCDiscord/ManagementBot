package de.ghc.managementbot.entity;

import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public interface Registrable {
    void addChannel(TextChannel channel);
    void removeChannel(TextChannel channel);
    List<TextChannel> getChannels();
    String getToken();
}
