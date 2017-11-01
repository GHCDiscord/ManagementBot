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
        if (event.getMessageIdLong() == 363045230707998721L) { //letzte Nachricht in #glo_rules
            switch (event.getReactionEmote().getName()) {
                case "\uD83C\uDDE9\uD83C\uDDEA": //DE
                case "\uD83C\uDDE6\uD83C\uDDF9": //AT
                case "\uD83C\uDDE8\uD83C\uDDED": //CH
                    //German
                    Content.addRole(event.getMember(), event.getGuild(), event.getGuild().getRoleById(Country.GERMANY.getVerifiedRole()));
                    break;
                case "\uD83C\uDDFA\uD83C\uDDF8": //US
                case "\uD83C\uDDE8\uD83C\uDDE6": //CA
                case "\uD83C\uDDE6\uD83C\uDDFA": //AU
                case "\uD83C\uDDFA\uD83C\uDDF2": //UM
                case "\uD83C\uDDF0\uD83C\uDDFE": //KY
                case "\uD83C\uDDF1\uD83C\uDDF7": //LR
                case "\uD83C\uDDEC\uD83C\uDDE7": //GB
                case "\uD83C\uDDEE\uD83C\uDDEA": //IE
                case "\uD83C\uDDE6\uD83C\uDDEC": //AG
                    //English
                    Content.addRole(event.getMember(), event.getGuild(), event.getGuild().getRoleById(Country.ENGLAND.getVerifiedRole()));
                    break;
                default:
                    event.getUser().openPrivateChannel().queue(c -> c.sendMessage("This language is currently not supported. Please try another language. If you like to start a new country-area, please ask an admin.").queue());
                    break;
            }
            event.getReaction().removeReaction(event.getUser()).queue();
        }
    }
}
