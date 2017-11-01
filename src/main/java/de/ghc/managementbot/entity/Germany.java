package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.Data;

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
}
