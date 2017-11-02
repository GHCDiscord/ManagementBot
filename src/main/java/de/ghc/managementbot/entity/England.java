package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.Data;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;

/*package*/ class England extends Country {

    /*package*/ England() {
        super(Language.ENGLISH);
    }

    @Override
    public long getAdminRole() {
        return Data.Role.en_admin;
    }

    @Override
    public long getStaffRole() {
        return Data.Role.en_staff;
    }

    @Override
    public long getModeratorRole() {
        return Data.Role.en_moderator;
    }

    @Override
    public long getVerifiedRole() {
        return Data.Role.en_verifed;
    }

    @Override
    public long getGeneralChannel() {
        return Data.Channel.en_general;
    }

    @Override
    public long getRulesChannel() {
        return Data.Channel.en_rules;
    }

    @Override
    public long getHackersIpChannel() {
        return Data.Channel.en_hackersip;
    }

    @Override
    public void sendWelcomeMessage(Member member, Guild guild) {

    }
}
