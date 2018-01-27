package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.Data;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

/*package*/ class Germany extends Country {

    /*package*/ Germany() {
        super(Language.GERMAN);
    }

    @Override
    public long getAdminRole() {
        return Data.Role.de_admin;
    }

    @Override
    public long getStaffRole() {
        return Data.Role.de_staff;
    }

    @Override
    public long getModeratorRole() {
        return Data.Role.de_moderator;
    }

    @Override
    public long getVerifiedRole() {
        return Data.Role.de_verified;
    }

    @Override
    public long getGeneralChannel() {
        return Data.Channel.de_general;
    }

    @Override
    public long getRulesChannel() {
        return Data.Channel.de_regeln;
    }

    @Override
    public long getHackersIpChannel() {
        return Data.Channel.de_hackersip;
    }

    @Override
    public void sendWelcomeMessage(Member member, Guild guild) {
        guild.getTextChannelById(getGeneralChannel()).sendMessageFormat("Willkommen %s in der GHC. Bitte lese dir auch die <#%s> durch.", member.getAsMention(), getRulesChannel()).queue();
    }
}
