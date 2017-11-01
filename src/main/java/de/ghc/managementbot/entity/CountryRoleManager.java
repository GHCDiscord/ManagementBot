package de.ghc.managementbot.entity;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.content.Data;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CountryRoleManager {

    protected void roleCommandExecution(MessageReceivedEvent event, final RoleExecute todo) {
        if (event.getTextChannel() == null)
            return; //no Guild
        Member author = event.getMember();
        if (author.getGuild().getIdLong() != Data.Guild.GHC)
            return;
        Country country = Content.getCountry(event.getTextChannel());
        final List<Role> mentionedRoles = event.getMessage().getMentionedRoles();
        final List<User> mentionedUsers = event.getMessage().getMentionedUsers();
        final List<Member> members = new ArrayList<>();
        mentionedUsers.forEach(u -> members.add(author.getGuild().getMember(u)));
        if (country.isAdmin(author)) {
            for (Member member: members) {
                if (country.isVerified(member)) {
                    List<Role> intractableRoles = new ArrayList<>();
                    for (Role role : mentionedRoles) {
                        if (country.isCountryRole(role) && author.canInteract(role)) {
                            intractableRoles.add(role);
                        }
                    }
                    todo.execute(member, intractableRoles);
                }
            }
        }
    }

    public static boolean isCountryRole(long role) {
        return Country.ENGLAND.isCountryRole(role) || Country.GERMANY.isCountryRole(role);
    }

    public static boolean isCountryRole(Role role) {
        return Country.ENGLAND.isCountryRole(role) || Country.GERMANY.isCountryRole(role);
    }

    public static List<Role> getCountryRoles(Member member) {
        if (member == null)
            return Collections.emptyList();
        List<Role> roles = member.getRoles();
        List<Role> countryRoles = new ArrayList<>(roles);
        countryRoles.removeIf(r -> !isCountryRole(r));
        return countryRoles;
    }

    public static List<Role> getCountryRoles(User user) {
        return getCountryRoles(Content.getGHCMember(user));
    }

    public interface RoleExecute {
        void execute(Member member, List<Role> roles);
    }

}
