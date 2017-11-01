package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.Content;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public abstract class Country {

    public static final Country GERMANY = new Germany();
    public static final Country ENGLAND = new England();

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
}
