package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.Content;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

public abstract class Country {

    public static final Country GERMANY = new Germany();
    public static final Country ENGLAND = new England();

    public static void handleCountryReaction(MessageReactionAddEvent event) {
        if (event.getMessageIdLong() == 363045230707998721L) { //letzte Nachricht in #glo_rules
            switch (event.getReactionEmote().getName()) {
                case "\uD83C\uDDE9\uD83C\uDDEA": //DE
                case "\uD83C\uDDE6\uD83C\uDDF9": //AT
                case "\uD83C\uDDE8\uD83C\uDDED": //CH
                    //German
                    Content.addRole(event.getMember(), event.getGuild(), event.getGuild().getRoleById(GERMANY.getVerifiedRole()));
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
                    Content.addRole(event.getMember(), event.getGuild(), event.getGuild().getRoleById(ENGLAND.getVerifiedRole()));
                    break;
                default:
                    event.getUser().openPrivateChannel().queue(c -> c.sendMessage("This language is currently not supported. Please try another language. If you like to start a new country-area, please ask an admin.").queue());
                    break;
            }
            event.getReaction().removeReaction(event.getUser()).queue();
        }
    }

    private final Language language;

    /*package*/ Country(Language language) {
        this.language = language;
    }

    public Language getLanguage() {
        return language;
    }

    public boolean isAdmin(Member member) {
        return Content.hasRole(member, getAdminRole());
    }
    public abstract long getAdminRole();

    public boolean isStaff(Member member) {
        return Content.hasRole(member, getStaffRole());
    }
    public abstract long getStaffRole();

    public boolean isModerator(Member member) {
        return Content.hasRole(member, getModeratorRole());
    }

    public abstract long getModeratorRole();

    public boolean isVerified(Member member) {
        return Content.hasRole(member, getVerifiedRole());
    }
    public abstract long getVerifiedRole();

    public boolean isCountryRole(Role role) {
        return isCountryRole(role.getIdLong());
    }
    public boolean isCountryRole(long role) {
        return role == getAdminRole() || role == getModeratorRole() || role == getVerifiedRole() || role == getStaffRole();
    }

    public abstract long getGeneralChannel();
    public abstract long getRulesChannel();
    public abstract long getHackersIpChannel();

    public abstract void sendWelcomeMessage(Member member, Guild guild);
}
