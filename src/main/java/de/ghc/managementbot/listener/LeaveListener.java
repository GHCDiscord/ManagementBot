package de.ghc.managementbot.listener;

import de.ghc.managementbot.commands.UpdateIP;
import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import de.ghc.managementbot.content.Database;
import de.ghc.managementbot.entity.Country;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class LeaveListener extends ListenerAdapter {

    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        if (event.getGuild().getIdLong() == Data.Guild.GHC) {
            System.out.println(Database.expireUser(event.getMember().getUser()));
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        long msgID = event.getMessageIdLong();
        //UpdateIP
        if (UpdateIP.getMessageUpdateIp().containsKey(msgID) && event.getReactionEmote().getName().equals("✅")) {
            if (Content.isStaff(event.getMember()))
                if (!UpdateIP.getMessageUpdateIp().get(msgID).updateIP(msgID))
                    event.getChannel().getMessageById(event.getMessageId()).complete().clearReactions().queue();
            else
                event.getChannel().getMessageById(event.getMessageId()).complete().clearReactions().queue();
        }

        //Welcome
        Country.handleCountryReaction(event);
    }
}
