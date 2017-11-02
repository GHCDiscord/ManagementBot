package de.ghc.managementbot.commands;

import de.ghc.managementbot.content.Content;
import de.ghc.managementbot.entity.Command;
import de.ghc.managementbot.entity.CountryRoleManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class AddRole extends CountryRoleManager implements Command {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        roleCommandExecution(event, Content::addRoles);
    }

    @Override
    public List<String> getCallers() {
        return Arrays.asList("!addrole", "!setrole");
    }

    @Override
    public boolean isCalled(String msg) {
        List<String> callers = getCallers();
        return callers.contains(msg.toLowerCase().split(" ")[0]);
    }
}
