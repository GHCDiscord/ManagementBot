package de.ghc.managementbot.listener;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Database;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class LeaveListener extends ListenerAdapter {

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (event.getGuild().equals(Content.getGhc())) {
            System.out.println(Database.expireUser(event.getMember().getUser()));
        }
    }
}
