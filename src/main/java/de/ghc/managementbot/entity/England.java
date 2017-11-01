package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.Data;

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
}
